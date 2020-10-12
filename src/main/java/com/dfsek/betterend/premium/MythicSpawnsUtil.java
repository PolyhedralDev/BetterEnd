package com.dfsek.betterend.premium;

import com.dfsek.betterend.BetterEnd;
import com.dfsek.betterend.config.ConfigUtil;
import com.dfsek.betterend.config.LangUtil;
import com.dfsek.betterend.world.EndBiomeGrid;
import com.dfsek.betterend.world.EndChunkGenerator;
import io.lumine.xikage.mythicmobs.MythicMobs;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.stream.IntStream;

public class MythicSpawnsUtil {
    private static final BetterEnd main = BetterEnd.getInstance();
    private static final File configFile = new File(main.getDataFolder() + File.separator + "mythicSpawns.yml");
    private static final YamlConfiguration config = new YamlConfiguration();
    private static final Random random = new Random();

    private MythicSpawnsUtil() {
    }

    @SuppressWarnings("unchecked")
    public static void startSpawnRoutine() {

        if(BetterEnd.isPremium()) {
            LangUtil.log("mythicmobs.enable", Level.INFO);
            try {
                config.load(configFile);
            } catch(IOException e) {
                LangUtil.log("mythicmobs.unable-to-find", Level.WARNING);
                return;
            } catch(InvalidConfigurationException e) {
                e.printStackTrace();
                return;
            }

            int spawnTime = config.getInt("spawn-frequency");

            main.getServer().getScheduler().scheduleSyncRepeatingTask(BetterEnd.getInstance(), () -> {
                try {
                    int maxMobs = config.getInt("mythicmob-cap") * (config.getBoolean("cap-is-per-player") ? main.getServer().getOnlinePlayers().size() : 1);
                    int numMobs = MythicMobs.inst().getMobManager().getActiveMobs().size();
                    if(maxMobs > numMobs) {
                        for(Player p : main.getServer().getOnlinePlayers()) {
                            if(p.getWorld().getGenerator() instanceof EndChunkGenerator) {
                                if(! (Math.abs(p.getLocation().getChunk().getX()) > 20 || Math.abs(p.getLocation().getChunk().getZ()) > 20))
                                    continue;
                                if(ConfigUtil.debug)
                                    main.getLogger().info("Starting MythicMobs spawns for " + p.getName());

                                List<Map<?, ?>> mobs = config.getMapList("mobs");

                                if(ConfigUtil.debug)
                                    main.getLogger().info("Spawning max of " + maxMobs + ", " + numMobs + " already exist(s).");
                                IntStream.Builder mobIDs = IntStream.builder();
                                IntStream.Builder weights = IntStream.builder();
                                for(int i = 0; i < mobs.size(); i++) {
                                    mobIDs.add(i);
                                    weights.add(((Map<String, Integer>) mobs.get(i)).get("weight"));
                                }
                                Map<?, ?> mob = mobs.get(chooseOnWeight(mobIDs.build().toArray(), weights.build().toArray()));
                                Location attemptLoc = p.getLocation()
                                        .add(new Vector(random.nextInt(((Map<String, Integer>) mob).get("maxDistance") - ((Map<String, Integer>) mob).get("minDistance") + 1) + ((Map<String, Integer>) mob).get("minDistance"), 0, 0)
                                                .rotateAroundY(random.nextInt(360)));
                                for(int i = 0; i < new Random().nextInt(((Map<String, Integer>) mob).get("maxGroupSize") - ((Map<String, Integer>) mob).get("minGroupSize") + 1)
                                        + ((Map<String, Integer>) mob).get("minGroupSize"); i++) {
                                    int y = 0;
                                    switch((String) mob.get("spawn")) {
                                        case "GROUND":
                                            attemptLoc.add((double) random.nextInt(7) - 3, 0, (double) random.nextInt(7) - 3);
                                            for(y = p.getWorld().getMaxHeight()
                                                    - 1; p.getWorld().getBlockAt(attemptLoc.getBlockX(), y, attemptLoc.getBlockZ()).getType() != Material.GRASS_BLOCK
                                                        && p.getWorld().getBlockAt(attemptLoc.getBlockX(), y, attemptLoc.getBlockZ()).getType() != Material.END_STONE
                                                        && p.getWorld().getBlockAt(attemptLoc.getBlockX(), y, attemptLoc.getBlockZ()).getType() != Material.DIRT
                                                        && p.getWorld().getBlockAt(attemptLoc.getBlockX(), y, attemptLoc.getBlockZ()).getType() != Material.STONE
                                                        && p.getWorld().getBlockAt(attemptLoc.getBlockX(), y, attemptLoc.getBlockZ()).getType() != Material.PODZOL
                                                        && p.getWorld().getBlockAt(attemptLoc.getBlockX(), y, attemptLoc.getBlockZ()).getType() != Material.COARSE_DIRT
                                                        && p.getWorld().getBlockAt(attemptLoc.getBlockX(), y, attemptLoc.getBlockZ()).getType() != Material.GRAVEL
                                                        && p.getWorld().getBlockAt(attemptLoc.getBlockX(), y, attemptLoc.getBlockZ()).getType() != Material.STONE_SLAB && y > 0; y--)
                                                ;

                                            break;
                                        case "AIR":
                                            y = p.getWorld().getMaxHeight() - 96 - random.nextInt(64);
                                            break;
                                        default:
                                            LangUtil.log("custom-structures.invalid-spawn", Level.WARNING, (String) mob.get("spawn"));
                                            break;
                                    }
                                    if(y < 1) continue;
                                    attemptLoc.setY(y);
                                    if(((List<?>) mob.get("biomes")).contains(EndBiomeGrid.fromWorld(attemptLoc.getWorld()).getBiome(attemptLoc).toString())
                                            && attemptLoc.clone().add(0, 1, 0).getBlock().isPassable() && attemptLoc.clone().add(0, 2, 0).getBlock().isPassable()
                                            && attemptLoc.clone().add(0, 1, 0).getBlock().getLightLevel() < ((Map<String, Integer>) mob).get("maxLight")) {
                                        MythicMobs.inst().getMobManager().spawnMob((String) mob.get("name"), attemptLoc.add(0, 1, 0));
                                        if(ConfigUtil.debug)
                                            main.getLogger().info("Spawning mob \"" + mob.get("name") + "\" at " + attemptLoc);
                                    }
                                }

                            }
                        }
                    }
                } catch(NoClassDefFoundError e) {
                    LangUtil.log("mythicmobs.fail-to-spawn", Level.WARNING);
                }
            }, 20L * spawnTime, 20L * spawnTime);
        }
    }

    public static int chooseOnWeight(int[] items, int[] weights) {
        double completeWeight = 0.0;
        for(int weight : weights)
            completeWeight += weight;
        double r = Math.random() * completeWeight;
        double countWeight = 0.0;
        for(int i = 0; i < items.length; i++) {
            countWeight += weights[i];
            if(countWeight >= r) return items[i];
        }
        return - 1;
    }
}