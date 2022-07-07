package me.evilterabite.rplace.hook;

import me.evilterabite.rplace.RPlace;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

public class HookHandler {

    private HookHandler() {}

    private static final Map<String, Class<? extends Hook>> HOOK_CLASSES = new HashMap<>();
    private static final Map<Class<? extends Hook>, Hook> LOADED_HOOKS = new HashMap<>();

    public static void registerHook(String pluginName, Class<? extends Hook> clazz) {
        HOOK_CLASSES.put(pluginName, clazz);
    }

    public static void loadHooks() {
        for(Map.Entry<String, Class<? extends Hook>> entry : HOOK_CLASSES.entrySet()) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin(entry.getKey());
            if(plugin == null || !plugin.isEnabled())
                continue;

            Hook hook = constructHook(entry.getValue(), plugin);
            if(hook == null)
                continue;

            LOADED_HOOKS.put(entry.getValue(), hook);
            RPlace.getInstance().getLogger().info("Successfully hooked into " + plugin.getDescription().getName() + " v" + plugin.getDescription().getVersion());
        }
    }

    private static Hook constructHook(Class<? extends Hook> clazz, Plugin plugin) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (ReflectiveOperationException ex) {
            RPlace.getInstance().getLogger().log(Level.SEVERE, "Failed to construct instance of " + clazz.getName(), ex);
            return null;
        } catch (Exception ex) {
            PluginDescriptionFile desc = plugin.getDescription();
            RPlace.getInstance().getLogger().log(Level.SEVERE, String.format("Failed to hook into %s. Is the current version of %s (%s) supported by FoxKingdoms?", desc.getName(), desc.getName(), desc.getVersion()), ex);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> getHook(Class<? extends T> clazz) {
        return Optional.ofNullable((T) LOADED_HOOKS.get(clazz));
    }

    public interface Hook {}

}
