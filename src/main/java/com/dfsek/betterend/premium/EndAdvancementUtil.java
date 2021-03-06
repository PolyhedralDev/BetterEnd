package com.dfsek.betterend.premium;

import com.dfsek.betterend.BetterEnd;
import com.dfsek.betterend.util.Util;
import com.dfsek.betterend.world.EndBiomeGrid;
import com.dfsek.betterend.world.EndChunkGenerator;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.jar.JarFile;

public class EndAdvancementUtil {
    private static final BetterEnd main = BetterEnd.getInstance();
    public static boolean noPackWarn = false;

    private EndAdvancementUtil() {
    }

    public static void enable(Plugin plugin) {
        File file = new File(Bukkit.getWorlds().get(0).getWorldFolder(), "datapacks" + File.separator + "bukkit");

        try(JarFile jar = new JarFile(new File(BetterEnd.class.getProtectionDomain().getCodeSource().getLocation().toURI()))) {
            Util.copyResourcesToDirectory(jar, "datapacks/bukkit", file.toString());
        } catch(IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for(Player p : plugin.getServer().getOnlinePlayers()) {
                double totalChunkDistance2D = Math.sqrt(Math.pow(p.getLocation().getChunk().getX(), 2) + Math.pow(p.getLocation().getChunk().getZ(), 2));
                if(p.getWorld().getGenerator() instanceof EndChunkGenerator && (totalChunkDistance2D > 50)) {
                    try {
                        switch(EndBiomeGrid.fromWorld(p.getWorld()).getBiome(p.getLocation())) {
                            case AETHER:
                                grantAdvancement("visit_aether", p);
                                break;
                            case AETHER_FOREST:
                                grantAdvancement("visit_aether_forest", p);
                                break;
                            case AETHER_HIGHLANDS:
                                grantAdvancement("visit_aether_highlands", p);
                                break;
                            case AETHER_HIGHLANDS_FOREST:
                                grantAdvancement("visit_aether_highlands_forest", p);
                                break;
                            case VOID:
                                grantAdvancement("visit_void", p);
                                break;
                            case STARFIELD:
                                grantAdvancement("visit_starfield", p);
                                break;
                            case SHATTERED_END:
                                grantAdvancement("visit_shattered_end", p);
                                break;
                            case SHATTERED_FOREST:
                                grantAdvancement("visit_shattered_forest", p);
                                break;
                            case END:
                                grantAdvancement("visit_end", p);
                                break;
                            default:
                        }
                        if(p.getLocation().getY() < - 64) grantAdvancement("into_void", p);
                        else if(p.getLocation().getY() > 5000) grantAdvancement("dizzying_heights", p);
                        if(hasAdvancement("visit_end", p) && hasAdvancement("visit_shattered_forest", p) && hasAdvancement("visit_shattered_end", p)
                                && hasAdvancement("visit_starfield", p) && hasAdvancement("visit_void", p) && hasAdvancement("visit_aether_highlands_forest", p)
                                && hasAdvancement("visit_aether_highlands", p) && hasAdvancement("visit_aether_forest", p) && hasAdvancement("visit_aether", p)) {
                            grantAdvancement("explore", p);
                        }
                    } catch(IllegalArgumentException e) {
                        if(! noPackWarn) {
                            noPackWarn = true;
                            main.getLogger().warning("BetterEnd attempted to award an advancement, but it was not found!");
                            main.getLogger().warning("If this is your first time starting your server with BetterEnd, restart to enable advancements.");
                            main.getLogger().warning("If this is a consistent issue, please seek support on Discord, or report it on GitHub.");
                        }
                    }
                }
            }
        }, 20L, 20L);
    }

    public static void grantAdvancement(String adv, Player player) {
        NamespacedKey nsk = new NamespacedKey(main, "outer_end/" + adv);
        org.bukkit.advancement.Advancement a = main.getServer().getAdvancement(nsk);
        if(a != null) {
            AdvancementProgress avp = player.getAdvancementProgress(a);
            if(! avp.isDone()) {
                main.getServer().dispatchCommand(main.getServer().getConsoleSender(), "advancement grant " + player.getName() + " only betterend:outer_end/" + adv);
            }
        } else {
            throw new IllegalArgumentException("Invalid advancement name \"" + adv + "\"");
        }
    }

    public static boolean hasAdvancement(String adv, Player player) {
        NamespacedKey nsk = new NamespacedKey(main, "outer_end/" + adv);
        org.bukkit.advancement.Advancement a = main.getServer().getAdvancement(nsk);
        if(a != null) {
            AdvancementProgress avp = player.getAdvancementProgress(a);
            return avp.isDone();
        } else {
            throw new IllegalArgumentException("Invalid advancement name \"" + adv + "\"");
        }
    }

}
