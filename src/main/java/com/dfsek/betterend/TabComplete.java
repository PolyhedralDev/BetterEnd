package com.dfsek.betterend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.dfsek.betterend.population.tree.EndTreeType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

public class TabComplete implements TabCompleter {
	private static final List<String> COMMANDS = Arrays.asList("biome", "tpbiome", "version", "reload", "tree");
	private static List<String> BIOMES = Arrays.asList("AETHER", "END", "SHATTERED_END", "AETHER_HIGHLANDS", "SHATTERED_FOREST", "VOID", "STARFIELD");
	static {
		if(BetterEnd.isPremium()) {
			BIOMES = Arrays.asList("AETHER", "END", "SHATTERED_END", "AETHER_HIGHLANDS", "SHATTERED_FOREST", "AETHER_FOREST", "AETHER_HIGHLANDS_FOREST", "VOID",
					"STARFIELD");
		}
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {

		List<String> argList = new ArrayList<>();
		if(args.length == 1) {
			argList.addAll(COMMANDS);
			return argList.stream().filter(a -> a.startsWith(args[0])).collect(Collectors.toList());
		}
		if(args.length >= 2) {
			switch(args[0]) {
				case "tpbiome":
					if(args.length == 2) argList.addAll(BIOMES);
					break;
				case "tree":
					if(args.length == 3) for(EndTreeType t : EndTreeType.values()) {
						argList.add(t.toString());
					}
					else if(args.length == 2) {
						argList.add("plant");
						argList.add("grow");
					}
					break;
				default:
			}
			return argList.stream().filter(a -> a.startsWith(args[1].toUpperCase())).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}
}
