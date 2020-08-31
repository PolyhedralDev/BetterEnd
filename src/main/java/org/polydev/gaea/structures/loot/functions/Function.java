package org.polydev.gaea.structures.loot.functions;

import org.bukkit.inventory.ItemStack;

import java.util.Random;

public interface Function {
    ItemStack apply(ItemStack original, Random r);
}
