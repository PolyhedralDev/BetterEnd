package org.polydev.gaea.structures.loot;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LootTable {
    private final List<Pool> pools = new ArrayList<>();
    public LootTable(String json) throws ParseException {
        System.out.println(json);
        JSONParser jsonParser = new JSONParser();
        Object tableJSON = jsonParser.parse(json);
        JSONArray poolArray = (JSONArray) ((JSONObject) tableJSON).get("pools");
        for(Object pool: poolArray) {
            pools.add(new Pool((JSONObject) pool));
        }
    }
    public List<ItemStack> getLoot(Random r) {
        List<ItemStack> itemList = new ArrayList<>();
        for(Pool pool : pools) {
            itemList.addAll(pool.getItems(r));
        }
        return itemList;
    }
    public void fillInventory(Inventory i, Random r) {
        List<ItemStack> loot = getLoot(r);
        for(ItemStack stack : loot) {
            i.setItem(r.nextInt(i.getSize()), stack);
        }
    }
}
