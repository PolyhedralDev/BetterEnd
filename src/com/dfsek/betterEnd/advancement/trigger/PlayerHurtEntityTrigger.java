package com.dfsek.betterend.advancement.trigger;

import com.google.gson.JsonObject;
import com.dfsek.betterend.advancement.shared.DamageObject;
import com.dfsek.betterend.advancement.shared.EntityObject;
import com.dfsek.betterend.advancement.util.JsonBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Fires whenever a player deals any amount of damage (including 0) to an entity (excluding players).
 */
public class PlayerHurtEntityTrigger extends Trigger {
	private DamageObject damage;
	private EntityObject entity;
	
	public PlayerHurtEntityTrigger() {
		super(Type.PLAYER_HURT_ENTITY);
	}
	
	
	
	/**
	 * @return information about the damage event or null, if none was specified
	 */
	@Nullable
	@Contract(pure = true)
	public DamageObject getDamage() {
		return damage;
	}
	
	/**
	 * @return information about the entity that the player had damaged or null, if none was specified
	 */
	@Nullable
	@Contract(pure = true)
	public EntityObject getEntity() {
		return entity;
	}
	
	
	
	/**
	 * @param damage information about the damage event or null, if it should be cleared
	 * @return the current trigger for chaining
	 */
	@NotNull
	public PlayerHurtEntityTrigger setDamage(@Nullable DamageObject damage) {
		this.damage = damage;
		return this;
	}
	
	/**
	 * @param entity information about the entity that the player had damaged or null, if it should be cleared
	 * @return the current trigger for chaining
	 */
	@NotNull
	public PlayerHurtEntityTrigger setEntity(@Nullable EntityObject entity) {
		this.entity = entity;
		return this;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	protected JsonObject getConditions() {
		return new JsonBuilder()
				.add("damage", damage)
				.add("entity", entity)
				.build();
	}
}
