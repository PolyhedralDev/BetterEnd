package com.dfsek.betterEnd.advancement.trigger;

import com.google.gson.JsonObject;
import com.dfsek.betterEnd.advancement.shared.ItemObject;
import com.dfsek.betterEnd.advancement.shared.RangeObject;
import com.dfsek.betterEnd.advancement.util.JsonBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Fires whenever the player enchants an item. The item doesn't have to be taken out of the enchanting table.
 */
public class EnchantedItemTrigger extends Trigger {
	private ItemObject item;
	private RangeObject levels;
	
	public EnchantedItemTrigger() {
		super(Type.ENCHANTED_ITEM);
	}
	
	
	
	/**
	 * @return the item after it has been enchanted or null, if none was specified
	 */
	@Nullable
	@Contract(pure = true)
	public ItemObject getItem() {
		return item;
	}
	
	/**
	 * @return the number of levels the player used up to enchant the item or null, if none was specified
	 */
	@Nullable
	@Contract(pure = true)
	public RangeObject getLevels() {
		return levels;
	}
	
	
	
	/**
	 * @param item the item after it has been enchanted or null, if it should be cleared
	 * @return the current trigger for chaining
	 */
	@NotNull
	public EnchantedItemTrigger setItem(@Nullable ItemObject item) {
		this.item = item;
		return this;
	}
	
	/**
	 * @param levels the number of levels the player used up to enchant the item or null, if it should be cleared
	 * @return the current trigger for chaining
	 */
	@NotNull
	public EnchantedItemTrigger setLevels(@Nullable RangeObject levels) {
		this.levels = levels;
		return this;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	protected JsonObject getConditions() {
		return new JsonBuilder()
				.add("item", item)
				.add("levels", levels)
				.build();
	}
}
