package com.dfsek.betterend.command;

import com.dfsek.betterend.command.profile.ProfileCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.GaeaPlugin;
import org.polydev.gaea.command.Command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BetterEndCommand extends Command {
    public BetterEndCommand(GaeaPlugin main) {
        super(main);
    }

    @Override
    public String getName() {
        return "be";
    }

    @Override
    public List<Command> getSubCommands() {
        return Arrays.asList(new VersionCommand(this),
                new ReloadCommand(this),
                new BiomeCommand(this),
                new ProfileCommand(this));
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
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
