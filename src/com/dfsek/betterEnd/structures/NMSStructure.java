package com.dfsek.betterend.structures;

import java.io.IOException;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;

import com.dfsek.betterend.Main;

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
	private Location origin;
	private int rotation = 0;
	private String name;
	private int permutation = 0;
	/**
	 * Load a structure from a packaged NBT structure file.
	 * @param name - The structure name
	 * @param permutation - the permutation of the structure to fetch
	 * @return NMSStructure - The structure object
	 */
	public NMSStructure(Location origin, String name, int permutation) {
		DefinedStructure structure = new DefinedStructure();
		try {
			structure.b(NBTCompressedStreamTools.a(main.getResource("struc/" + name + "/" + name + "_" + permutation + ".nbt"))); //Load structure from packaged NBT file in /struc/*/*.nbt
		} catch (IOException e) {
			throw new IllegalArgumentException("Structure with name \"" + name + "\" could not be found.");
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Structure with name \"" + name + "\" and permutation \"" + permutation + "\" could not be found.");
		}
		NBTTagCompound tag = structure.a(new NBTTagCompound());
        this.dimension = new int[] {tag.getList("size", 3).e(0), tag.getList("size", 3).e(1), tag.getList("size", 3).e(2)};
		this.structure = structure;
		this.origin = origin;
		this.name = name;
		this.permutation = permutation;
	}
	
	public NMSStructure(Location origin, String name) {
		DefinedStructure structure = new DefinedStructure();
		try {
			structure.b(NBTCompressedStreamTools.a(main.getResource("struc/" + name + ".nbt"))); //Load structure from packaged NBT file in /struc/*/*.nbt
		} catch (NullPointerException | IOException e) {
			throw new IllegalArgumentException("Structure with name \"" + name + "\" could not be found.");
		}
		NBTTagCompound tag = structure.a(new NBTTagCompound());
        this.dimension = new int[] {tag.getList("size", 3).e(0), tag.getList("size", 3).e(1), tag.getList("size", 3).e(2)};
		this.structure = structure;
		this.origin = origin;
		this.name = name;
	}

	/**
	 * Gets origin of a structure.
	 * @return Location - The origin of the structure
	 */
	public Location getOrigin() {
        return this.origin;
	}
	
	public int getPermutation() {
        return this.permutation;
	}
	
	/**
	 * Gets name of a structure.
	 * @return String - The name of the structure
	 */
	public String getName() {
        return this.name;
	}
	
	/**
	 * Sets rotation of structure.
	 * @param rotation - The rotation (in degrees)
	 */
	public void setRotation(int rotation) {
		if(rotation % 90 != 0 || rotation > 360) throw new IllegalArgumentException("Invalid rotation provided. Rotation must be multiple of 90.");
        this.rotation = rotation;
	}
	
	/**
	 * Gets dimensions of a structure.
	 * @return int[] - The X, Y, and Z dimensions of the structure
	 */
	public int[] getDimensions() {
        return this.dimension;
	}
	
	/**
	 * Gets rotation of a structure.
	 * @return int - The rotation of the structure
	 */
	public int getRotation() {
        return this.rotation;
	}
	
	public Location[] getBoundingLocations() {
		switch(this.rotation) {
		case 0:
		case 360:
			return new Location[] {this.origin, new Location(this.origin.getWorld(), this.origin.getX() + this.getX(), this.origin.getY() + this.getY(), this.origin.getZ() + this.getZ())};
		case 90:
			return new Location[] {this.origin, new Location(this.origin.getWorld(), this.origin.getX() - this.getZ(), this.origin.getY() + this.getY(), this.origin.getZ() + this.getX())};
		case 180:
			return new Location[] {this.origin, new Location(this.origin.getWorld(), this.origin.getX() - this.getX(), this.origin.getY() + this.getY(), this.origin.getZ() - this.getZ())};
		case 270:
			return new Location[] {this.origin, new Location(this.origin.getWorld(), this.origin.getX() + this.getZ(), this.origin.getY() + this.getY(), this.origin.getZ() - this.getX())};
		default:
			throw new IllegalArgumentException("Invalid rotation provided. Rotation must be multiple of 90.");
		}
		
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
	 */
	public void paste() {
		EnumBlockRotation rotationBM;
		switch(this.rotation) {
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
	    WorldServer world = ((CraftWorld) this.origin.getWorld()).getHandle();
	    DefinedStructureInfo structInfo = new DefinedStructureInfo().a(EnumBlockMirror.NONE).a(rotationBM).a(false).a((ChunkCoordIntPair) null).c(false).a(new Random());
	    this.structure.a(world, new BlockPosition(this.origin.getBlockX(), this.origin.getBlockY(), this.origin.getBlockZ()), structInfo);
	}
}

