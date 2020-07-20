package com.dfsek.betterend.structures;

import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import com.dfsek.betterend.BetterEnd;
import org.bukkit.Location;

import com.dfsek.betterend.util.NMSReflectorUtil;

/**
 * Representation of Vanilla Structure Block structure.
 * 
 * @author dfsek
 * @since 2.0.0
 */
public class NMSStructure {
	private static final BetterEnd main = BetterEnd.getInstance();
	private int[] dimension;
	private Object structure;
	private Location origin;
	private int rotation = 0;
	private String name;
	private int permutation = 0;

	/**
	 * Load a structure from a packaged NBT structure file.
	 * 
	 * @author dfsek
	 * @since 2.0.0
	 * @param name
	 *          - The structure name.
	 * @param permutation
	 *          - the permutation of the structure to fetch.
	 */
	public NMSStructure(Location origin, String name, int permutation) {
		Object structure;
		try {
			structure = NMSReflectorUtil.definedStructureConstructor.newInstance();
			NMSReflectorUtil.loadStructure.invoke(structure, NMSReflectorUtil.loadNBTStreamFromInputStream.invoke(NMSReflectorUtil.nbtStreamToolsClass,
					main.getResource("struc/" + name + "/" + name + "_" + permutation + ".nbt")));

			Object tag = NMSReflectorUtil.getStructureAsNBTMethod.invoke(structure, NMSReflectorUtil.compoundNBTConstructor.newInstance());
			this.dimension = new int[]{(int) NMSReflectorUtil.getNBTListItemMethod.invoke(NMSReflectorUtil.getNBTListMethod.invoke(tag, "size", 3), 0),
					(int) NMSReflectorUtil.getNBTListItemMethod.invoke(NMSReflectorUtil.getNBTListMethod.invoke(tag, "size", 3), 1),
					(int) NMSReflectorUtil.getNBTListItemMethod.invoke(NMSReflectorUtil.getNBTListMethod.invoke(tag, "size", 3), 2)};
			this.structure = structure;
			this.origin = origin;
			this.name = name;
			this.permutation = permutation;
		} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load a structure from a packaged NBT structure file sans permutation.
	 * 
	 * @author dfsek
	 * @since 3.1.0
	 * @param origin
	 *          - The origin location of the structure.
	 * @param name
	 *          - The structure name.
	 */
	public NMSStructure(Location origin, String name) {
		Object structure;
		try {
			structure = NMSReflectorUtil.definedStructureConstructor.newInstance();
			NMSReflectorUtil.loadStructure.invoke(structure,
					NMSReflectorUtil.loadNBTStreamFromInputStream.invoke(NMSReflectorUtil.nbtStreamToolsClass, main.getResource("struc/" + name + ".nbt")));

			Object tag = NMSReflectorUtil.getStructureAsNBTMethod.invoke(structure, NMSReflectorUtil.compoundNBTConstructor.newInstance());
			this.dimension = new int[]{(int) NMSReflectorUtil.getNBTListItemMethod.invoke(NMSReflectorUtil.getNBTListMethod.invoke(tag, "size", 3), 0),
					(int) NMSReflectorUtil.getNBTListItemMethod.invoke(NMSReflectorUtil.getNBTListMethod.invoke(tag, "size", 3), 1),
					(int) NMSReflectorUtil.getNBTListItemMethod.invoke(NMSReflectorUtil.getNBTListMethod.invoke(tag, "size", 3), 2)};
			this.structure = structure;
			this.origin = origin;
			this.name = name;
			this.permutation = -1;
		} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load a structure from an InputStream.
	 * 
	 * @author dfsek
	 * @since 3.5.0
	 * @param origin
	 *          - The origin location of the structure.
	 * @param file
	 *          - The FileInputStream from which to load the structure.
	 */
	public NMSStructure(Location origin, FileInputStream file) {
		Object structure;
		try {
			structure = NMSReflectorUtil.definedStructureConstructor.newInstance();
			NMSReflectorUtil.loadStructure.invoke(structure, NMSReflectorUtil.loadNBTStreamFromInputStream.invoke(NMSReflectorUtil.nbtStreamToolsClass, file));

			Object tag = NMSReflectorUtil.getStructureAsNBTMethod.invoke(structure, NMSReflectorUtil.compoundNBTConstructor.newInstance());
			this.dimension = new int[]{(int) NMSReflectorUtil.getNBTListItemMethod.invoke(NMSReflectorUtil.getNBTListMethod.invoke(tag, "size", 3), 0),
					(int) NMSReflectorUtil.getNBTListItemMethod.invoke(NMSReflectorUtil.getNBTListMethod.invoke(tag, "size", 3), 1),
					(int) NMSReflectorUtil.getNBTListItemMethod.invoke(NMSReflectorUtil.getNBTListMethod.invoke(tag, "size", 3), 2)};
			this.structure = structure;
			this.origin = origin;
			this.name = null;
			this.permutation = -1;
		} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the origin of a structure.
	 * 
	 * @author dfsek
	 * @since 2.0.0
	 * @return Location - The origin of the structure
	 */
	public Location getOrigin() {
		return this.origin;
	}

	public int getPermutation() {
		return this.permutation;
	}

	/**
	 * Gets the name of a structure.
	 * 
	 * @author dfsek
	 * @since 2.0.0
	 * @return String - The name of the structure
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the rotation of structure.
	 * 
	 * @author dfsek
	 * @since 2.0.0
	 * @param rotation
	 *          - The rotation (in degrees)
	 */
	public void setRotation(int rotation) {
		if(rotation % 90 != 0 || rotation > 360) throw new IllegalArgumentException("Invalid rotation provided. Rotation must be multiple of 90.");
		this.rotation = rotation;
	}

	/**
	 * Gets the dimensions of a structure.
	 * 
	 * @author dfsek
	 * @since 2.0.0
	 * @return int[] - The X, Y, and Z dimensions of the structure
	 */
	public int[] getDimensions() {
		return this.dimension;
	}

	/**
	 * Gets the rotation of a structure.
	 * 
	 * @author dfsek
	 * @since 2.0.0
	 * @return int - The rotation of the structure
	 */
	public int getRotation() {
		return this.rotation;
	}

	/**
	 * Gets the locations containing the structure.
	 * 
	 * @author dfsek
	 * @since 2.0.0
	 * @return Location[] - The top and bottom bounding locations.
	 */
	public Location[] getBoundingLocations() {
		switch(this.rotation) {
			case 0:
			case 360:
				return new Location[]{this.origin,
						new Location(this.origin.getWorld(), this.origin.getX() + this.getX(), this.origin.getY() + this.getY(), this.origin.getZ() + this.getZ())};
			case 90:
				return new Location[]{this.origin,
						new Location(this.origin.getWorld(), this.origin.getX() - this.getZ(), this.origin.getY() + this.getY(), this.origin.getZ() + this.getX())};
			case 180:
				return new Location[]{this.origin,
						new Location(this.origin.getWorld(), this.origin.getX() - this.getX(), this.origin.getY() + this.getY(), this.origin.getZ() - this.getZ())};
			case 270:
				return new Location[]{this.origin,
						new Location(this.origin.getWorld(), this.origin.getX() + this.getZ(), this.origin.getY() + this.getY(), this.origin.getZ() - this.getX())};
			default:
				throw new IllegalArgumentException("Invalid rotation provided. Rotation must be multiple of 90.");
		}
	}

	/**
	 * Gets the X dimension of a structure.
	 * 
	 * @author dfsek
	 * @since 2.0.0
	 * @return int - The X dimension of the structure
	 */
	public int getX() {
		return this.dimension[0];
	}

	/**
	 * Gets the Y dimension of a structure.
	 * 
	 * @author dfsek
	 * @since 2.0.0
	 * @return int - The Y dimension of the structure
	 */
	public int getY() {
		return this.dimension[1];
	}

	/**
	 * Gets the Z dimension of a structure.
	 * 
	 * @author dfsek
	 * @since 2.0.0
	 * @return int - The Z dimension of the structure
	 */
	public int getZ() {
		return this.dimension[2];
	}

	/**
	 * Pastes a structure into the world.
	 * 
	 * @author dfsek
	 * @since 2.0.0
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
			Object info = NMSReflectorUtil.setReflectionMethod.invoke(NMSReflectorUtil.definedStructureInfoConstructor.newInstance(),
					NMSReflectorUtil.enumBlockMirrorValueOfMethod.invoke(NMSReflectorUtil.enumBlockMirrorClass, "NONE"));
			info = NMSReflectorUtil.setRotationMethod.invoke(info, rot);
			info = NMSReflectorUtil.mysteryBooleanMethod.invoke(info, false);
			info = NMSReflectorUtil.chunkCoordIntPairMethod.invoke(info, NMSReflectorUtil.chunkCoordIntPairClass.cast(null));
			info = NMSReflectorUtil.mysteryBooleancMethod.invoke(info, false);
			info = NMSReflectorUtil.setRandomMethod.invoke(info, new Random());

			Object pos = NMSReflectorUtil.blockPositionConstructor.newInstance(this.origin.getBlockX(), this.origin.getBlockY(), this.origin.getBlockZ());
			try {
				if(NMSReflectorUtil.version.startsWith("v1_15")) {
					NMSReflectorUtil.pasteMethod.invoke(this.structure, world, pos, info);
				} else {
					NMSReflectorUtil.pasteMethod.invoke(this.structure, world, pos, info, new Random());
				}
			} catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
