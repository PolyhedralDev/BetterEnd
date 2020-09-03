package org.polydev.gaea.profiler;

import net.md_5.bungee.api.ChatColor;

public class DataHolder {
    private final long desired;
    private final DataType type;
    private final double desiredRangePercent;

    public DataHolder(DataType type, long desired, double desiredRangePercent) {
        this.desired = desired;
        this.type = type;
        this.desiredRangePercent = desiredRangePercent;
    }

    public String getFormattedData(long data) {
        double range = desiredRangePercent*desired;
        ChatColor color = ChatColor.YELLOW;
        if(Math.abs(data - desired) > range) {
            if(data > desired) color = type.getDesire().getHighColor();
            else color = type.getDesire().getLowColor();
        }
        return color + type.getFormatted(data) + ChatColor.RESET;
    }
}
