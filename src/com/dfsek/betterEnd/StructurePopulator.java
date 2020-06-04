package com.dfsek.betterEnd;

import java.io.IOException;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import com.dfsek.betterEnd.structures.NMSStructure;


public class StructurePopulator extends BlockPopulator {
	static Main main = Main.getInstance();
	@Override
	public void populate(World world, Random random, Chunk chunk) {
		if(chunk.getX() == 0 && chunk.getZ() == 0) {
			try {
				NMSStructure structure = new NMSStructure("gold_dungeon", 0);
				int[] dimension = structure.getDimensions();
				//DefinedStructure structure = NMSStructure.loadStructure("gold_dungeon", 0); //Load structure from packaged file
				//int[] dimension = NMSStructure.getStructureDimensions(structure);
		        System.out.println("[BetterEnd] X: "+  dimension[0] + ", Y: " + dimension[1] + ", Z: " + dimension[2]);
		        //NMSStructure.pasteStructure(structure, new Location(world, 15, 100, 15), EnumBlockRotation.NONE);
		        structure.paste(new Location(world, 0, 58, 0), 90);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
}
