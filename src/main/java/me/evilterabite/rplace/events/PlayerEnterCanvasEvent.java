package me.evilterabite.rplace.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerEnterCanvasEvent extends Event {

    public static HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private boolean cancelled;

    public PlayerEnterCanvasEvent(Player player) {
        this.player = player;
        cancelled = false;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
