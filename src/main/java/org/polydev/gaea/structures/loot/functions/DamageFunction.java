package org.polydev.gaea.structures.loot.functions;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class DamageFunction implements Function {
    private final int max;
    private final int min;
    public DamageFunction(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public ItemStack apply(ItemStack original, Random r) {
        double itemDurability = (r.nextDouble() * (max - min)) + min;
        Damageable damage = (Damageable) original.getItemMeta();
        damage.setDamage((int) (original.getType().getMaxDurability() - (itemDurability / 100) * original.getType().getMaxDurability()));
        original.setItemMeta((ItemMeta) damage);
        return original;
    }
}
