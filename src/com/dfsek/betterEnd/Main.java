package com.dfsek.betterEnd;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends JavaPlugin {	
	FileConfiguration config = this.getConfig();

	private static Main instance;
	@Override
	public void onEnable() {	
		config.addDefault("outer-islands.noise", 96);
		config.addDefault("outer-islands.biome-size", 512);
		config.addDefault("trees.min-per-chunk", 3);
		config.addDefault("trees.max-per-chunk", 6);
		config.addDefault("trees.obsidian-pillars.max-height", 14);
		config.addDefault("trees.obsidian-pillars.min-height", 5);
		config.options().copyDefaults(true);
		instance = this;

		saveConfig();
		dumpSchemFile("wood_house_s", 4);
		dumpSchemFile("stronghold", 1);
		dumpSchemFile("cobble_house_s", 5);
	}
	@Override
	public void onDisable() {
	}
	public static Main getInstance() {
		return instance;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1 && args[0].equalsIgnoreCase("biome")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command is for players only!");
				return true;
			}
			Player p = (Player) sender;
			if (sender.hasPermission("betterend.checkbiome")) {
				sender.sendMessage("[BetterEnd] You are standing in \"" + getBiome(p.getLocation().getBlockX(), p.getLocation().getBlockZ(), p.getLocation().getWorld().getSeed()) + "\"");
				return true;
			} else {
				sender.sendMessage(ChatColor.RED + "You do not have permission for this command.");
				return true;
			}
		} else if (args.length == 2 && args[0].equalsIgnoreCase("tpbiome")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command is for players only!");
				return true;
			}
			Player p = (Player) sender;
			if (sender.hasPermission("betterend.gotobiome")) {
				if(args[1].equalsIgnoreCase("END") || args[1].equalsIgnoreCase("SHATTERED_END") || args[1].equalsIgnoreCase("OBSIDIAN_FOREST") || args[1].equalsIgnoreCase("AETHER")) {
					sender.sendMessage("[BetterEnd] Locating biome \"" + args[1] + "\"");
					boolean foundBiome = false;
					int tries = 0;
					Location candidate = p.getLocation();
					while(foundBiome == false && tries < 2500) {
						Location candidateN = candidate.add(tries,0,0);
						if(getBiome(candidateN.getBlockX(), candidateN.getBlockZ(), p.getWorld().getSeed()).equalsIgnoreCase(args[1]) && Math.sqrt(Math.pow(candidateN.getBlockX(), 2) + Math.pow(candidateN.getBlockZ(), 2)) > 1000) {
							sender.sendMessage("[BetterEnd] Teleporting...");
							p.teleport(candidateN);
							foundBiome = true;
							return true;
						}
						candidateN = candidate.add(-tries,0,0);
						if(getBiome(candidateN.getBlockX(), candidateN.getBlockZ(), p.getWorld().getSeed()).equalsIgnoreCase(args[1]) && Math.sqrt(Math.pow(candidateN.getBlockX(), 2) + Math.pow(candidateN.getBlockZ(), 2)) > 1000) {
							sender.sendMessage("[BetterEnd] Teleporting...");
							p.teleport(candidateN);
							foundBiome = true;
							return true;
						}
						candidateN = candidate.add(0,0,tries);
						if(getBiome(candidateN.getBlockX(), candidateN.getBlockZ(), p.getWorld().getSeed()).equalsIgnoreCase(args[1]) && Math.sqrt(Math.pow(candidateN.getBlockX(), 2) + Math.pow(candidateN.getBlockZ(), 2)) > 1000) {
							sender.sendMessage("[BetterEnd] Teleporting...");
							p.teleport(candidateN);
							foundBiome = true;
							return true;
						}
						candidateN = candidate.add(0,0,-tries);
						if(getBiome(candidateN.getBlockX(), candidateN.getBlockZ(), p.getWorld().getSeed()).equalsIgnoreCase(args[1]) && Math.sqrt(Math.pow(candidateN.getBlockX(), 2) + Math.pow(candidateN.getBlockZ(), 2)) > 1000) {
							sender.sendMessage("[BetterEnd] Teleporting...");
							p.teleport(candidateN);
							foundBiome = true;
							return true;
						}
						tries++;
					}
					sender.sendMessage("[BetterEnd] Unable to locate biome.");
					return true;
				} else return false;
			} else {
				sender.sendMessage(ChatColor.RED + "You do not have permission for this command.");
				return true;
			}
		}
		return false;
	} 
	private static final List<String> COMMANDS = Arrays.asList("biome", "tpbiome", "version");
	//private static final List<String> BIOMES = Arrays.asList("AETHER", "END", "SHATTERED_END", "OBSIDIAN_FOREST");
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		 return (args.length > 0) ? StringUtil.copyPartialMatches(args[0], COMMANDS, new ArrayList<>()) : null;
	}
	public static String getBiome(int X, int Z, long l) {
		SimplexOctaveGenerator biomeGenerator = new SimplexOctaveGenerator(l, 4);
		Main main = Main.getInstance();
		double d = biomeGenerator.noise((double) (X)/main.getConfig().getInt("outer-islands.biome-size"), (double) (Z)/main.getConfig().getInt("outer-islands.biome-size"), 0.5D, 0.5D);
		if (d < -0.5) return "SHATTERED_END";//green
		else if (d < 0) return "END";//red
		else if (d < 0.5) return "OBSIDIAN_FOREST";//blue
		else return "AETHER";//orange
	}

	public static double getBiomeNoise(int X, int Z, long l) {
		Main main = Main.getInstance();
		SimplexOctaveGenerator biomeGenerator = new SimplexOctaveGenerator(l, 4);
		return biomeGenerator.noise((double) (X)/main.getConfig().getInt("outer-islands.biome-size"), (double) (Z)/main.getConfig().getInt("outer-islands.biome-size"), 0.5D, 0.5D);
	}
	@Override
	public CustomChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		return new CustomChunkGenerator();
	}
	private void dumpSchemFile(String name, int perms) {
		new File(this.getDataFolder() + "/scm/").mkdir();
		new File(this.getDataFolder() + "/scm/" + name + "/").mkdir();
		System.out.println("[BetterEnd] Generating schematic files...");
		for(int i = 0; i < perms; i++) {
			System.out.println("[BetterEnd] dumping internal schematic " + "/scm/" + name + "/" + name + "_" + i + ".schem");
			File file = new File(this.getDataFolder() + "/scm/" + name + "/",  name + "_" + i + ".schem");
			try {
				// create the file
				if (file.createNewFile()) {
					copyInputStreamToFile(getResource("scm/" + name + "/" + name + "_" + i + ".schem"), file);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private static void copyInputStreamToFile(InputStream inputStream, File file)
			throws IOException {
		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			int read;
			byte[] bytes = new byte[1024];
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
		}

	}
}
