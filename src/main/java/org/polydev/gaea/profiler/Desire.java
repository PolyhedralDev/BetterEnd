package org.polydev.gaea.profiler;

import net.md_5.bungee.api.ChatColor;

public enum Desire {
    LOW(ChatColor.RED, ChatColor.GREEN), HIGH(ChatColor.GREEN, ChatColor.RED);

    private final ChatColor high;
    private final ChatColor low;
    Desire(ChatColor high, ChatColor low) {
        this.high = high;
        this.low = low;
    }

    public ChatColor getHighColor() {
        return high;
    }

    public ChatColor getLowColor() {
        return low;
    }
}
