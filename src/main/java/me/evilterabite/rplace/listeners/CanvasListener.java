package me.evilterabite.rplace.listeners;

import me.evilterabite.rplace.RPlace;
import me.evilterabite.rplace.events.PlayerEnterCanvasEvent;
import me.evilterabite.rplace.events.PlayerLeaveCanvasEvent;
import me.evilterabite.rplace.utils.C;
import me.evilterabite.rplace.utils.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

public class CanvasListener implements Listener {

    public static HashMap<UUID, ItemStack[]> playerInventoryMap = new HashMap<>();
    public static HashMap<UUID, ItemStack[]> playerArmourMap = new HashMap<>();
    public static ItemStack paletteItem = ItemCreator.create(Material.CHEST, ChatColor.LIGHT_PURPLE + "Open the Block Palette", "");

    @EventHandler
    void onEnterCanvas(PlayerEnterCanvasEvent event) {
        if(RPlace.canvas == null) return;
        RPlace.playersInCanvas.add(event.getPlayer().getUniqueId());
        storePlayerContents(event.getPlayer());
        event.getPlayer().sendMessage(C.canvasEnter());
        if(C.invisPlayer()) {
            event.getPlayer().setInvisible(true);
        }
        event.getPlayer().getInventory().setItem(0, paletteItem);
    }

    @EventHandler
    void onLeaveCanvas(PlayerLeaveCanvasEvent event) {
        if(RPlace.canvas == null)
            return;

        Player player = event.getPlayer();
        RPlace.playersInCanvas.remove(player.getUniqueId());
        restorePlayerContents(player);

        player.sendMessage(C.canvasLeave());
        if(C.invisPlayer()) {
            player.setInvisible(false);
        }
    }

    public static void storePlayerContents(Player player) {
        ItemStack[] invContents = player.getInventory().getContents();
        ItemStack[] armorContents = player.getInventory().getArmorContents();

        playerInventoryMap.put(player.getUniqueId(), invContents);
        playerArmourMap.put(player.getUniqueId(), armorContents);
        player.getInventory().clear();

        // Also save the inventory data to persistent storage
        // This isn't used normally, but is needed in case of server crashes
        // to be able to restore player inventories
        Bukkit.getScheduler().runTaskAsynchronously(RPlace.getInstance(), () ->
                RPlace.getInstance().getDataStorage().storeInventory(player.getUniqueId(), invContents, armorContents));

        // This key is checked when a player joins the server. It will be removed
        // on a proper exit (when a player gets their inventory back). If it's still
        // there at that point, it means the player needs to get their inventory
        // restored, which will be done from the data in persistent storage.
        player.getPersistentDataContainer().set(getRestoreKey(), PersistentDataType.BYTE, (byte) 0x01);
    }

    public static void restorePlayerContents(Player player) {
        if(RPlace.canvas == null)
            return;

        player.getPersistentDataContainer().remove(getRestoreKey());

        ItemStack[] invContents = playerInventoryMap.get(player.getUniqueId());
        if(invContents != null) {
            player.getInventory().setContents(invContents);
            playerInventoryMap.remove(player.getUniqueId());
        }

        ItemStack[] armorContents = playerArmourMap.get(player.getUniqueId());
        if(armorContents != null) {
            player.getInventory().setArmorContents(armorContents);
            playerArmourMap.remove(player.getUniqueId());
        }
    }

    public static NamespacedKey getRestoreKey() {
        return new NamespacedKey(RPlace.getInstance(), "inventory_restore_needed");
    }

}
