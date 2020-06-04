package com.dfsek.betterEnd;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import com.dfsek.betterEnd.UpdateChecker.UpdateReason;

import io.lumine.xikage.mythicmobs.MythicMobs;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Main extends JavaPlugin implements Listener {	
	public FileConfiguration config = this.getConfig();

	private static Main instance;
	@Override
	public void onEnable() {	
		instance = this;
		Logger logger = this.getLogger();
		new MetricsLite(this, 7709);

		this.getServer().getPluginManager().registerEvents(this, this);
		config.addDefault("all-aether", false);
		config.addDefault("update-checker.enable", true);
		config.addDefault("update-checker.frequency", 3600);
		config.addDefault("outer-islands.structures.chance-per-chunk", 5);
		config.addDefault("outer-islands.structures.shulker-nest.shulker-spawn-attempts", 8);
		config.addDefault("outer-islands.ruins.chance-per-chunk", 30);
		config.addDefault("outer-islands.noise", 56);
		config.addDefault("outer-islands.height-multiplier.top", 6);
		config.addDefault("outer-islands.height-multiplier.bottom", 52);
		config.addDefault("outer-islands.island-height", 64);
		config.addDefault("outer-islands.island-threshold", 30);
		config.addDefault("aether.clouds.enable-clouds", true);
		config.addDefault("aether.clouds.cloud-noise", 36);
		config.addDefault("aether.ores.enable-ores", true);
		config.addDefault("aether.ores.ore-chance", 20);
		config.addDefault("aether.cave-decoration", true);
		config.addDefault("outer-islands.cave-decoration", true);
		config.addDefault("aether.clouds.cloud-height", 128);
		config.addDefault("aether.animals.herd-chance-per-chunk", 15);
		config.addDefault("aether.animals.herd-min-size", 2);
		config.addDefault("aether.animals.herd-max-size", 5);
		config.addDefault("aether.tree-multiplier", 4);
		config.addDefault("aether.mythic-boss.enable", false);
		config.addDefault("aether.mythic-boss.gold-name", "SkeletonKing");
		config.addDefault("outer-islands.biome-size", 1024);
		config.addDefault("outer-islands.heat-noise", 512);
		config.addDefault("trees.min-per-chunk", 4);
		config.addDefault("trees.max-per-chunk", 7);
		config.addDefault("trees.obsidian-pillars.max-height", 14);
		config.addDefault("trees.obsidian-pillars.min-height", 7);
		config.addDefault("outer-islands.generate-end-cities", false);
		config.addDefault("prevent-enderman-block-pickup", true);
		config.addDefault("debug", false);
		config.options().copyDefaults(true);
		saveConfig();

		logger.info(" ");
		logger.info(" ");
		logger.info("|---------------------------------------------------------------------------------|");
		logger.info("BetterEnd would not have been possible without the support of the following people:");
		logger.info("Developers: dfsek and Baer");
		logger.info("Builders: GucciPoochie, sgtmushroom39, Daverono, and Merazmus");
		logger.info("|---------------------------------------------------------------------------------|");
		logger.info(" ");
		logger.info(" ");

		dumpSchemFile("wood_house_s", 4);
		dumpSchemFile("shulker_nest", 2);
		dumpSchemFile("stone_ruin", 18);
		dumpSchemFile("shattered_ruin", 16);
		dumpSchemFile("end_house", 3);
		dumpSchemFile("stronghold", 1);
		dumpSchemFile("gold_dungeon", 1);
		dumpSchemFile("cobble_house_s", 5);
		int sec = config.getInt("update-checker.frequency", 3600);
		if(config.getBoolean("update-checker.enable", true)) {
			getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				@Override
				public void run() {
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
			}, 20L * sec, 20L * sec);
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
				if(args[1].equalsIgnoreCase("END") || args[1].equalsIgnoreCase("SHATTERED_END") || args[1].equalsIgnoreCase("VOID") || args[1].equalsIgnoreCase("AETHER") || args[1].equalsIgnoreCase("AETHER_HIGHLANDS")) {
					sender.sendMessage("[BetterEnd] Locating biome \"" + args[1] + "\"");
					boolean foundBiome = false;
					int tries = 0;
					Location candidate = p.getLocation();
					while(foundBiome == false && tries < 10000) {
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
	private static final List<String> BIOMES = Arrays.asList("AETHER", "END", "SHATTERED_END", "OBSIDIAN_FOREST", "AETHER_HIGHLANDS");
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return (args.length == 1) ? StringUtil.copyPartialMatches(args[0], COMMANDS, new ArrayList<>()) : StringUtil.copyPartialMatches(args[0], BIOMES, new ArrayList<>());
	}
	public static String getBiome(int X, int Z, long l) {
		SimplexOctaveGenerator biomeGenerator = new SimplexOctaveGenerator(l, 4);
		Main main = Main.getInstance();
		boolean allAether = main.getConfig().getBoolean("all-aether", false);
		int heatNoise = main.getConfig().getInt("outer-islands.heat-noise");
		if(allAether) return "AETHER";
		double h = biomeGenerator.noise((double) (X)/heatNoise, (double) (Z)/heatNoise, 0.5D, 0.5D);
		double d = biomeGenerator.noise((double) (X)/main.getConfig().getInt("outer-islands.biome-size"), (double) (Z)/main.getConfig().getInt("outer-islands.biome-size"), 0.5D, 0.5D);
		if (d < -0.5) return "SHATTERED_END";//green
		else if (d < 0) return "END";//red
		else if (d < 0.5) return "VOID";//blue
		else if(h < -0.5) return "AETHER_HIGHLANDS";
		else return "AETHER";//orange
	}
	public static double getBiomeNoise(int X, int Z, long l) {
		Main main = Main.getInstance();
		SimplexOctaveGenerator biomeGenerator = new SimplexOctaveGenerator(l, 4);
		return biomeGenerator.noise((double) (X)/main.getConfig().getInt("outer-islands.biome-size"), (double) (Z)/main.getConfig().getInt("outer-islands.biome-size"), 0.5D, 0.5D);
	}
	@Override
	public EndChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		return new EndChunkGenerator();
	}
	private void dumpSchemFile(String name, int perms) {
		new File(this.getDataFolder() + "/scm/").mkdir();
		new File(this.getDataFolder() + "/scm/" + name + "/").mkdir();
		System.out.println("[BetterEnd] Dumping schematic files...");
		for(int i = 0; i < perms; i++) {
			if(config.getBoolean("debug")) System.out.println("[BetterEnd] dumping internal schematic " + "/scm/" + name + "/" + name + "_" + i + ".schem");
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
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityPickup(EntityChangeBlockEvent event) {
		if(event.getEntity() instanceof Enderman && config.getBoolean("prevent-enderman-block-pickup")) {
			event.setCancelled(true);
		}
	}
	@EventHandler (ignoreCancelled=true)
	public void onInventoryOpenEvent(InventoryOpenEvent event) {
		if(config.getBoolean("aether.mythic-boss.enable", false)) {
			//get the destination inventory
			InventoryHolder holder = event.getInventory().getHolder();
			Inventory inventory = event.getInventory();
			if (inventory.getHolder() instanceof Chest) {
				Location l = ((Chest) holder).getLocation();
				if(config.getBoolean("debug")) System.out.println("[BetterEnd] Player opened chest in " + l.getWorld() + " at " + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ());
				Chest chest = (Chest) l.getBlock().getState();
				NamespacedKey key = new NamespacedKey(this, "valkyrie-spawner");
				if(chest.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
					Location spawn;
					switch(chest.getPersistentDataContainer().get(key, PersistentDataType.INTEGER)) {
					case 0:
						spawn = chest.getLocation().subtract(9.5, 2, -0.5);
						break;
					case 1:
						spawn = chest.getLocation().add(0.5, -2, 10.5);
						break;
					case 2:
						spawn = chest.getLocation().add(10.5, -2, 0.5);
						break;
					case 3:
						spawn = chest.getLocation().subtract(-0.5, 2, 9.5);
						break;
					default:
						chest.getPersistentDataContainer().remove(key);
						chest.update();
						return;
					}
					if(config.getBoolean("debug")) System.out.println("[BetterEnd] Chest is a Mythic Boss Spawn Chest.");
					String boss = config.getString("aether.mythic-boss.gold-name", "SkeletonKing");
					try {
						MythicMobs.inst().getMobManager().spawnMob(boss, spawn);
					} catch(NoClassDefFoundError e) {
						this.getLogger().warning("Failed to spawn Mythic Boss. Is MythicMobs installed?");
					}
					//chest.getWorld().spawnEntity(spawn, EntityType.ZOMBIE);
											
				}
				chest.getPersistentDataContainer().remove(key);
				chest.update();
			}
		}
	}
}
