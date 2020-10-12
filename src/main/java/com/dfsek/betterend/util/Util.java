package com.dfsek.betterend.util;

import com.dfsek.betterend.BetterEnd;
import com.dfsek.betterend.UpdateChecker;
import com.dfsek.betterend.UpdateChecker.UpdateReason;
import com.dfsek.betterend.config.ConfigUtil;
import com.dfsek.betterend.config.LangUtil;
import com.dfsek.betterend.world.EndBiome;
import com.dfsek.betterend.world.EndBiomeGrid;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {
    private static final BetterEnd main = BetterEnd.getInstance();
    private static final Logger logger = main.getLogger();

    private Util() {
    }


    public static boolean tpBiome(Player p, String[] args) {
        if(args[0].equalsIgnoreCase("END") || args[0].equalsIgnoreCase("SHATTERED_END") || args[0].equalsIgnoreCase("VOID") || args[0].equalsIgnoreCase("STARFIELD")
                || args[0].equalsIgnoreCase("SHATTERED_FOREST") || args[0].equalsIgnoreCase("AETHER") || args[0].equalsIgnoreCase("AETHER_HIGHLANDS")
                || (BetterEnd.isPremium() && args[0].equalsIgnoreCase("AETHER_HIGHLANDS_FOREST")) || (BetterEnd.isPremium() && args[0].equalsIgnoreCase("AETHER_FOREST"))) {
            LangUtil.send("commands.locating-biome", p, args[0]);
            int tries = 0;
            Location candidate = p.getLocation();
            while(tries < 400000) {
                Location candidateN = candidate.add(tries, 0, 0);
                if(EndBiomeGrid.fromWorld(p.getWorld()).getBiome(candidateN).equals(EndBiome.valueOf(args[0].toUpperCase()))
                        && Math.sqrt(Math.pow(candidateN.getBlockX(), 2) + Math.pow(candidateN.getBlockZ(), 2)) > 1000) {
                    LangUtil.send("commands.tp", p);
                    p.teleport(candidateN);
                    return true;
                }
                candidateN = candidate.add(- tries, 0, 0);
                if(EndBiomeGrid.fromWorld(p.getWorld()).getBiome(candidateN).equals(EndBiome.valueOf(args[0].toUpperCase()))
                        && Math.sqrt(Math.pow(candidateN.getBlockX(), 2) + Math.pow(candidateN.getBlockZ(), 2)) > 1000) {
                    LangUtil.send("commands.tp", p);
                    p.teleport(candidateN);
                    return true;
                }
                candidateN = candidate.add(0, 0, tries);
                if(EndBiomeGrid.fromWorld(p.getWorld()).getBiome(candidateN).equals(EndBiome.valueOf(args[0].toUpperCase()))
                        && Math.sqrt(Math.pow(candidateN.getBlockX(), 2) + Math.pow(candidateN.getBlockZ(), 2)) > 1000) {
                    LangUtil.send("commands.tp", p);
                    p.teleport(candidateN);
                    return true;
                }
                candidateN = candidate.add(0, 0, - tries);
                if(EndBiomeGrid.fromWorld(p.getWorld()).getBiome(candidateN).equals(EndBiome.valueOf(args[0].toUpperCase()))
                        && Math.sqrt(Math.pow(candidateN.getBlockX(), 2) + Math.pow(candidateN.getBlockZ(), 2)) > 1000) {
                    LangUtil.send("commands.tp", p);
                    p.teleport(candidateN);
                    return true;
                }
                tries++;
            }
            LangUtil.send("commands.unable-to-locate", p);
            return true;
        } else return false;
    }

    public static void checkUpdates() {
        BetterEnd instance = BetterEnd.getInstance();
        UpdateChecker.init(instance, 79389).requestUpdateCheck().whenComplete((result, exception) -> {
            if(result.requiresUpdate()) {
                LangUtil.log("update.new-version", Level.INFO, result.getNewestVersion());
                return;
            }

            UpdateReason reason = result.getReason();
            if(reason == UpdateReason.upToDate) {
                LangUtil.log("update.up-to-date", Level.INFO, result.getNewestVersion());
            } else if(reason == UpdateReason.UNRELEASED_VERSION) {
                LangUtil.log("update.more-recent", Level.INFO, result.getNewestVersion());
            } else {
                LangUtil.log("update.error", Level.INFO, result.getNewestVersion());
            }
        });
    }

    public static void copyResourcesToDirectory(JarFile fromJar, String jarDir, String destDir) throws IOException {
        for(Enumeration<JarEntry> entries = fromJar.entries(); entries.hasMoreElements(); ) {
            JarEntry entry = entries.nextElement();
            if(entry.getName().startsWith(jarDir + "/") && ! entry.isDirectory()) {
                File dest = new File(destDir + File.separator + entry.getName().substring(jarDir.length() + 1));
                if(ConfigUtil.debug) BetterEnd.getInstance().getLogger().info("Output: " + dest.toString());
                if(dest.exists()) continue;
                File parent = dest.getParentFile();
                if(parent != null) {
                    parent.mkdirs();
                }
                if(ConfigUtil.debug)
                    BetterEnd.getInstance().getLogger().info("Output does not already exist. Creating... ");
                try(FileOutputStream out = new FileOutputStream(dest); InputStream in = fromJar.getInputStream(entry)) {
                    byte[] buffer = new byte[8 * 1024];

                    int s = 0;
                    while((s = in.read(buffer)) > 0) {
                        out.write(buffer, 0, s);
                    }
                } catch(IOException e) {
                    throw new IOException("Could not copy asset from jar file", e);
                }
            }
        }
    }

    public static void logForEach(List<String> msgs, Level lvl) {
        for(String msg : msgs) {
            logger.log(lvl, ChatColor.translateAlternateColorCodes('&', msg));
        }
    }

    public static double getOffset(Random random, double amount) {
        double offset = 0;
        if(random.nextBoolean()) offset = random.nextBoolean() ? - amount : amount;
        return offset;
    }
}
