package com.dfsek.betterend.structures.fortress;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;

import com.dfsek.betterend.structures.NMSStructure;

public class Fortress {
	private List<List<FortressNode>> nodes = new ArrayList<List<FortressNode>>();
	//private List<List<FortressNodeType>> branches = new ArrayList<List<FortressNodeType>>();
	private Random random;
	private int size;
	public Fortress(int size, Random random) {
		this.random = random;
		this.size = size;
		for(int i = 0; i < size; i++) {
			this.nodes.add(new ArrayList<FortressNode>());
			for(int j = 0; j < size; j++) {
				this.nodes.get(i).add(new FortressNode(random.nextBoolean() ? FortressNodeType.NODE_BLANK : (random.nextBoolean() ? FortressNodeType.NODE_1WAY : FortressNodeType.NODE_2WAY), FortressPartRotation.NONE));
			}
		}
	}
	public FortressNode getNodeAt(int X, int Z) {
		if(X > size || Z > size) throw new IllegalArgumentException("Requested node out of bounds!");
		return nodes.get(X).get(Z);
	}
	public int getSize() {
		return this.size;
	}
	public void build(Location origin) {
		int x = 0;
		for(List<FortressNode> nodeList : nodes) {
			int z = 0;
			for(FortressNode node : nodeList) {
				if(node.getType() != FortressNodeType.NODE_BLANK) {
					NMSStructure part = new NMSStructure(origin.clone().add(x*42,0,z*42), "end_fortress/" + getRandomPartName(node.getType()));
					part.setRotation(getRotationInt(node.getRotation()));
					part.paste();
				}
				z++;
			}
			x++;
		}
		x = -1;
		for(List<FortressNode> nodeList : nodes) {
			int z = -1;
			for(FortressNode node : nodeList) {
				node.getClass();
				new NMSStructure(origin.clone().add(x*42+21,0,z*42+21), "end_fortress/end_fortress_b_cross_" + random.nextInt(2)).paste();
				new NMSStructure(origin.clone().add(x*42,0,z*42+21), "end_fortress/end_fortress_b_cross_" + random.nextInt(2)).paste();
				new NMSStructure(origin.clone().add(x*42+21,0,z*42), "end_fortress/end_fortress_b_cross_" + random.nextInt(2)).paste();
				z++;
			}
			x++;
		}
	}
	public String getRandomPartName(FortressNodeType type) {
		if(type == FortressNodeType.NODE_1WAY) return new String[] {"end_fortress_n_monument_" + random.nextInt(6), "end_fortress_n_parkour_" + random.nextInt(2)}[random.nextInt(2)]; 
		else if(type == FortressNodeType.NODE_2WAY) return "end_fortress_n_office_" + random.nextInt(2); 
		else throw new IllegalArgumentException();
	}
	private int getRotationInt(FortressPartRotation rot) {
		if(rot == FortressPartRotation.NONE) return 0;
		else if(rot == FortressPartRotation.CW_90_DEG) return 90;
		else if(rot == FortressPartRotation.R_180_DEG) return 180;
		return 270;
	}
}
