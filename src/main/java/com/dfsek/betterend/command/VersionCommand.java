package com.dfsek.betterend.command;

import com.dfsek.betterend.BetterEnd;
import com.dfsek.betterend.config.LangUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.command.Command;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class VersionCommand extends Command {
    public VersionCommand(Command parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "version";
    }

    @Override
    public List<Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Plugin gaea = Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Gaea"));
        String gaeaVersion = gaea.getDescription().getVersion();
        LangUtil.send("commands.version", sender, getMain().getDescription().getVersion() + "-" + (BetterEnd.isPremium() ? "premium" : "free"), gaeaVersion);
        return true;
    }

    @Override
    public int arguments() {
        return 0;
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        return Collections.emptyList();
    }
}
