package me.evilterabite.rplace.utils;

import me.evilterabite.rplace.RPlace;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class C {

    private static final FileConfiguration config = RPlace.getInstance().getConfig();

    public static void consoleNotAllowed(CommandSender sender) {
        sender.sendMessage("Not allowed on console");
    }

    public static void noPermission(CommandSender sender) {
        sender.sendMessage("No Permission");
    }

    public static String timerNotFinished() {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("msg_timernotfinished")));
    }

    public static String outsideOfCanvas() {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("msg_outsideofcanvas")));
    }

    public static String insideFromOutsideOfCanvas() {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("msg_insidefromoutsideofcanvas")));
    }

    public static String canvasEnter() {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("msg_canvasenter")));
    }

    public static String canvasLeave() {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("msg_canvasleave")));
    }

    public static String canvasEnterOther() {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("msg_canvasenter_other")));
    }

    public static String canvasLeaveOther() {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("msg_canvasleave_other")));
    }

    public static String noTeleport() {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("msg_noteleport")));
    }

    public static String cannotEnterInCombat() {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("msg_cannot_enter_in_combat")));
    }

    public static boolean canvasEnterLeaveMessageEnabled() {
        return config.getBoolean("canvas_enter_leave_message");
    }

    public static boolean canvasEnterLeaveMessageOtherEnabled() {
        return config.getBoolean("canvas_enter_leave_message_other");
    }

    public static boolean timerOutsideCanvas() {
        return config.getBoolean("show_timer_outside_canvas");
    }

    public static Boolean invisPlayer() {
        return config.getBoolean("invis_player");
    }

    public static int canvasZoneLimit() {
        return config.getInt("canvas_zone_limits");
    }
}
