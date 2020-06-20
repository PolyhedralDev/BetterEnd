package com.dfsek.betterend.structures;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import org.bukkit.Location;

import com.dfsek.betterend.Main;

public class NMSStructure {
	private static Main main = Main.getInstance();
	private int[] dimension;
	private Object structure;
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
		Object structure;
		try {
			structure = NMSReflectorUtil.definedStructureConstructor.newInstance();
			NMSReflectorUtil.loadStructure.invoke(structure, NMSReflectorUtil.loadNBTStreamFromInputStream.invoke(NMSReflectorUtil.nbtStreamToolsClass, main.getResource("struc/" + name + "/" + name + "_" + permutation + ".nbt")));

			Object tag = NMSReflectorUtil.getStructureAsNBTMethod.invoke(structure, NMSReflectorUtil.compoundNBTConstructor.newInstance());
			this.dimension = new int[] {(int) NMSReflectorUtil.getNBTListItemMethod.invoke(NMSReflectorUtil.getNBTListMethod.invoke(tag, "size", 3), 0),
					(int) NMSReflectorUtil.getNBTListItemMethod.invoke(NMSReflectorUtil.getNBTListMethod.invoke(tag, "size", 3), 1), 
					(int) NMSReflectorUtil.getNBTListItemMethod.invoke(NMSReflectorUtil.getNBTListMethod.invoke(tag, "size", 3), 2)};
			this.structure = structure;
			this.origin = origin;
			this.name = name;
			this.permutation = permutation;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}


	}
	
	/**
	 * Load a structure from a packaged NBT structure file sans permutation.
	 * @param name - The structure name
	 * @return NMSStructure - The structure object
	 */
	public NMSStructure(Location origin, String name) {
		Object structure;
		try {
			structure = NMSReflectorUtil.definedStructureConstructor.newInstance();
			NMSReflectorUtil.loadStructure.invoke(structure, NMSReflectorUtil.loadNBTStreamFromInputStream.invoke(NMSReflectorUtil.nbtStreamToolsClass, main.getResource("struc/" + name  + ".nbt")));

			Object tag = NMSReflectorUtil.getStructureAsNBTMethod.invoke(structure, NMSReflectorUtil.compoundNBTConstructor.newInstance());
			this.dimension = new int[] {(int) NMSReflectorUtil.getNBTListItemMethod.invoke(NMSReflectorUtil.getNBTListMethod.invoke(tag, "size", 3), 0),
					(int) NMSReflectorUtil.getNBTListItemMethod.invoke(NMSReflectorUtil.getNBTListMethod.invoke(tag, "size", 3), 1), 
					(int) NMSReflectorUtil.getNBTListItemMethod.invoke(NMSReflectorUtil.getNBTListMethod.invoke(tag, "size", 3), 2)};
			this.structure = structure;
			this.origin = origin;
			this.name = name;
			this.permutation = -1;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
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
		try {
			Object rot;
			switch(this.rotation) {
			case 0:
			case 360:
				rot = NMSReflectorUtil.enumBlockRotationValueOfMethod.invoke(NMSReflectorUtil.enumBlockRotationClass, "NONE");
				break;
			case 90:
				rot = NMSReflectorUtil.enumBlockRotationValueOfMethod.invoke(NMSReflectorUtil.enumBlockRotationClass, "CLOCKWISE_90");
				break;
			case 180:
				rot = NMSReflectorUtil.enumBlockRotationValueOfMethod.invoke(NMSReflectorUtil.enumBlockRotationClass, "CLOCKWISE_180");
				break;
			case 270:
				rot = NMSReflectorUtil.enumBlockRotationValueOfMethod.invoke(NMSReflectorUtil.enumBlockRotationClass, "COUNTERCLOCKWISE_90");
				break;
			default:
				throw new IllegalArgumentException("Invalid rotation provided. Rotation must be multiple of 90.");
			}

			Object world = NMSReflectorUtil.getCraftWorldHandleMethod.invoke(NMSReflectorUtil.craftWorldClass.cast(this.origin.getWorld()));
			Object info = NMSReflectorUtil.setReflectionMethod.invoke(NMSReflectorUtil.definedStructureInfoConstructor.newInstance(), NMSReflectorUtil.enumBlockMirrorValueOfMethod.invoke(NMSReflectorUtil.enumBlockMirrorClass, "NONE"));
			info = NMSReflectorUtil.setRotationMethod.invoke(info, rot);
			info = NMSReflectorUtil.mysteryBooleanMethod.invoke(info, false);
			info = NMSReflectorUtil.chunkCoordIntPairMethod.invoke(info, NMSReflectorUtil.chunkCoordIntPairClass.cast(null));
			info = NMSReflectorUtil.mysteryBooleancMethod.invoke(info, false);
			info = NMSReflectorUtil.setRandomMethod.invoke(info, new Random());
			
			
			Object pos = NMSReflectorUtil.blockPositionConstructor.newInstance(this.origin.getBlockX(), this.origin.getBlockY(), this.origin.getBlockZ());
			try {
				NMSReflectorUtil.pasteMethod.invoke(this.structure, world, pos, info);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}

