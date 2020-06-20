package com.dfsek.betterend.structures.fortress;

public class FortressPartRotation {
	public static FortressPartRotation NONE;
	public static FortressPartRotation CW_90_DEG;
	public static FortressPartRotation CCW_90_DEG;
	public static FortressPartRotation R_180_DEG;
	
	
	public int getRotationInt() {
		if(this == NONE) return 0;
		else if(this == CW_90_DEG) return 90;
		else if(this == R_180_DEG) return 180;
		return 270;
	}
}
