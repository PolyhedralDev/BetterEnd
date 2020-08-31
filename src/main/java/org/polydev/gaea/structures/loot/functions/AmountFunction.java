package org.polydev.gaea.structures.loot.functions;

import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class AmountFunction implements Function {
    private final int max;
    private final int min;
    public AmountFunction(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public ItemStack apply(ItemStack original, Random r) {
        original.setAmount(r.nextInt(max-min+1)+min);
        return null;
    }
}
