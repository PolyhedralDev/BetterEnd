package com.dfsek.betterEnd;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

import com.dfsek.betterEnd.advancement.Advancement;
import com.dfsek.betterEnd.advancement.AdvancementFactory;
import com.dfsek.betterEnd.advancement.Rewards;
import com.dfsek.betterEnd.advancement.shared.ItemObject;
import com.dfsek.betterEnd.advancement.trigger.PlayerKilledEntityTrigger;

import net.md_5.bungee.api.chat.TextComponent;

public class EndAdvancements {
	public static void enable(Plugin plugin) {
		AdvancementFactory factory = new AdvancementFactory(plugin, true, false);

		//Create a root advancement which is also automatically unlocked (with a player head icon)
		Advancement root = new Advancement(new NamespacedKey(plugin, "end/root"), new ItemObject().setItem(Material.END_STONE), new TextComponent("The End"), new TextComponent("Or the beginning?"));

		Advancement aetherVisit = factory.getItem("outer_end/visit_aether", root, "A Hostile Paradise", "Enter the Aether", Material.OAK_LOG);
		Advancement aetherHighlandsVisit = factory.getItem("outer_end/visit_aether_highlands", aetherVisit, "Game Design", "Enter the Aether Highlands", Material.OAK_LOG);
		Advancement aetherForestVisit = factory.getItem("outer_end/visit_aether_forest", aetherVisit, "Trees.", "Enter the Aether Forest", Material.OAK_LOG);
		Advancement aetherHighlandsForestVisit = factory.getItem("outer_end/visit_aether_highlands_forest", aetherHighlandsVisit, "S p r o c e", "Enter the Aether Highlands Forest", Material.OAK_LOG);
		Advancement goldDungeon = factory.getItem("outer_end/gold_dungeon", aetherVisit, "I've Got a Bad Feeling About This...", "Open a Gold Dungeon Chest.", Material.OAK_LOG);
		
		
		Advancement endVisit = factory.getItem("outer_end/visit_end", root, "Finally, Sonething Familiar", "Enter the End", Material.END_STONE);
		
		Advancement shatteredVisit = factory.getItem("outer_end/visit_shattered_end", root, "What Happened Here?", "Enter the Shattered End", Material.OAK_LOG);
		Advancement shatteredForestVisit = factory.getItem("outer_end/visit_shattered_end", shatteredVisit, "Funky Trees", "Enter the Shattered Forest", Material.OAK_LOG);
		
		//One of the most common advancements, the requirement is that you obtain an item:
		//Advancement wood = factory.getItem("outer_end/wood", root, "Chopper", "Chop down a tree", Material.OAK_LOG);
		//I could still use a factory, but I wanted to give an example of how development works without it:

		//Reload the data cache after all advancements have been added
		Bukkit.reloadData();
	}
}
