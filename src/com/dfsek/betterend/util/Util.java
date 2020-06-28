package com.dfsek.betterend.util;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.dfsek.betterend.Main;
import com.dfsek.betterend.UpdateChecker;
import com.dfsek.betterend.UpdateChecker.UpdateReason;
import com.dfsek.betterend.world.Biome;

public class Util {
	public static Object chooseOnWeight(Object[] items, int[] weights) {
		double completeWeight = 0.0;
		for (int weight : weights)
			completeWeight += weight;
		double r = Math.random() * completeWeight;
		double countWeight = 0.0;
		for (int i = 0; i < items.length; i++) {
			countWeight += weights[i];
			if (countWeight >= r)
				return items[i];
		}
		return null;
	}
	public static boolean tpBiome(Player p, String[] args) {
		if(args[1].equalsIgnoreCase("END") || args[1].equalsIgnoreCase("SHATTERED_END") || args[1].equalsIgnoreCase("VOID") || args[1].equalsIgnoreCase("STARFIELD") || args[1].equalsIgnoreCase("SHATTERED_FOREST") || args[1].equalsIgnoreCase("VOID") || args[1].equalsIgnoreCase("AETHER") || args[1].equalsIgnoreCase("AETHER_HIGHLANDS") || (Main.isPremium() && args[1].equalsIgnoreCase("AETHER_HIGHLANDS_FOREST")) || (Main.isPremium() && args[1].equalsIgnoreCase("AETHER_FOREST"))) {
			p.sendMessage(ChatColor.DARK_AQUA + "[BetterEnd]" + ChatColor.AQUA + " Locating biome \"" + ChatColor.DARK_AQUA + args[1] + ChatColor.AQUA +  "\"");
			int tries = 0;
			Location candidate = p.getLocation();
			while(tries < 10000) {
				Location candidateN = candidate.add(tries,0,0);
				if(Biome.fromLocation(candidateN).equals(Biome.fromString(args[1])) && Math.sqrt(Math.pow(candidateN.getBlockX(), 2) + Math.pow(candidateN.getBlockZ(), 2)) > 1000) {
					p.sendMessage(ChatColor.DARK_AQUA + "[BetterEnd] " + ChatColor.AQUA + "Teleporting...");
					p.teleport(candidateN);
					return true;
				}
				candidateN = candidate.add(-tries,0,0);
				if(Biome.fromLocation(candidateN).equals(Biome.fromString(args[1])) && Math.sqrt(Math.pow(candidateN.getBlockX(), 2) + Math.pow(candidateN.getBlockZ(), 2)) > 1000) {
					p.sendMessage(ChatColor.DARK_AQUA + "[BetterEnd] " + ChatColor.AQUA + "Teleporting...");
					p.teleport(candidateN);
					return true;
				}
				candidateN = candidate.add(0,0,tries);
				if(Biome.fromLocation(candidateN).equals(Biome.fromString(args[1])) && Math.sqrt(Math.pow(candidateN.getBlockX(), 2) + Math.pow(candidateN.getBlockZ(), 2)) > 1000) {
					p.sendMessage(ChatColor.DARK_AQUA + "[BetterEnd] " + ChatColor.AQUA + "Teleporting...");
					p.teleport(candidateN);
					return true;
				}
				candidateN = candidate.add(0,0,-tries);
				if(Biome.fromLocation(candidateN).equals(Biome.fromString(args[1])) && Math.sqrt(Math.pow(candidateN.getBlockX(), 2) + Math.pow(candidateN.getBlockZ(), 2)) > 1000) {
					p.sendMessage(ChatColor.DARK_AQUA + "[BetterEnd] " + ChatColor.AQUA + "Teleporting...");
					p.teleport(candidateN);
					return true;
				}
				tries++;
			}
			p.sendMessage("[BetterEnd] Unable to locate biome.");
			return true;
		} else return false;
	}
	public static void checkUpdates() {
		Main instance = Main.getInstance();
		UpdateChecker.init(instance, 79389).requestUpdateCheck().whenComplete((result, exception) -> {
			if (result.requiresUpdate()) {
				instance.getLogger().info(String.format("A new version of BetterEnd is available: %s ", result.getNewestVersion()));
				return;
			}

			UpdateReason reason = result.getReason();
			if (reason == UpdateReason.UP_TO_DATE) {
				instance.getLogger().info(String.format("Your version of BetterEnd (%s) is up to date!", result.getNewestVersion()));
			} else if (reason == UpdateReason.UNRELEASED_VERSION) {
				instance.getLogger().info(String.format("Your version of BetterEnd (%s) is more recent than the one publicly available.", result.getNewestVersion()));
			} else {
				instance.getLogger().warning("An error occurred while checking for an update. Reason: " + reason);//Occurred
			}
		});
	}
}
