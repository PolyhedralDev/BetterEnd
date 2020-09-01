package com.dfsek.betterend;

import com.dfsek.betterend.world.EndBiomeGrid;
import com.dfsek.betterend.config.WorldConfig;
import org.polydev.gaea.tree.CustomTreeType;
import com.dfsek.betterend.util.ThreadedTreeUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.TreeType;
import org.bukkit.block.Chest;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.persistence.PersistentDataType;

import com.dfsek.betterend.util.BossTimeoutUtil;
import com.dfsek.betterend.config.ConfigUtil;
import com.dfsek.betterend.util.EndAdvancementUtil;
import com.dfsek.betterend.world.EndBiome;
import com.dfsek.betterend.world.EndChunkGenerator;

import io.lumine.xikage.mythicmobs.MythicMobs;

import java.util.Random;

public class EventListener implements Listener {
	private final BetterEnd main = BetterEnd.getInstance();

	@EventHandler(ignoreCancelled = true)
	public void onInventoryOpenEvent(InventoryOpenEvent event) {
		if(WorldConfig.fromWorld(event.getPlayer().getWorld()).mythicBossEnable) {
			InventoryHolder holder = event.getInventory().getHolder();
			Inventory inventory = event.getInventory();
			if(inventory.getHolder() instanceof Chest) {
				Location l = ((Chest) holder).getLocation();
				if(ConfigUtil.debug) main.getLogger()
						.info("[BetterEnd] Player opened chest in " + l.getWorld() + " at " + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ());
				Chest chest = (Chest) l.getBlock().getState();
				NamespacedKey key = new NamespacedKey(main, "valkyrie-spawner");
				if(chest.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
					if(BetterEnd.isPremium()) EndAdvancementUtil.grantAdvancement("gold_dungeon", (Player) event.getPlayer());
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
					if(ConfigUtil.debug) main.getLogger().info("[BetterEnd] Chest is a Mythic Boss Spawn Chest.");
					String boss = WorldConfig.fromWorld(event.getPlayer().getWorld()).mythicBossName;
					try {
						if(!BetterEnd.isPremium() || BossTimeoutUtil.timeoutReached(chest)) MythicMobs.inst().getMobManager().spawnMob(boss, spawn);
					} catch(NoClassDefFoundError e) {
						main.getLogger().warning("Failed to spawn Mythic Boss. Is MythicMobs installed?");
					}
				}
				if(!BetterEnd.isPremium()) {
					chest.getPersistentDataContainer().remove(key);
					chest.update();
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityPickup(EntityChangeBlockEvent event) {
		if(event.getEntity() instanceof Enderman && event.getEntity().getWorld().getGenerator() instanceof EndChunkGenerator && WorldConfig.fromWorld(event.getEntity().getWorld()).endermanBlockPickup
				&& EndBiomeGrid.fromWorld(event.getEntity().getWorld()).getBiome(event.getBlock().getLocation()).isAether()) {
			event.setCancelled(true);
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void saplingOverride(StructureGrowEvent e) {
		if(BetterEnd.isPremium() && (((WorldConfig.fromWorld(e.getWorld()).bigTreeSaplingWorld || WorldConfig.fromWorld(e.getWorld()).bigTreeSaplingBiomes) && e.getWorld().getGenerator() instanceof EndChunkGenerator) || ConfigUtil.generateBigTreesEverywhere)) {
			Random treeRandom = new Random();
			if((WorldConfig.fromWorld(e.getWorld()).bigTreeSaplingWorld || ConfigUtil.generateBigTreesEverywhere || EndBiomeGrid.fromWorld(e.getWorld()).getBiome(e.getLocation()).equals(EndBiome.AETHER_FOREST)) && (e.getSpecies().equals(TreeType.TREE) || e.getSpecies().equals(TreeType.BIG_TREE))) {
				if(treeRandom.nextInt(100) < 100/ConfigUtil.treeGrowthMultiplier) {
					e.getLocation().getBlock().setType(Material.AIR);
					ThreadedTreeUtil.plantLargeTree(CustomTreeType.GIANT_OAK, e.getLocation(), treeRandom);
				}
				e.setCancelled(true);
			} else if((WorldConfig.fromWorld(e.getWorld()).bigTreeSaplingWorld || ConfigUtil.generateBigTreesEverywhere || EndBiomeGrid.fromWorld(e.getWorld()).getBiome(e.getLocation()).equals(EndBiome.AETHER_HIGHLANDS_FOREST)) && (e.getSpecies().equals(TreeType.TALL_REDWOOD) || e.getSpecies().equals(TreeType.REDWOOD) || e.getSpecies().equals(TreeType.MEGA_REDWOOD))) {
				if(treeRandom.nextInt(100) < 100/ConfigUtil.treeGrowthMultiplier) {
					e.getLocation().getBlock().setType(Material.AIR);
					ThreadedTreeUtil.plantLargeTree(CustomTreeType.GIANT_SPRUCE, e.getLocation(), treeRandom);
				}
				e.setCancelled(true);
			}
		}
	}
}
