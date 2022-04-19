package me.evilterabite.rplace.libraries;

import me.evilterabite.rplace.RPlace;
import me.evilterabite.rplace.commands.CanvasCommand;
import me.evilterabite.rplace.events.CanvasCreateEvent;
import me.evilterabite.rplace.utils.Zone;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Canvas {

    private String name;
    private Zone zone;
    private int placeBlockTimer;

    public Canvas(String name, Zone zone, int placeBlockTimer) {
        this.name = name;
        this.zone = zone;
        this.placeBlockTimer = placeBlockTimer;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public int getPlaceBlockTimer() {
        return placeBlockTimer;
    }

    public void setPlaceBlockTimer(int placeBlockTimer) {
        Plugin plugin = RPlace.getPlugin(RPlace.class);
        plugin.getConfig().set("place_timer", placeBlockTimer);
        plugin.saveConfig();
        plugin.reloadConfig();
        this.placeBlockTimer = placeBlockTimer;
    }

    public void create() {
        reset();
        store();
        RPlace.canvas = this;
        RPlace.canvasZone = new Zone(CanvasCommand.posList.get(0).subtract(0, 50, 0), CanvasCommand.posList.get(1).add(0, 50, 0));
        Bukkit.getPluginManager().callEvent(new CanvasCreateEvent(this));
    }

    public void recover() {
        RPlace.canvas = this;
        RPlace.canvasZone = new Zone(
                new Location(zone.getWorld(), zone.getMinX(), zone.getMinY() - 50, zone.getMinZ()),
                new Location(zone.getWorld(), zone.getMaxX(), zone.getMaxY() + 50, zone.getMaxZ())
        );
    }

    public void reset() {
        List<Block> canvasBlocks = zone.select();
        for(Block b : canvasBlocks) {
            b.setType(Material.WHITE_CONCRETE);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void store() {
        RPlace.getPlugin(RPlace.class).getConfig().set("canvas", this.toString());
        RPlace.getPlugin(RPlace.class).saveConfig();

    }

    public static Canvas deserialize(String serializedCanvas) {
        List<String> rawCanvas = new ArrayList<>(Arrays.asList(serializedCanvas.split(":")));
        if(!rawCanvas.isEmpty()) {
            List<String> rawZone = new ArrayList<>(Arrays.asList(rawCanvas.get(1).split(",")));
            String name = rawCanvas.get(0);
            Zone zone = new Zone(Bukkit.getWorld(rawZone.get(0)),
                    Integer.parseInt(rawZone.get(1)),
                    Integer.parseInt(rawZone.get(2)),
                    Integer.parseInt(rawZone.get(3)),
                    Integer.parseInt(rawZone.get(4)),
                    Integer.parseInt(rawZone.get(5)),
                    Integer.parseInt(rawZone.get(6)));
            int placeBlockTimer = Integer.parseInt(rawCanvas.get(2));

            return new Canvas(name, zone, placeBlockTimer);
        }

        return null;
    }

    @Override
    public String toString() {
        String separator = ":";
        return name + separator + zone.toString() + separator + placeBlockTimer;
    }
}
