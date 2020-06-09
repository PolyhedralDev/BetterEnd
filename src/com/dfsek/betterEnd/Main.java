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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import com.dfsek.betterEnd.UpdateChecker.UpdateReason;
import io.lumine.xikage.mythicmobs.MythicMobs;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Main extends JavaPlugin implements Listener {	
	public FileConfiguration config = this.getConfig();

	private static Main instance;
	@Override
	public void onEnable() {	
		instance = this;
		Logger logger = this.getLogger();
		
		Metrics metrics = new Metrics(this, 7709);
        metrics.addCustomChart(new Metrics.SimplePie("premium", () -> isPremium() ? "Yes" : "No"));
        
		this.getServer().getPluginManager().registerEvents(this, this);

		try {
			File configFile = new File(getDataFolder() + File.separator + "config.yml");
			if(!configFile.exists()) {
				configFile.getParentFile().mkdirs();
				FileOutputStream writer = new FileOutputStream(configFile);
				InputStream out = this.getResource("config.yml");
				byte[] linebuffer = new byte[4096];
				int lineLength = 0;
				while((lineLength = out.read(linebuffer)) > 0)
				{
					writer.write(linebuffer, 0, lineLength);
				}
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info(" ");
		logger.info(" ");
		logger.info("|---------------------------------------------------------------------------------|");
		logger.info("BetterEnd would not have been possible without the support of the following people:");
		logger.info("Developers: dfsek and Baer");
		logger.info("Builders: GucciPoochie, sgtmushroom39, Daverono, and Merazmus");
		logger.info("|---------------------------------------------------------------------------------|");
		logger.info(" ");
		logger.info(" ");

		int sec = config.getInt("update-checker.frequency", 3600);
		if(config.getBoolean("update-checker.enable", true)) {
			getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				@Override
				public void run() {
					checkUpdates();
				}
			}, 20L * sec, 20L * sec);
			
		}
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				checkUpdates();
			}
		}, 100);

	}
	public void checkUpdates() {
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
				sender.sendMessage(ChatColor.DARK_AQUA + "[BetterEnd] " + ChatColor.AQUA + "This command is for players only!");
				return true;
			}
			Player p = (Player) sender;
			if (sender.hasPermission("betterend.checkbiome")) {
				try {
				if(p.getWorld().getGenerator().getClass().equals(new EndChunkGenerator().getClass())) sender.sendMessage(ChatColor.DARK_AQUA + "[BetterEnd] " + ChatColor.AQUA + "You are standing in \"" + ChatColor.DARK_AQUA + getBiome(p.getLocation().getBlockX(), p.getLocation().getBlockZ(), p.getLocation().getWorld().getSeed()) + "\"");
				else sender.sendMessage(ChatColor.DARK_AQUA +  "[BetterEnd] " + ChatColor.RED + "This world is not a BetterEnd world!");
				} catch (NullPointerException e) {
					sender.sendMessage(ChatColor.DARK_AQUA +  "[BetterEnd] " + ChatColor.RED + "This world is not a BetterEnd world!");
				}
				return true;
			} else {
				sender.sendMessage(ChatColor.RED + "You do not have permission for this command.");
				return true;
			}
		} else if (args.length == 2 && args[0].equalsIgnoreCase("tpbiome")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.DARK_AQUA + "[BetterEnd] " + ChatColor.AQUA + "This command is for players only!");
				return true;
			}
			Player p = (Player) sender;
			if (p.hasPermission("betterend.gotobiome")) {
				try {
					if(p.getWorld().getGenerator().getClass().equals(new EndChunkGenerator().getClass())) return tpBiome(p, args);
					else sender.sendMessage(ChatColor.DARK_AQUA +  "[BetterEnd] " + ChatColor.RED + "This world is not a BetterEnd world!");
				} catch (NullPointerException e) {
					sender.sendMessage(ChatColor.DARK_AQUA +  "[BetterEnd] " + ChatColor.RED + "This world is not a BetterEnd world!");
				}
				return true;
			} else {
				sender.sendMessage(ChatColor.DARK_AQUA +  "[BetterEnd] " + ChatColor.RED + "You do not have permission for this command.");
				return true;
			}
		} else if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "You do not have permission for this command.");
		} else if (args.length == 1 && args[0].equalsIgnoreCase("version")) {
			sender.sendMessage(ChatColor.DARK_AQUA + "[BetterEnd]" + ChatColor.AQUA + " This server is running " + ChatColor.DARK_AQUA + "BetterEnd v" + this.getDescription().getVersion());
			return true;
		}
		return false;
	} 

	private static final List<String> COMMANDS = Arrays.asList("biome", "tpbiome", "version");
	private static final List<String> BIOMES = Arrays.asList("AETHER", "END", "SHATTERED_END", "OBSIDIAN_FOREST", "AETHER_HIGHLANDS");
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> argList = new ArrayList<>();
		if (args.length == 1) {
			argList.addAll(COMMANDS);
			return argList.stream().filter(a -> a.startsWith(args[0])).collect(Collectors.toList());
		}
		if (args.length == 2) {
			switch(args[0]) {
			case "tpbiome":
				argList.addAll(BIOMES);
				return argList.stream().filter(a -> a.startsWith(args[1].toUpperCase())).collect(Collectors.toList());
			}

		}

		return null; // returns an empty list
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
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityPickup(EntityChangeBlockEvent event) {
		if(event.getEntity() instanceof Enderman && config.getBoolean("prevent-enderman-block-pickup", true)) {
			if(event.getEntity().getWorld().getGenerator().getClass().equals(new EndChunkGenerator().getClass()) && getBiomeNoise(event.getEntity().getLocation().getBlockX(), event.getEntity().getLocation().getBlockZ(), event.getEntity().getWorld().getSeed()) > 0.5) {
				event.setCancelled(true);
			}
		}
	}
	public static boolean isPremium() {
		return false;
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		if(!isPremium()) getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				if(e.getPlayer().isOp()) e.getPlayer().sendMessage(new String[] {ChatColor.AQUA + "You're running the free version of BetterEnd. To get the premium version and change/remove this message, follow the instructions here:" + ChatColor.UNDERLINE + "https://github.com/dfsek/BetterEnd-Public/wiki/Premium", ChatColor.GRAY + "The above message is only sent to Operators. The message below is sent to all players:"});
				e.getPlayer().sendMessage(new String[] {ChatColor.AQUA + "This server runs " + ChatColor.DARK_AQUA + "BetterEnd!", ChatColor.AQUA + "For more information, type /be, or visit the Wiki: " + ChatColor.UNDERLINE + "https://github.com/dfsek/BetterEnd-Public/wiki"});
			}
		}, 80);
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
						spawn = chest.getLocation().subtract(-0.5, 2, 10.5);
						break;
					case 2:
						spawn = chest.getLocation().add(10.5, -2, 0.5);
						break;
					case 3:
						spawn = chest.getLocation().add(0.5, -2, 9.5);
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
	public boolean tpBiome(Player p, String[] args) {
		if(args[1].equalsIgnoreCase("END") || args[1].equalsIgnoreCase("SHATTERED_END") || args[1].equalsIgnoreCase("VOID") || args[1].equalsIgnoreCase("AETHER") || args[1].equalsIgnoreCase("AETHER_HIGHLANDS")) {
			p.sendMessage(ChatColor.DARK_AQUA + "[BetterEnd]" + ChatColor.AQUA + " Locating biome \"" + ChatColor.DARK_AQUA + args[1] + ChatColor.AQUA +  "\"");
			boolean foundBiome = false;
			int tries = 0;
			Location candidate = p.getLocation();
			while(foundBiome == false && tries < 10000) {
				Location candidateN = candidate.add(tries,0,0);
				if(getBiome(candidateN.getBlockX(), candidateN.getBlockZ(), p.getWorld().getSeed()).equalsIgnoreCase(args[1]) && Math.sqrt(Math.pow(candidateN.getBlockX(), 2) + Math.pow(candidateN.getBlockZ(), 2)) > 1000) {
					p.sendMessage(ChatColor.DARK_AQUA + "[BetterEnd] " + ChatColor.AQUA + "Teleporting...");
					p.teleport(candidateN);
					foundBiome = true;
					return true;
				}
				candidateN = candidate.add(-tries,0,0);
				if(getBiome(candidateN.getBlockX(), candidateN.getBlockZ(), p.getWorld().getSeed()).equalsIgnoreCase(args[1]) && Math.sqrt(Math.pow(candidateN.getBlockX(), 2) + Math.pow(candidateN.getBlockZ(), 2)) > 1000) {
					p.sendMessage(ChatColor.DARK_AQUA + "[BetterEnd] " + ChatColor.AQUA + "Teleporting...");
					p.teleport(candidateN);
					foundBiome = true;
					return true;
				}
				candidateN = candidate.add(0,0,tries);
				if(getBiome(candidateN.getBlockX(), candidateN.getBlockZ(), p.getWorld().getSeed()).equalsIgnoreCase(args[1]) && Math.sqrt(Math.pow(candidateN.getBlockX(), 2) + Math.pow(candidateN.getBlockZ(), 2)) > 1000) {
					p.sendMessage(ChatColor.DARK_AQUA + "[BetterEnd] " + ChatColor.AQUA + "Teleporting...");
					p.teleport(candidateN);
					foundBiome = true;
					return true;
				}
				candidateN = candidate.add(0,0,-tries);
				if(getBiome(candidateN.getBlockX(), candidateN.getBlockZ(), p.getWorld().getSeed()).equalsIgnoreCase(args[1]) && Math.sqrt(Math.pow(candidateN.getBlockX(), 2) + Math.pow(candidateN.getBlockZ(), 2)) > 1000) {
					p.sendMessage(ChatColor.DARK_AQUA + "[BetterEnd] " + ChatColor.AQUA + "Teleporting...");
					p.teleport(candidateN);
					foundBiome = true;
					return true;
				}
				tries++;
			}
			p.sendMessage("[BetterEnd] Unable to locate biome.");
			return true;
		} else return false;
	}
}
