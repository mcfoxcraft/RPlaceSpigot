package me.evilterabite.rplace;

import me.evilterabite.rplace.utils.FileUtil;
import me.evilterabite.rplace.utils.FileUtil.YamlFile;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class DataStorage {

    public void storeInventory(UUID playerId, ItemStack[] invContents, ItemStack[] armorContents) {
        YamlFile file = FileUtil.create("inventories/" + playerId + ".yml");
        FileConfiguration data = file.getData();
        this.storeItems(data.createSection("inventory"), invContents);
        this.storeItems(data.createSection("armor"), armorContents);
        file.save();
    }

    public boolean restoreInventory(Player player) {
        YamlFile file = FileUtil.load("inventories/" + player.getUniqueId() + ".yml");
        if(file == null)
            return false; // player has no stored inventory

        FileConfiguration data = file.getData();
        player.getInventory().setContents(loadItems(data.getConfigurationSection("inventory")));
        player.getInventory().setArmorContents(loadItems(data.getConfigurationSection("armor")));
        return true;
    }

    // This is more efficient than directly storing the array
    // because any 'null' item stacks (empty slots) aren't stored
    private void storeItems(ConfigurationSection section, ItemStack[] items) {
        section.set("size", items.length);
        for(int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            if(item != null && item.getType() != Material.AIR) {
                section.set(String.valueOf(i), item);
            }
        }
    }

    private ItemStack[] loadItems(ConfigurationSection section) {
        if(section == null)
            return null;

        int size = section.getInt("size");
        ItemStack[] items = new ItemStack[size];

        for(String key : section.getKeys(false)) {
            if(key.equals("size"))
                continue;
            int slot = Integer.parseInt(key);
            items[slot] = section.getItemStack(key);
        }

        return items;
    }

}
