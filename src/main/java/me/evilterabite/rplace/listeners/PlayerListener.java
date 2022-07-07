package me.evilterabite.rplace.listeners;

import me.evilterabite.rplace.RPlace;
import me.evilterabite.rplace.commands.CanvasCommand;
import me.evilterabite.rplace.events.PlayerEnterCanvasEvent;
import me.evilterabite.rplace.events.PlayerLeaveCanvasEvent;
import me.evilterabite.rplace.hook.HookHandler;
import me.evilterabite.rplace.hook.PvPManagerHook;
import me.evilterabite.rplace.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.logging.Level;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PersistentDataContainer pdc = player.getPersistentDataContainer();

        if(pdc.has(CanvasListener.getRestoreKey(), PersistentDataType.BYTE)) {
            pdc.remove(CanvasListener.getRestoreKey());
            RPlace.getInstance().getLogger().warning("The inventory of " + player.getName() + " wasn't restored properly when they left the server! Attempting to load it from persistent storage now.");

            if(RPlace.getInstance().getDataStorage().restoreInventory(player)) {
                RPlace.getInstance().getLogger().info("Restore successful!");
            } else {
                RPlace.getInstance().getLogger().severe("Could not restore!");
            }
        }
    }

    @EventHandler
    void onPlayerMove(PlayerMoveEvent event) {
        if(RPlace.canvas == null || event.getTo() == null || !movedBlock(event.getFrom(), event.getTo()))
            return;

        Player player = event.getPlayer();
        if(RPlace.canvasZone.getWorld() != player.getWorld())
            return;

        boolean inCanvas = RPlace.canvasZone.contains(event.getTo());
        if(inCanvas && !RPlace.playersInCanvas.contains(player.getUniqueId())) {
            if(HookHandler.getHook(PvPManagerHook.class).map(hook -> hook.isInCombat(player)).orElse(false)) {
                player.sendMessage(C.cannotEnterInCombat());
                event.setCancelled(true);
                return;
            }

            Bukkit.getPluginManager().callEvent(new PlayerEnterCanvasEvent(player));
        } else if(!inCanvas && RPlace.playersInCanvas.contains(player.getUniqueId())) {
            Bukkit.getPluginManager().callEvent(new PlayerLeaveCanvasEvent(player));
        }
    }

    private boolean movedBlock(Location from, Location to) {
        return from.getBlockX() != to.getBlockX() ||
                from.getBlockY() != to.getBlockY() ||
                from.getBlockZ() != to.getBlockZ();
    }

    @EventHandler
    void onPlayerDisconnect(PlayerQuitEvent event) {
        if(RPlace.playersInCanvas.contains(event.getPlayer().getUniqueId())) {
            Bukkit.getPluginManager().callEvent(new PlayerLeaveCanvasEvent(event.getPlayer()));
        }

        Bukkit.getScheduler().runTaskAsynchronously(RPlace.getInstance(), () ->  RPlace.getInstance().getDataStorage().removeFile(event.getPlayer()));
    }

    @EventHandler
    void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        if(event.getFrom() == RPlace.canvas.getZone().getWorld()) {
            if (RPlace.playersInCanvas.contains(event.getPlayer().getUniqueId())) {
                RPlace.getInstance().getLogger().log(Level.SEVERE, "You should not let players change worlds while on the canvas! This can cause major issues.");
                Bukkit.getPluginManager().callEvent(new PlayerLeaveCanvasEvent(event.getPlayer()));
            }
        }
    }

    @EventHandler
    void onPlayerTeleport(PlayerTeleportEvent event) {
        if(RPlace.playersInCanvas.contains(event.getPlayer().getUniqueId())) {
            event.getPlayer().sendMessage("Please leave the canvas before teleporting out!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    void onPlayerInteract(PlayerInteractEvent event) {
        if(!event.hasItem()) return;
        if(!event.getItem().hasItemMeta()) return;
        if(event.getItem().getItemMeta().getDisplayName().equals(CanvasCommand.modItem.getItemMeta().getDisplayName())) {
            if(RPlace.playersInCanvas.contains(event.getPlayer().getUniqueId()) && event.getPlayer().hasPermission("rplace.moderator")) {
                Block block = event.getClickedBlock();
                block.setType(Material.WHITE_CONCRETE);
            }
        }
        if(event.getItem().getItemMeta().getDisplayName().equals(CanvasListener.paletteItem.getItemMeta().getDisplayName())) {
            RPlace.paletteGUI.open(event.getPlayer());
        }
    }
}
