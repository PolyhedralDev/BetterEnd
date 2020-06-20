package com.dfsek.betterend.structures.fortress;

public class FortressNode {
	FortressNodeType type;
	FortressPartRotation rotation;
	public FortressNode(FortressNodeType type, FortressPartRotation rotation) {
		this.type = type;
		this.rotation = rotation;
	}
	public FortressNodeType getType() {
		return this.type;
	}
	public FortressPartRotation getRotation() {
		return this.rotation;
	}
	public FortressNodeType setType(FortressNodeType type) {
		this.type = type;
		return this.type;
	}
	public FortressPartRotation setRotation(FortressPartRotation rotation) {
		this.rotation = rotation;
		return this.rotation;
	}
}
