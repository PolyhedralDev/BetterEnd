package org.polydev.gaea.structures.loot;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.polydev.gaea.structures.loot.functions.AmountFunction;
import org.polydev.gaea.structures.loot.functions.DamageFunction;
import org.polydev.gaea.structures.loot.functions.Function;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Entry {
    private final JSONObject entry;
    private final Material item;
    private final long weight;
    private final List<Function> functions = new ArrayList<>();
    public Entry(JSONObject entry) {
        this.entry = entry;
        this.item = Material.valueOf(entry.get("name").toString().toUpperCase());
        this.weight = (long) entry.get("weight");
        if(this.entry.containsKey("functions")) {
            for (Object function : (JSONArray) this.entry.get("functions")) {
                switch(((String) ((JSONObject) function).get("function"))) {
                    case "set_count":
                        long max = (long) ((JSONObject) ((JSONObject) function).get("count")).get("max");
                        long min = (long) ((JSONObject) ((JSONObject) function).get("count")).get("min");
                        functions.add(new AmountFunction(Math.toIntExact(min), Math.toIntExact(max)));
                        break;
                    case "set_damage":
                        long maxDamage = (long) ((JSONObject) ((JSONObject) function).get("damage")).get("max");
                        long minDamage = (long) ((JSONObject) ((JSONObject) function).get("damage")).get("min");
                        functions.add(new DamageFunction(Math.toIntExact(minDamage), Math.toIntExact(maxDamage)));
                }
            }
        }
    }
    public ItemStack getItem(Random r) {
        ItemStack item = new ItemStack(this.item, 1);
        for(Function f : functions) {
            item = f.apply(item, r);
        }
        return item;
    }
    public long getWeight() {
        return this.weight;
    }
}
