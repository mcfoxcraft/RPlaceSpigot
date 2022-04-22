package me.evilterabite.rplace.events;

import me.evilterabite.rplace.libraries.Canvas;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CanvasCreateEvent extends Event {

    public static HandlerList HANDLERS = new HandlerList();

    private final Canvas canvas;

    public CanvasCreateEvent(Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Canvas getCanvas() {
        return canvas;
    }
}
