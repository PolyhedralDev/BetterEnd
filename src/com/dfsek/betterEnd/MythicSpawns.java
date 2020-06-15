package com.dfsek.betterEnd;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import io.lumine.xikage.mythicmobs.MythicMobs;

public class MythicSpawns {
	static Main main = Main.getInstance();
	static File configFile = new File(main.getDataFolder() + File.separator + "mythicSpawns.yml");
	static YamlConfiguration config = new YamlConfiguration();
	static Random random = new Random();
	static boolean debug = main.getConfig().getBoolean("debug");
	public static void startSpawnRoutine() {

		if(Main.isPremium()) {
			main.getLogger().info("Starting MythicMobs integration");
			try {
				config.load(configFile);
			} catch (IOException e) {
				main.getLogger().warning("Unable to locate mythicSpawns.yml. Aborting MythicMobs random spawning.");
				return;
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
				return;
			}

			int spawnTime = config.getInt("spawn-frequency");

			main.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
				@Override
				public void run() {
					try {
						int maxMobs = config.getInt("mythicmob-cap") * (config.getBoolean("cap-is-per-player") ? main.getServer().getOnlinePlayers().size() : 1);
						int numMobs = MythicMobs.inst().getMobManager().getActiveMobs().size();
						if(maxMobs > numMobs) {
							for(Player p : main.getServer().getOnlinePlayers()) {
								if(p.getWorld().getGenerator() instanceof EndChunkGenerator) {
									if(!(Math.abs(p.getChunk().getX()) > 20 || Math.abs(p.getChunk().getZ()) > 20)) continue;
									if(debug) main.getLogger().info("Starting MythicMobs spawns for " + p.getName());

									List<Map<?, ?>> mobs = config.getMapList("mobs");

									if(debug) main.getLogger().info("Spawning max of " + maxMobs + ", " + numMobs + " already exist(s).");
									IntStream.Builder mobIDs = IntStream.builder();
									IntStream.Builder weights = IntStream.builder();
									for(int i = 0; i < mobs.size(); i++) {
										mobIDs.add(i);
										weights.add((int) mobs.get(i).get("weight")); 
									}
									Map<?, ?> mob = mobs.get(chooseOnWeight(mobIDs.build().toArray(), weights.build().toArray()));
									Location attemptLoc = p.getLocation().add(new Vector(random.nextInt((int) mob.get("maxDistance") - (int) mob.get("minDistance")+1) + (int) mob.get("minDistance"), 0, 0).rotateAroundY(random.nextInt(360)));
									for(int i = 0; i < new Random().nextInt((int)mob.get("maxGroupSize")-(int)mob.get("minGroupSize")+1) + (int) mob.get("minGroupSize"); i++) {
										int Y = 0;
										switch((String) mob.get("spawn")) {
										case "GROUND":
											attemptLoc.add(random.nextInt(7)-3, 0, random.nextInt(7)-3);
											for (Y = p.getWorld().getMaxHeight()-1; p.getWorld().getBlockAt(attemptLoc.getBlockX(),Y,attemptLoc.getBlockZ()).getType() != Material.GRASS_BLOCK && 
													p.getWorld().getBlockAt(attemptLoc.getBlockX(),Y,attemptLoc.getBlockZ()).getType() != Material.END_STONE && 
													p.getWorld().getBlockAt(attemptLoc.getBlockX(),Y,attemptLoc.getBlockZ()).getType() != Material.DIRT && 
													p.getWorld().getBlockAt(attemptLoc.getBlockX(),Y,attemptLoc.getBlockZ()).getType() != Material.STONE &&
													p.getWorld().getBlockAt(attemptLoc.getBlockX(),Y,attemptLoc.getBlockZ()).getType() != Material.PODZOL &&
													p.getWorld().getBlockAt(attemptLoc.getBlockX(),Y,attemptLoc.getBlockZ()).getType() != Material.COARSE_DIRT &&
													p.getWorld().getBlockAt(attemptLoc.getBlockX(),Y,attemptLoc.getBlockZ()).getType() != Material.GRAVEL &&
													p.getWorld().getBlockAt(attemptLoc.getBlockX(),Y,attemptLoc.getBlockZ()).getType() != Material.STONE &&
													p.getWorld().getBlockAt(attemptLoc.getBlockX(),Y,attemptLoc.getBlockZ()).getType() != Material.STONE_SLAB && Y>0; Y--);
											
											break;
										case "AIR":
											Y = p.getWorld().getMaxHeight()-96-random.nextInt(64);
											break;
										}
										if(Y < 1) continue;
										attemptLoc.setY(Y);
										if(((List<?>) mob.get("biomes")).contains(Main.getBiome(attemptLoc.getBlockX(), attemptLoc.getBlockZ(), p.getWorld().getSeed())) && attemptLoc.clone().add(0,1,0).getBlock().isPassable() && attemptLoc.clone().add(0,2,0).getBlock().isPassable() && attemptLoc.clone().add(0,1,0).getBlock().getLightLevel() < (int) mob.get("maxLight")) {
											MythicMobs.inst().getMobManager().spawnMob((String) mob.get("name"), attemptLoc.add(0,1,0));
											if(debug) main.getLogger().info("Spawning mob \"" + mob.get("name") + "\" at " + attemptLoc);
										}
									}

								}
							}
						}
					} catch(NoClassDefFoundError e) {
						main.getLogger().warning("Failed to spawn Mobs. Is MythicMobs installed?");
					}
				}
			}, 20L * spawnTime, 20L * spawnTime);

		}
	}
	public static int chooseOnWeight(int[] items, int[] weights) {
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
		return -1;
	}
}