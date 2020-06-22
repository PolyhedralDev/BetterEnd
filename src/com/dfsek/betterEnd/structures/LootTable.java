package com.dfsek.betterend.structures;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Collections;

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

import com.dfsek.betterend.ConfigUtil;
import com.dfsek.betterend.Main;

public class LootTable {
	private Object tableJSON;
	private static Main main = Main.getInstance();

	/**
	 * Load a loot table from a name.
	 * @param name - The loot table name.
	 */
	public LootTable(String name) {
		File tableFile = new File(main.getDataFolder() + File.separator + "loot" + File.separator + name +  ".json");
		String json = "{}";
		if(Main.isPremium() && tableFile.exists()) {
			String line;
			try {
				InputStream is = new FileInputStream(tableFile);
				BufferedReader buf = new BufferedReader(new InputStreamReader(is));
				line = buf.readLine();
				StringBuilder sb = new StringBuilder();
				while(line != null){
					sb.append(line).append("\n");
					line = buf.readLine();
				}
				buf.close();
				json = sb.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			InputStream  inputStream = main.getResource("loot/" + name + ".json");
			InputStreamReader isReader = new InputStreamReader(inputStream);
			BufferedReader reader = new BufferedReader(isReader);
			StringBuffer sb = new StringBuffer();
			String str;
			try {
				while((str = reader.readLine())!= null){
					sb.append(str);
				}
			} catch (IOException e) {
				main.getLogger().warning("Error retrieving loot table \""  + name + "\"");
			}
			json = sb.toString();
		}
		JSONParser jsonParser = new JSONParser();
		try {
			this.tableJSON = jsonParser.parse(json);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void populateChest(Location location, Random random) {
		JSONArray poolArray = (JSONArray) ((JSONObject) this.tableJSON).get("pools");
		if (location.getBlock().getState() instanceof Container && (location.getBlock().getType() == Material.CHEST || location.getBlock().getType() == Material.TRAPPED_CHEST)) {
			for (Object pool : poolArray) {
				JSONObject pooldata = (JSONObject) pool;
				int max = Math.toIntExact((long) ((JSONObject)pooldata.get("rolls")).get("max"));
				int min = Math.toIntExact((long) ((JSONObject)pooldata.get("rolls")).get("min"));

				JSONArray itemArray = (JSONArray) pooldata.get("entries");
				int rolls = random.nextInt(max-min+1)+min;
				if(ConfigUtil.DEBUG) System.out.println("[BetterEnd] min: " + min + ", max: " + max + ", " + rolls + " rolls.");

				for(int i = 0; i < rolls; i++) {
					int count = 1;
					JSONObject itemdata = (JSONObject) chooseOnWeight(itemArray);
					String itemname = (String) itemdata.get("name");
					double itemDurability = 100;
					double enchant = 0;
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
									enchant = (random.nextDouble()*(maxd-mind))+mind;
								}
							}
						} catch(ClassCastException e) {
							if(ConfigUtil.DEBUG) System.out.println("[BetterEnd] Error on item \""+ itemname + "\"");
						}
					}
					if(ConfigUtil.DEBUG) System.out.println("[BetterEnd] "+ itemname + " x" + count + ", durability=" + itemDurability + ", enchant lvl=" + enchant);
					try {
						ItemStack randomItem = new ItemStack(Material.valueOf(itemname.toUpperCase()), count);
						if(enchant != 0) randomItem = randomEnchantment(randomItem, enchant, random);
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
						System.out.println("[BetterEnd] Invalid item \""+ itemname + "\"");
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
	public ItemStack randomEnchantment(ItemStack item, double enchant, Random random) {
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
