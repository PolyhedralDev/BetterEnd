package com.dfsek.betterEnd.structures;

import java.io.IOException;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;

import com.dfsek.betterEnd.Main;

import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.ChunkCoordIntPair;
import net.minecraft.server.v1_15_R1.DefinedStructure;
import net.minecraft.server.v1_15_R1.DefinedStructureInfo;
import net.minecraft.server.v1_15_R1.EnumBlockMirror;
import net.minecraft.server.v1_15_R1.EnumBlockRotation;
import net.minecraft.server.v1_15_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.WorldServer;

public class NMSStructure {
	private static Main main = Main.getInstance();
	private int[] dimension;
	public DefinedStructure structure;
	
	/**
	 * Load a structure from a packaged NBT structure file.
	 * @param name - The structure name
	 * @param permutation - the permutation of the structure to fetch
	 * @return NMSStructure - The structure object
	 */
	public NMSStructure(String name, int permutation) throws IOException {
		DefinedStructure structure = new DefinedStructure();
		structure.b(NBTCompressedStreamTools.a(main.getResource("struc/" + name + "/" + name + "_" + permutation + ".nbt"))); //Load structure from packaged NBT file in /struc/*/*.nbt
		NBTTagCompound tag = structure.a(new NBTTagCompound());
        this.dimension = new int[] {tag.getList("size", 3).e(0), tag.getList("size", 3).e(1), tag.getList("size", 3).e(2)};
		this.structure = structure;
	}
	
	/**
	 * Gets dimensions of a structure.
	 * @return int[] - The X, Y, and Z dimensions of the structure
	 */
	public int[] getDimensions() {
        return this.dimension;
	}
	
	/**
	 * Gets X dimension of a structure.
	 * @return int - The X dimension of the structure
	 */
	public int getX() {
		return this.dimension[0];
	}
	
	/**
	 * Gets Y dimension of a structure.
	 * @return int - The Y dimension of the structure
	 */
	public int getY() {
		return this.dimension[1];
	}
	
	/**
	 * Gets Z dimension of a structure.
	 * @return int - The Z dimension of the structure
	 */
	public int getZ() {
		return this.dimension[2];
	}
	
	/**
	 * Pastes a structure into the world.
	 * @param startEdge - The location at which to paste
	 * @param rotation - The rotation of the structure
	 */
	public void paste(Location startEdge, int rotation) {
		EnumBlockRotation rotationBM;
		switch(rotation) {
		case 0:
		case 360:
			rotationBM = EnumBlockRotation.NONE;
			break;
		case 90:
			rotationBM = EnumBlockRotation.CLOCKWISE_90;
			break;
		case 180:
			rotationBM = EnumBlockRotation.CLOCKWISE_180;
			break;
		case 270:
			rotationBM = EnumBlockRotation.COUNTERCLOCKWISE_90;
			break;
		default:
			throw new IllegalArgumentException("Invalid rotation provided. Rotation must be multiple of 90.");
		}
	    WorldServer world = ((CraftWorld) startEdge.getWorld()).getHandle();
	    DefinedStructureInfo structInfo = new DefinedStructureInfo().a(EnumBlockMirror.NONE).a(rotationBM).a(false).a((ChunkCoordIntPair) null).c(false).a(new Random());
	    // false sets ignore entities to false (so it does NOT ignore them)
	    // 1.0f sets the amount of to be pasted blocks to 100%
	    // mirror & rotation are self explaining
	    // the block does the thing like the block does, which can be set in the ingame structure block GUI
	    // no idea at the moment, what the coord pair, the random and the second false does
	    // If you want to find out more about it, see net.minecraft.server.v1_13_R1.TileEntityStructure.c(boolean), there mojang calls the method and compare it with the ingame GUI
	    this.structure.a(world, new BlockPosition(startEdge.getBlockX(), startEdge.getBlockY(), startEdge.getBlockZ()), structInfo);
	}
}
