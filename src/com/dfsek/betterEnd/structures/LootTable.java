package com.dfsek.betterend.structures;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.dfsek.betterend.Main;
import com.dfsek.betterend.util.ConfigUtil;
import com.dfsek.betterend.util.Util;

public class LootTable {
	private Object tableJSON;
	private static Main main = Main.getInstance();

	/**
	 * Loads a loot table with a name.<br>
	 * If the premium version of the plugin is used, the loot/ folder will be checked for a matching table before an attempt is made to load the packaged version.
	 * @author dfsek
	 * @since 1.0.0
	 * @param name - The loot table name.
	 */
	public LootTable(String name) {
		File tableFile = new File(main.getDataFolder() + File.separator + "loot" + File.separator + name +  ".json");
		String json = "{}";
		if(Main.isPremium() && tableFile.exists()) {
			try {
				json = Util.getFileAsString(new FileInputStream(tableFile));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				json = Util.getFileAsString(main.getResource("loot/" + name + ".json"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		JSONParser jsonParser = new JSONParser();
		try {
			this.tableJSON = jsonParser.parse(json);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Populates a chest with a loot table.
	 * @author dfsek
	 * @since 1.0.0
	 * @param location - The location of the chest.
	 * @param random - The Random object to populate with.
	 */
	public void populateChest(Location location, Random random) {
		JSONArray poolArray = (JSONArray) ((JSONObject) this.tableJSON).get("pools");
		if (location.getBlock().getState() instanceof Container && (location.getBlock().getType() == Material.CHEST || location.getBlock().getType() == Material.TRAPPED_CHEST)) {
			for (Object pool : poolArray) {
				JSONObject pooldata = (JSONObject) pool;
				int max = Math.toIntExact((long) ((JSONObject)pooldata.get("rolls")).get("max"));
				int min = Math.toIntExact((long) ((JSONObject)pooldata.get("rolls")).get("min"));

				JSONArray itemArray = (JSONArray) pooldata.get("entries");
				int rolls = random.nextInt(max-min+1)+min;
				if(ConfigUtil.DEBUG) main.getLogger().info("[BetterEnd] min: " + min + ", max: " + max + ", " + rolls + " rolls.");

				for(int i = 0; i < rolls; i++) {
					int count = 1;
					JSONObject itemdata = (JSONObject) chooseOnWeight(itemArray);
					String itemname = (String) itemdata.get("name");
					double itemDurability = 100;
					double enchant = 0;
					JSONArray disabled = new JSONArray();;
					if(itemdata.containsKey("functions")) {
						try {
							for (Object function : (JSONArray) itemdata.get("functions")) {
								if(((String) ((JSONObject) function).get("function")).equalsIgnoreCase("set_count")) {
									long maxc = (long) ((JSONObject)((JSONObject)function).get("count")).get("max");
									long minc = (long) ((JSONObject)((JSONObject)function).get("count")).get("min");
									count = random.nextInt(Math.toIntExact(maxc)-Math.toIntExact(minc)) + Math.toIntExact(minc);
								}
								if(((String) ((JSONObject) function).get("function")).equalsIgnoreCase("set_damage")) {
									long maxd = (long) ((JSONObject)((JSONObject)function).get("damage")).get("max");
									long mind = (long) ((JSONObject)((JSONObject)function).get("damage")).get("min");
									itemDurability = (random.nextDouble()*(maxd-mind))+mind;
								}
								if(((String) ((JSONObject) function).get("function")).equalsIgnoreCase("enchant_with_levels")) {
									long maxd = (long) ((JSONObject)((JSONObject)function).get("levels")).get("max");
									long mind = (long) ((JSONObject)((JSONObject)function).get("levels")).get("min");

									try {
										disabled = (JSONArray) ((JSONObject)function).get("disabled_enchants");
										enchant = (random.nextDouble()*(maxd-mind))+mind;
									} catch(ClassCastException e) {
									}
								}
							}
						} catch(ClassCastException | IllegalArgumentException e) {
							main.getLogger().severe("[BetterEnd] An unexpected exception was thrown whilst populating item \""+ itemname + "\"");
							main.getLogger().severe(e.getMessage());
							main.getLogger().severe("If you are using a custom loot table, double-check it for errors. Enabling debug mode via config.yml may help determine the cause.");
							main.getLogger().severe("If you aren't, report this error to the BetterEnd Issue Tracker.");
						}
					}
					if(ConfigUtil.DEBUG) main.getLogger().info("[BetterEnd] "+ itemname + " x" + count + ", durability=" + itemDurability + ", enchant lvl=" + enchant);
					try {
						ItemStack randomItem = new ItemStack(Material.valueOf(itemname.toUpperCase()), count);
						if(enchant != 0) randomItem = randomEnchantment(randomItem, enchant, random, disabled);
						Damageable damage = (Damageable) randomItem.getItemMeta();
						damage.setDamage((int) (Material.valueOf(itemname.toUpperCase()).getMaxDurability()-(itemDurability/100)*Material.valueOf(itemname.toUpperCase()).getMaxDurability()));
						randomItem.setItemMeta((ItemMeta) damage);

						BlockState blockState = location.getBlock().getState();
						Container container = (Container) blockState;
						Inventory containerInventory = container.getInventory();
						ItemStack[] containerContent = containerInventory.getContents();
						for (int j = 0; j < randomItem.getAmount(); j++) {
							boolean done = false;
							int attemps = 0;
							while (!done) {
								int randomPos = random.nextInt(containerContent.length);
								ItemStack randomPosItem = containerInventory.getItem(randomPos);
								if (randomPosItem != null) {
									if (this.isSameItem(randomPosItem, randomItem) && randomPosItem.getAmount() < randomItem.getMaxStackSize()) {
										ItemStack randomItemCopy = randomItem.clone();
										int newAmount = randomPosItem.getAmount() + 1;
										randomItemCopy.setAmount(newAmount);
										containerContent[randomPos] = randomItemCopy;
										containerInventory.setContents(containerContent);
										done = true;
									}
								} else {
									ItemStack randomItemCopy = randomItem.clone();
									randomItemCopy.setAmount(1);
									containerContent[randomPos] = randomItemCopy;
									containerInventory.setContents(containerContent);
									done = true;
								}
								attemps++;
								if (attemps >= containerContent.length) {
									done = true;
								}
							}
						}
					} catch(IllegalArgumentException e) {
						e.printStackTrace();
						main.getLogger().info("[BetterEnd] Invalid item \""+ itemname + "\"");
					}

				}

			}
		}
	}
	private Object chooseOnWeight(JSONArray items) {
		double completeWeight = 0.0;
		for (Object item : items)
			completeWeight += Math.toIntExact((long) ((JSONObject) item).get("weight"));
		double r = Math.random() * completeWeight;
		double countWeight = 0.0;
		for (Object item : items) {
			countWeight += Math.toIntExact((long) ((JSONObject) item).get("weight"));
			if (countWeight >= r)
				return item;
		}
		return null;
	}
	/**
	 * Randomly enchants an item using Vanilla levels.
	 * @author dfsek
	 * @since 3.0.0
	 * @param item - the ItemStack to be enchanted.
	 * @param enchant - The Enchantment level.
	 * @param random - The Random object to use for enchanting.
	 * @param disabled - A JSONArray containing disabled enchantments.
	 * @return The enchanted ItemStack.
	 */
	@SuppressWarnings("deprecation") 
	public ItemStack randomEnchantment(ItemStack item, double enchant, Random random, JSONArray disabled) {
		List<Enchantment> possible = new ArrayList<Enchantment>();
		for (Enchantment ench : Enchantment.values()) {
			if (ench.canEnchantItem(item)) {
				possible.add(ench);
			}
		}
		int numEnchant = (int) (random.nextInt((int) Math.abs(enchant))/10+1);
		if (possible.size() >= numEnchant) {
			Collections.shuffle(possible);

			for(int i = 0; i < numEnchant; i++) {
				Enchantment chosen = possible.get(i);
				if(disabled != null && disabled.contains(chosen.getName())) continue;
				if(ConfigUtil.DEBUG) main.getLogger().info("Enchantment name: " + chosen.getName());
				for (Enchantment ench : item.getEnchantments().keySet()) {
					if(chosen.conflictsWith(ench)) continue;
				}

				int lvl = random.nextInt(1+(int) (((enchant/40 > 1) ? 1 : enchant/40)*((chosen.getMaxLevel()))));
				if(lvl != 0) item.addEnchantment(chosen, lvl);
				else item.addEnchantment(chosen, 1);
			}     
		}
		return item;
	}
	private boolean isSameItem(ItemStack randomPosItem, ItemStack randomItem) {
		ItemMeta randomPosItemMeta = randomPosItem.getItemMeta();
		ItemMeta randomItemMeta = randomItem.getItemMeta();

		return randomPosItem.getType().equals(randomItem.getType()) && randomPosItemMeta.equals(randomItemMeta);
	}
}
