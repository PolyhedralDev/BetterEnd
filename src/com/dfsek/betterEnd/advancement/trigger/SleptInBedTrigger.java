package com.dfsek.betterEnd.advancement.trigger;

import com.google.gson.JsonObject;
import com.dfsek.betterEnd.advancement.shared.LocationObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Fires when the player successfully enters a bed (the player does not have to pass the night).
 * For example if the bed explodes, such as from trying to sleep in the nether, this trigger will not fire.
 */
public class SleptInBedTrigger extends Trigger {
	private LocationObject location;
	
	public SleptInBedTrigger() {
		super(Type.SLEPT_IN_BED);
	}
	
	
	
	/**
	 * @return information about the location of the sleeping player or null, if none was specified
	 */
	@Nullable
	@Contract(pure = true)
	public LocationObject getLocation() {
		return location;
	}
	
	/**
	 * @param location information about the location of the sleeping player or null, if it should be cleared
	 * @return the current trigger for chaining
	 */
	@NotNull
	public SleptInBedTrigger setLocation(@Nullable LocationObject location) {
		this.location = location;
		return this;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	protected JsonObject getConditions() {
		return location == null ? new JsonObject() : location.toJson();
	}
}
