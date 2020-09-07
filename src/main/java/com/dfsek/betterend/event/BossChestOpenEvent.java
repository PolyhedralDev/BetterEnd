package com.dfsek.betterend.event;

import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BossChestOpenEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Chest chest;
    private final Location spawnLocation;
    private final String bossName;
    private final Player player;
    private boolean cancelled;

    public BossChestOpenEvent(Chest chest, Location spawn, String bossName, Player p) {
        this.chest = chest;
        this.spawnLocation = spawn;
        this.bossName = bossName;
        this.player = p;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Chest getChest() {
        return chest;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public Player getPlayer() {
        return player;
    }

    public String getBossName() {
        return bossName;
    }

    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}
