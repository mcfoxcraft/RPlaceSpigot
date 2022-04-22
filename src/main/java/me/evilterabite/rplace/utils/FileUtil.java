package me.evilterabite.rplace.utils;

import me.evilterabite.rplace.RPlace;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class FileUtil {

    private FileUtil() {}

    public static YamlFile create(String fileName) {
        File file = new File(RPlace.getInstance().getDataFolder(), fileName);
        try {
            if(!file.exists() || !file.isFile()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            return new YamlFile(file, new YamlConfiguration());
        } catch (IOException ex) {
            RPlace.getInstance().getLogger().log(Level.SEVERE, "Failed to create " + file.getName(), ex);
            return null;
        }
    }

    public static YamlFile load(String fileName) {
        File file = new File(RPlace.getInstance().getDataFolder(), fileName);
        FileConfiguration data = new YamlConfiguration();

        try {
            if(!file.exists() || !file.isFile())
                return null;

            data.load(file);
            return new YamlFile(file, data);
        } catch (IOException | InvalidConfigurationException ex) {
            RPlace.getInstance().getLogger().log(Level.SEVERE, "Failed to load " + file.getName(), ex);
            return null;
        }
    }

    public static class YamlFile {

        private final File file;
        private final FileConfiguration data;

        private YamlFile(File file, FileConfiguration data) {
            this.file = file;
            this.data = data;
        }

        public File getFile() {
            return file;
        }

        public FileConfiguration getData() {
            return data;
        }

        public void save() {
            try {
                data.save(file);
            } catch (IOException ex) {
                RPlace.getInstance().getLogger().log(Level.SEVERE, "Failed to save " + file.getName(), ex);
            }
        }

    }

}
