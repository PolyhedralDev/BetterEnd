package com.dfsek.betterend;

import org.bukkit.Material;

public class Ore {
	private int contChance;
	private Material ore;
	
	public Ore(Material ore, int contChance) {
		this.contChance = contChance;
		this.ore = ore;
	}
	public int getContChance() {
		return contChance;
	}
	public Material getType() {
		return ore;
	}
}
