package com.dfsek.betterend.command;

import com.dfsek.betterend.BetterEnd;
import com.dfsek.betterend.util.Util;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.command.WorldCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BiomeTeleportCommand extends WorldCommand {
    private static List<String> BIOMES = Arrays.asList("AETHER", "END", "SHATTERED_END", "AETHER_HIGHLANDS", "SHATTERED_FOREST", "VOID", "STARFIELD");

    static {
        if(BetterEnd.isPremium()) {
            BIOMES = Arrays.asList("AETHER", "END", "SHATTERED_END", "AETHER_HIGHLANDS", "SHATTERED_FOREST", "AETHER_FOREST", "AETHER_HIGHLANDS_FOREST", "VOID",
                    "STARFIELD");
        }
    }
    public BiomeTeleportCommand(org.polydev.gaea.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String s, @NotNull String[] args, World world) {
        return Util.tpBiome(sender, args);
    }

    @Override
    public String getName() {
        return "teleport";
    }

    @Override
    public List<org.polydev.gaea.command.Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 1;
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        List<String> argList = new ArrayList<>();
        if(args.length == 1) argList.addAll(BIOMES);
        return argList.stream().filter(a -> a.toUpperCase().startsWith(args[args.length - 1].toUpperCase())).collect(Collectors.toList());
    }
}
