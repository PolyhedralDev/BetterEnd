package com.dfsek.betterend.command;

import com.dfsek.betterend.config.LangUtil;
import com.dfsek.betterend.world.EndBiomeGrid;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.command.WorldCommand;

import java.util.Collections;
import java.util.List;

public class BiomeCommand extends WorldCommand {
    public BiomeCommand(org.polydev.gaea.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings, World world) {
        LangUtil.send("commands.biome", sender, EndBiomeGrid.fromWorld(world).getBiome(sender.getLocation()).toString());
        return true;
    }

    @Override
    public String getName() {
        return "biome";
    }

    @Override
    public List<org.polydev.gaea.command.Command> getSubCommands() {
        return Collections.singletonList(new BiomeTeleportCommand(this));
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
