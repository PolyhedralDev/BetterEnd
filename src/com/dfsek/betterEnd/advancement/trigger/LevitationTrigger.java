package com.dfsek.betterEnd.advancement.trigger;

import com.google.gson.JsonObject;
import com.dfsek.betterEnd.advancement.shared.DistanceObject;
import com.dfsek.betterEnd.advancement.shared.RangeObject;
import com.dfsek.betterEnd.advancement.util.JsonBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Fires whenever the player is under the levitation status effect.
 */
public class LevitationTrigger extends Trigger {
	private RangeObject duration;
	private DistanceObject distance;
	
	public LevitationTrigger() {
		super(Type.LEVITATION);
	}
	
	
	
	/**
	 * @return the duration or null, if none was specified
	 */
	@Nullable
	@Contract(pure = true)
	public RangeObject getDuration() {
		return duration;
	}
	
	/**
	 * @return information about the distance of the entity or null, if none was specified
	 */
	@Nullable
	@Contract(pure = true)
	public DistanceObject getDistance() {
		return distance;
	}
	
	
	
	/**
	 * @param duration the duration or null, if it should be cleared
	 * @return the current trigger for chaining
	 */
	@NotNull
	public LevitationTrigger setDuration(@Nullable RangeObject duration) {
		this.duration = duration;
		return this;
	}
	
	/**
	 * @param distance information about the distance of the entity or null, if it should be cleared
	 * @return the current trigger for chaining
	 */
	@NotNull
	public LevitationTrigger setDistance(@Nullable DistanceObject distance) {
		this.distance = distance;
		return this;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	protected JsonObject getConditions() {
		return new JsonBuilder()
				.add("duration", duration)
				.add("distance", distance)
				.build();
	}
}
