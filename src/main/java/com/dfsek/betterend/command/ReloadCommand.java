package com.dfsek.betterend.command;

import com.dfsek.betterend.config.ConfigUtil;
import com.dfsek.betterend.config.LangUtil;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.command.Command;
import org.polydev.gaea.command.DebugCommand;

import java.util.Collections;
import java.util.List;

public class ReloadCommand extends Command implements DebugCommand {
    public ReloadCommand(Command parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public List<Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        LangUtil.send("commands.reload-config", sender);
        ConfigUtil.loadConfig(getMain().getLogger(), getMain());
        LangUtil.send("commands.complete-msg", sender);
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
