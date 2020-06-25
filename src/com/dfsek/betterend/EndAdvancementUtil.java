package com.dfsek.betterend;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.dfsek.betterend.advancement.Advancement;
import com.dfsek.betterend.advancement.AdvancementFactory;
import com.dfsek.betterend.advancement.shared.ItemObject;
import com.dfsek.betterend.advancement.trigger.ImpossibleTrigger;

import net.md_5.bungee.api.chat.TextComponent;

public class EndAdvancementUtil {
	private static Main main = Main.getInstance();
	public static void enable(Plugin plugin) {
		try {

			new Advancement(new NamespacedKey(plugin, "outer_end/explore"), new ItemObject().setItem(Material.COMPASS), new TextComponent("Ender Explorer"), new TextComponent("Find all 9 Outer End biomes"))
			.makeChild("minecraft:end/enter_end_gateway")
			.addTrigger("0", new ImpossibleTrigger())
			.setFrame(Advancement.Frame.GOAL)
			.activate(true);
		} catch (IllegalArgumentException e) {
		}
		AdvancementFactory factory = new AdvancementFactory(plugin, true, false);
		try {
			Advancement aetherVisit = new Advancement(new NamespacedKey(plugin, "outer_end/visit_aether"), new ItemObject().setItem(Material.OAK_LOG), new TextComponent("A Hostile Paradise"), new TextComponent("Enter the Aether"));
			aetherVisit.makeChild("minecraft:end/enter_end_gateway")
			.addTrigger("0", new ImpossibleTrigger())
			.activate(true);

			try {
				Advancement aetherHighlandsVisit = factory.getImpossible("outer_end/visit_aether_highlands", aetherVisit, "Game Design", "Enter the Highlands", Material.SPRUCE_LOG).setHidden(true);
				factory.getImpossible("outer_end/visit_aether_forest", aetherVisit, "Big Trees", "Enter the Aether Forest", Material.OAK_SAPLING).setHidden(true).setAnnounce(false);
				factory.getImpossible("outer_end/visit_aether_highlands_forest", aetherHighlandsVisit, "Another Forest?", "Enter the Highlands Forest", Material.SPRUCE_SAPLING).setHidden(true).setAnnounce(false);
				factory.getImpossible("outer_end/gold_dungeon", aetherVisit, "I've Got a Bad Feeling About This...", "Open a Gold Dungeon Chest. What could go wrong?", Material.CHISELED_QUARTZ_BLOCK).setHidden(true);
			} catch (IllegalArgumentException e) {
			}
		} catch (IllegalArgumentException e) {
		}

		try {
			Advancement voidVisit = new Advancement(new NamespacedKey(plugin, "outer_end/visit_void"), new ItemObject().setItem(Material.BLACK_CONCRETE), new TextComponent("Don't Fall"), new TextComponent("Enter a Void Biome"));
			voidVisit.makeChild("minecraft:end/enter_end_gateway")
			.addTrigger("0", new ImpossibleTrigger())
			.activate(true);

			factory.getImpossible("outer_end/visit_starfield", voidVisit, "A Field of Stars", "Enter a Starfield Biome", Material.GLOWSTONE).setHidden(true).setAnnounce(false);
			factory.getImpossible("outer_end/into_void", voidVisit, "Embrace the Void", "Travel below Y level -64", Material.BLACK_CONCRETE).setHidden(true);
			factory.getImpossible("outer_end/dizzying_heights", voidVisit, "Dizzying Heights", "Travel above Y level 5000", Material.WHITE_CONCRETE).setHidden(true);

		} catch (IllegalArgumentException e) {
		}

		try {
			new Advancement(new NamespacedKey(plugin, "outer_end/visit_end"), new ItemObject().setItem(Material.END_STONE), new TextComponent("Finally, Something Familiar"), new TextComponent("Enter the End"))
			.makeChild("minecraft:end/enter_end_gateway")
			.addTrigger("0", new ImpossibleTrigger())
			.activate(true);
		} catch (IllegalArgumentException e) {
		}


		try {
			Advancement shatteredVisit = new Advancement(new NamespacedKey(plugin, "outer_end/visit_shattered_end"), new ItemObject().setItem(Material.BLACK_CONCRETE), new TextComponent("What Happened Here?"), new TextComponent("Enter the Shattered End"));
			shatteredVisit.makeChild("minecraft:end/enter_end_gateway").addTrigger("0", new ImpossibleTrigger()).activate(true);
			factory.getImpossible("outer_end/visit_shattered_forest", shatteredVisit, "Funky Trees", "Enter the Shattered Forest", Material.PURPLE_STAINED_GLASS).setHidden(true);

		} catch (IllegalArgumentException e) {
		}
		Bukkit.reloadData();

		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				for(Player p : plugin.getServer().getOnlinePlayers()) {
					double totalChunkDistance2D = Math.sqrt(Math.pow(p.getLocation().getChunk().getX(), 2)+Math.pow(p.getLocation().getChunk().getZ(), 2));
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
