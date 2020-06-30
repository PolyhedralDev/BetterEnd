package com.dfsek.betterend;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.persistence.PersistentDataType;

import com.dfsek.betterend.util.BossTimeoutUtil;
import com.dfsek.betterend.util.ConfigUtil;
import com.dfsek.betterend.util.EndAdvancementUtil;
import com.dfsek.betterend.world.Biome;
import com.dfsek.betterend.world.generation.EndChunkGenerator;

import io.lumine.xikage.mythicmobs.MythicMobs;

public class EventListener implements Listener {
	private Main main = Main.getInstance();
	@EventHandler (ignoreCancelled=true)
	public void onInventoryOpenEvent(InventoryOpenEvent event) {
		if(ConfigUtil.ENABLE_MYTHIC_BOSS) {
			InventoryHolder holder = event.getInventory().getHolder();
			Inventory inventory = event.getInventory();
			if (inventory.getHolder() instanceof Chest) {
				Location l = ((Chest) holder).getLocation();
				if(ConfigUtil.DEBUG) System.out.println("[BetterEnd] Player opened chest in " + l.getWorld() + " at " + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ());
				Chest chest = (Chest) l.getBlock().getState();
				NamespacedKey key = new NamespacedKey(main, "valkyrie-spawner");
				if(chest.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
					if(Main.isPremium()) EndAdvancementUtil.grantAdvancement("gold_dungeon", (Player) event.getPlayer());
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
					if(ConfigUtil.DEBUG) System.out.println("[BetterEnd] Chest is a Mythic Boss Spawn Chest.");
					String boss = ConfigUtil.BOSS_NAME_GOLD;
					try {
						if(Main.isPremium() && BossTimeoutUtil.timeoutReached(chest)) MythicMobs.inst().getMobManager().spawnMob(boss, spawn);
						else if(!Main.isPremium())MythicMobs.inst().getMobManager().spawnMob(boss, spawn);
					} catch(NoClassDefFoundError e) {
						main.getLogger().warning("Failed to spawn Mythic Boss. Is MythicMobs installed?");
					}
				}
				if(!Main.isPremium()) {
					chest.getPersistentDataContainer().remove(key);
					chest.update();
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityPickup(EntityChangeBlockEvent event) {
		if(event.getEntity() instanceof Enderman && event.getEntity().getWorld().getGenerator() instanceof EndChunkGenerator && ConfigUtil.PREVENT_ENDERMAN_PICKUP && Biome.fromLocation(event.getBlock().getLocation()).isAether()) {
			event.setCancelled(true);
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void tabCompleteEvent(TabCompleteEvent event) {
		if(ConfigUtil.DEBUG) Bukkit.getConsoleSender().sendMessage("Tab completion detected from " + event.getSender().getName() + ", buffer: " + event.getBuffer() + ", Cancelled: " + event.isCancelled() + ", Completion options: " + String.join("\n", event.getCompletions()));
	}
}
