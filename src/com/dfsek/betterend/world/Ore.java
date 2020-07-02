package com.dfsek.betterend.world;

import org.bukkit.Material;

public class Ore {
	private int contChance;
	private Material oreMaterial;
	
	public Ore(Material oreMaterial, int contChance) {
		this.contChance = contChance;
		this.oreMaterial = oreMaterial;
	}
	public int getContChance() {
		return contChance;
	}
	public Material getType() {
		return oreMaterial;
	}
}
