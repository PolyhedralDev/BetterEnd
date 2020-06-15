package com.dfsek.betterend;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.dfsek.betterend.advancement.Advancement;
import com.dfsek.betterend.advancement.AdvancementFactory;
import com.dfsek.betterend.advancement.Rewards;
import com.dfsek.betterend.advancement.shared.Dimension;
import com.dfsek.betterend.advancement.shared.ItemObject;
import com.dfsek.betterend.advancement.trigger.ChangedDimensionTrigger;
import net.md_5.bungee.api.chat.TextComponent;

public class EndAdvancements {
	private static Main main = Main.getInstance();
	public static void enable(Plugin plugin) {
		try {
			AdvancementFactory factory = new AdvancementFactory(plugin, true, false);

			//Create a root advancement which is also automatically unlocked (with a player head icon)
			Advancement root = new Advancement(new NamespacedKey(plugin, "outer_end/root"), new ItemObject().setItem(Material.PURPUR_BLOCK), new TextComponent("The Outer End"), new TextComponent("This looks... Different..."));
			root.addTrigger("end", new ChangedDimensionTrigger().setTo(Dimension.THE_END))
			.makeRoot("block/purpur_block", false)
			.setAnnounce(false)
			.setRewards(new Rewards().setExperience(10))
			.activate(true);


			Advancement aetherVisit = factory.getImpossible("outer_end/visit_aether", root, "A Hostile Paradise", "Enter the Aether", Material.OAK_LOG);
			Advancement aetherHighlandsVisit = factory.getImpossible("outer_end/visit_aether_highlands", aetherVisit, "Game Design", "Enter the Aether Highlands", Material.SPRUCE_LOG).setHidden(true);
			factory.getImpossible("outer_end/visit_aether_forest", aetherVisit, "Trees.", "Enter the Aether Forest", Material.OAK_SAPLING).setHidden(true);
			factory.getImpossible("outer_end/visit_aether_highlands_forest", aetherHighlandsVisit, "S p r o c e", "Enter the Aether Highlands Forest", Material.SPRUCE_SAPLING).setHidden(true);
			factory.getImpossible("outer_end/gold_dungeon", aetherVisit, "I've Got a Bad Feeling About This...", "Open a Gold Dungeon Chest.", Material.CHISELED_QUARTZ_BLOCK).setHidden(true);

			Advancement voidVisit = factory.getImpossible("outer_end/visit_void", root, "Don't Fall", "Enter the Void", Material.BLACK_CONCRETE);
			factory.getImpossible("outer_end/visit_starfield", voidVisit, "A Field of Stars", "Enter a Starfield", Material.GLOWSTONE).setHidden(true);
			factory.getImpossible("outer_end/into_void", voidVisit, "Embrace the Void", "Travel below Y level -64", Material.BLACK_CONCRETE).setHidden(true);
			factory.getImpossible("outer_end/dizzying_heights", voidVisit, "Dizzying Heights", "Travel above Y level 5000", Material.WHITE_CONCRETE).setHidden(true);

			factory.getImpossible("outer_end/visit_end", root, "Finally, Something Familiar", "Enter the End", Material.END_STONE);

			factory.getImpossible("outer_end/explore", root, "Ender Explorer", "Find all 9 Outer End biomes", Material.COMPASS).setHidden(true);

			Advancement shatteredVisit = factory.getImpossible("outer_end/visit_shattered_end", root, "What Happened Here?", "Enter the Shattered End", Material.OBSIDIAN);
			factory.getImpossible("outer_end/visit_shattered_forest", shatteredVisit, "Funky Trees", "Enter the Shattered Forest", Material.PURPLE_STAINED_GLASS).setHidden(true);
			Bukkit.reloadData();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				for(Player p : plugin.getServer().getOnlinePlayers()) {
					double totalChunkDistance2D = Math.sqrt(Math.pow(p.getChunk().getX(), 2)+Math.pow(p.getChunk().getZ(), 2));
					if(p.getWorld().getGenerator() instanceof EndChunkGenerator && (totalChunkDistance2D > 50)) {
						switch(Main.getBiome(p.getLocation().getBlockX(), p.getLocation().getBlockZ(), p.getWorld().getSeed())) {
						case "AETHER":
							grantAdvancement("visit_aether", p);
							break;
						case "AETHER_FOREST":
							grantAdvancement("visit_aether_forest", p);
							break;
						case "AETHER_HIGHLANDS":
							grantAdvancement("visit_aether_highlands", p);
							break;
						case "AETHER_HIGHLANDS_FOREST":
							grantAdvancement("visit_aether_highlands_forest", p);
							break;
						case "VOID":
							grantAdvancement("visit_void", p);
							break;
						case "STARFIELD":
							grantAdvancement("visit_starfield", p);
							break;
						case "SHATTERED_END":
							grantAdvancement("visit_shattered_end", p);
							break;
						case "SHATTERED_FOREST":
							grantAdvancement("visit_shattered_forest", p);
							break;
						case "END":
							grantAdvancement("visit_end", p);
							break;
						default:
						}
						if(p.getLocation().getY() < -64) grantAdvancement("into_void", p);
						else if(p.getLocation().getY() > 5000) grantAdvancement("dizzying_heights", p);
						if(hasAdvancement("visit_end", p) &&
								hasAdvancement("visit_shattered_forest", p) &&
								hasAdvancement("visit_shattered_end", p) &&
								hasAdvancement("visit_starfield", p) &&
								hasAdvancement("visit_void", p) &&
								hasAdvancement("visit_aether_highlands_forest", p) &&
								hasAdvancement("visit_aether_highlands", p) &&
								hasAdvancement("visit_aether_forest", p) &&
								hasAdvancement("visit_aether", p)) {
							grantAdvancement("explore", p);
						}
					}
				}
			}
		}, 20L, 20L);
	}
	public static void grantAdvancement(String adv, Player player) {
		NamespacedKey nsk = new NamespacedKey(main, "outer_end/" + adv);
		org.bukkit.advancement.Advancement a = main.getServer().getAdvancement(nsk);
		if (a != null) {
			AdvancementProgress avp = player.getAdvancementProgress(a);
			if (!avp.isDone()) {
				main.getServer().dispatchCommand(main.getServer().getConsoleSender(), "advancement grant " + player.getName() + " only betterend:outer_end/" + adv);
			}
		} else {
			throw new IllegalArgumentException("Invalid advancement name \"" + adv + "\"");
		}
	}
	public static boolean hasAdvancement(String adv, Player player) {
		NamespacedKey nsk = new NamespacedKey(main, "outer_end/" + adv);
		org.bukkit.advancement.Advancement a = main.getServer().getAdvancement(nsk);
		if (a != null) {
			AdvancementProgress avp = player.getAdvancementProgress(a);
			return avp.isDone();
		} else {
			throw new IllegalArgumentException("Invalid advancement name \"" + adv + "\"");
		}
	}
}
