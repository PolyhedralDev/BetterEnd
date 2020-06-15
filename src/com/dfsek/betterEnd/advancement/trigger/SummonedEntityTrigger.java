package com.dfsek.betterend.advancement.trigger;

import com.google.gson.JsonObject;
import com.dfsek.betterend.advancement.shared.EntityObject;
import com.dfsek.betterend.advancement.util.JsonBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Fires whenever the player creates an entity. In the case of the ender dragon, all players able to view the boss bar fulfill the trigger.
 */
public class SummonedEntityTrigger extends Trigger {
	private EntityObject entity;
	
	public SummonedEntityTrigger() {
		super(Type.SUMMONED_ENTITY);
	}
	
	
	
	/**
	 * @return information about the tamed entity or null, if none was specified
	 */
	@Nullable
	@Contract(pure = true)
	public EntityObject getEntity() {
		return entity;
	}
	
	/**
	 * @param entity information about the tamed entity or null, if it should be cleared
	 * @return the current trigger for chaining
	 */
	@NotNull
	public SummonedEntityTrigger setEntity(@Nullable EntityObject entity) {
		this.entity = entity;
		return this;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	protected JsonObject getConditions() {
		return new JsonBuilder()
				.add("entity", entity)
				.build();
	}
}