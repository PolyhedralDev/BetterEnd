package com.dfsek.betterend.util;

import java.util.Date;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Chest;
import org.bukkit.persistence.PersistentDataType;

import com.dfsek.betterend.Main;

public class BossTimeoutUtil {
	private static final Main main = Main.getInstance();

	private BossTimeoutUtil() {
	}

	public static boolean timeoutReached(Chest chest) {
		NamespacedKey key = new NamespacedKey(main, "dungeon-timeout");
		long time = System.currentTimeMillis();
		try {
			if(ConfigUtil.debug) main.getLogger().info("current time: " + new Date(time).toString() + ", " + "needed time: "
					+ new Date(chest.getPersistentDataContainer().get(key, PersistentDataType.LONG)).toString());
		} catch(NullPointerException e) {
			if(ConfigUtil.debug) main.getLogger().info("current time: " + new Date(time).toString() + ", Time has not been set.");
		}
		try {
			if(chest.getPersistentDataContainer().get(key, PersistentDataType.LONG) < time) {
				if(ConfigUtil.debug) main.getLogger().info("Timeout reached.");
				chest.getPersistentDataContainer().set(key, PersistentDataType.LONG, time + ConfigUtil.bossRespawnTime);
				chest.update();
				return true;
			}
		} catch(NullPointerException e) {
			if(ConfigUtil.debug) main.getLogger().info("Timeout reached.");
			chest.getPersistentDataContainer().set(key, PersistentDataType.LONG, time + ConfigUtil.bossRespawnTime);
			chest.update();
			return true;
		}
		if(ConfigUtil.debug) main.getLogger().info("Timeout not reached.");
		return false;
	}
}
