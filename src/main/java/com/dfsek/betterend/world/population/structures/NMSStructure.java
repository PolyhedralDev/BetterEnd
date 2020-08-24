package com.dfsek.betterend.world.population.structures;

import com.dfsek.betterend.BetterEnd;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

/**
 * Representation of Vanilla Structure Block structure.
 *
 * @author dfsek
 * @since 2.0.0
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class NMSStructure {

    private static final BetterEnd main = BetterEnd.getInstance();
    private static final String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    private static Constructor definedStructureConstructor;
    private static Method loadStructure;
    private static Constructor compoundNBTConstructor;
    private static Method getStructureAsNBTMethod;
    private static Method getNBTListMethod;
    private static Method getNBTListItemMethod;
    private static Method loadNBTStreamFromInputStreamMethod;
    private static Method pasteMethod;
    private static Class nbtStreamToolsClass;
    private static Class craftWorldClass;
    private static Class enumBlockRotationClass;
    private static Method enumBlockRotationValueOfMethod;
    private static Class enumBlockMirrorClass;
    private static Method enumBlockMirrorValueOfMethod;
    private static Method setReflectionMethod;
    private static Method setRotationMethod;
    private static Method mysteryBooleanMethod;
    private static Method mysteryBooleancMethod;
    private static Method setRandomMethod;
    private static Constructor definedStructureInfoConstructor;
    private static Method getCraftWorldHandleMethod;
    private static Class chunkCoordIntPairClass;
    private static Method chunkCoordIntPairMethod;
    private static Constructor blockPositionConstructor;

    static {
        try {
            long start = System.nanoTime();
            main.getLogger().info("Beginning reflections for net.minecraft.server." + version + ".");
            craftWorldClass = Class.forName("org.bukkit.craftbukkit." + version + ".CraftWorld");
            Class compoundNBTTagClass = Class.forName("net.minecraft.server." + version + ".NBTTagCompound");
            Class definedStructureInfoClass = Class.forName("net.minecraft.server." + version + ".DefinedStructureInfo");
            Class blockPositionClass = Class.forName("net.minecraft.server." + version + ".BlockPosition");
            nbtStreamToolsClass = Class.forName("net.minecraft.server." + version + ".NBTCompressedStreamTools");
            loadNBTStreamFromInputStreamMethod = nbtStreamToolsClass.getMethod("a", InputStream.class);
            Class definedStructureClass = Class.forName("net.minecraft.server." + version + ".DefinedStructure");
            definedStructureConstructor = definedStructureClass.getConstructor();
            loadStructure = definedStructureClass.getMethod("b", compoundNBTTagClass);
            enumBlockRotationClass = Class.forName("net.minecraft.server." + version + ".EnumBlockRotation");
            enumBlockRotationValueOfMethod = enumBlockRotationClass.getMethod("valueOf", String.class);
            enumBlockMirrorClass = Class.forName("net.minecraft.server." + version + ".EnumBlockMirror");
            enumBlockMirrorValueOfMethod = enumBlockMirrorClass.getMethod("valueOf", String.class);
            compoundNBTConstructor = compoundNBTTagClass.getConstructor();
            Class listNBTTagClass = Class.forName("net.minecraft.server." + version + ".NBTTagList");
            getNBTListMethod = compoundNBTTagClass.getMethod("getList", String.class, int.class);
            getNBTListItemMethod = listNBTTagClass.getMethod("e", int.class);
            blockPositionConstructor = blockPositionClass.getConstructor(int.class, int.class, int.class);
            chunkCoordIntPairClass = Class.forName("net.minecraft.server." + version + ".ChunkCoordIntPair");
            chunkCoordIntPairMethod = definedStructureInfoClass.getMethod("a", chunkCoordIntPairClass);
            mysteryBooleanMethod = definedStructureInfoClass.getMethod("a", boolean.class);
            mysteryBooleancMethod = definedStructureInfoClass.getMethod("c", boolean.class);
            setRandomMethod = definedStructureInfoClass.getMethod("a", Random.class);
            getCraftWorldHandleMethod = craftWorldClass.getMethod("getHandle");
            getStructureAsNBTMethod = definedStructureClass.getMethod("a", compoundNBTTagClass);
            setRotationMethod = definedStructureInfoClass.getMethod("a", enumBlockRotationClass);
            setReflectionMethod = definedStructureInfoClass.getMethod("a", enumBlockMirrorClass);
            definedStructureInfoConstructor = definedStructureInfoClass.getConstructor();
            Class generatorAccessClass;
            if(version.startsWith("v1_16_R2")) {
                generatorAccessClass = Class.forName("net.minecraft.server." + version + ".WorldAccess");
            } else {
                generatorAccessClass = Class.forName("net.minecraft.server." + version + ".GeneratorAccess");
            }
            if (version.startsWith("v1_15")) {
                pasteMethod = definedStructureClass.getMethod("a", generatorAccessClass, blockPositionClass, definedStructureInfoClass);
            } else {
                pasteMethod = definedStructureClass.getMethod("a", generatorAccessClass, blockPositionClass, definedStructureInfoClass, Random.class);
            }
            BetterEnd.getInstance().getLogger().info("Finished reflections. Time elapsed: " + ((double) (System.nanoTime() - start)) / 1000000 + "ms");
        } catch (ClassNotFoundException | NoSuchMethodException e) {
			main.getLogger().severe("An error occurred whilst initializing Reflection. Please report this.");
			e.printStackTrace();
			main.getLogger().severe("Report the above error to BetterEnd at https://github.com/dfsek/BetterEnd-Public/issues");
            main.getLogger().severe("This is most likely caused by running the plugin on an unsupported version.");
        }
    }

    private int[] dimension;
    private Object structure;
    private Location origin;
    private int rotation = 0;
    private String name;
    private int permutation = 0;

    /**
     * Load a structure from a packaged NBT structure file.
     *
     * @param name        - The structure name.
     * @param permutation - the permutation of the structure to fetch.
     * @author dfsek
     * @since 2.0.0
     */
    public NMSStructure(Location origin, String name, int permutation) {
        Object structure;
        try {
            structure = definedStructureConstructor.newInstance();
            loadStructure.invoke(structure, loadNBTStreamFromInputStreamMethod.invoke(nbtStreamToolsClass,
                    main.getResource("struc/" + name + "/" + name + "_" + permutation + ".nbt")));

            Object tag = getStructureAsNBTMethod.invoke(structure, compoundNBTConstructor.newInstance());
            this.dimension = new int[]{(int) getNBTListItemMethod.invoke(getNBTListMethod.invoke(tag, "size", 3), 0),
                    (int) getNBTListItemMethod.invoke(getNBTListMethod.invoke(tag, "size", 3), 1),
                    (int) getNBTListItemMethod.invoke(getNBTListMethod.invoke(tag, "size", 3), 2)};
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
     *
     * @param origin - The origin location of the structure.
     * @param name   - The structure name.
     * @author dfsek
     * @since 3.1.0
     */
    public NMSStructure(Location origin, String name) {
        Object structure;
        try {
            structure = definedStructureConstructor.newInstance();
            loadStructure.invoke(structure,
                    loadNBTStreamFromInputStreamMethod.invoke(nbtStreamToolsClass, main.getResource("struc/" + name + ".nbt")));

            Object tag = getStructureAsNBTMethod.invoke(structure, compoundNBTConstructor.newInstance());
            this.dimension = new int[]{(int) getNBTListItemMethod.invoke(getNBTListMethod.invoke(tag, "size", 3), 0),
                    (int) getNBTListItemMethod.invoke(getNBTListMethod.invoke(tag, "size", 3), 1),
                    (int) getNBTListItemMethod.invoke(getNBTListMethod.invoke(tag, "size", 3), 2)};
            this.structure = structure;
            this.origin = origin;
            this.name = name;
            this.permutation = -1;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load a structure from an InputStream.
     *
     * @param origin - The origin location of the structure.
     * @param file   - The FileInputStream from which to load the structure.
     * @author dfsek
     * @since 3.5.0
     */
    public NMSStructure(Location origin, FileInputStream file) {
        Object structure;
        try {
            structure = definedStructureConstructor.newInstance();
            loadStructure.invoke(structure, loadNBTStreamFromInputStreamMethod.invoke(nbtStreamToolsClass, file));

            Object tag = getStructureAsNBTMethod.invoke(structure, compoundNBTConstructor.newInstance());
            this.dimension = new int[]{(int) getNBTListItemMethod.invoke(getNBTListMethod.invoke(tag, "size", 3), 0),
                    (int) getNBTListItemMethod.invoke(getNBTListMethod.invoke(tag, "size", 3), 1),
                    (int) getNBTListItemMethod.invoke(getNBTListMethod.invoke(tag, "size", 3), 2)};
            this.structure = structure;
            this.origin = origin;
            this.name = null;
            this.permutation = -1;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the class, use to initialize reflections before generation begins.
     * @author dfsek
     * @since 3.6.5
     */
    public static void load() {}

    /**
     * Gets the origin of a structure.
     *
     * @return Location - The origin of the structure
     * @author dfsek
     * @since 2.0.0
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
     * @return String - The name of the structure
     * @author dfsek
     * @since 2.0.0
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the dimensions of a structure.
     *
     * @return int[] - The X, Y, and Z dimensions of the structure
     * @author dfsek
     * @since 2.0.0
     */
    public int[] getDimensions() {
        return this.dimension;
    }

    /**
     * Gets the rotation of a structure.
     *
     * @return int - The rotation of the structure
     * @author dfsek
     * @since 2.0.0
     */
    public int getRotation() {
        return this.rotation;
    }

    /**
     * Sets the rotation of structure.
     *
     * @param rotation - The rotation (in degrees)
     * @author dfsek
     * @since 2.0.0
     */
    public void setRotation(int rotation) {
        if (rotation % 90 != 0 || rotation > 360)
            throw new IllegalArgumentException("Invalid rotation provided. Rotation must be multiple of 90.");
        this.rotation = rotation;
    }

    /**
     * Gets the locations containing the structure.
     *
     * @return Location[] - The top and bottom bounding locations.
     * @author dfsek
     * @since 2.0.0
     */
    public Location[] getBoundingLocations() {
        switch (this.rotation) {
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
     * @return int - The X dimension of the structure
     * @author dfsek
     * @since 2.0.0
     */
    public int getX() {
        return this.dimension[0];
    }

    /**
     * Gets the Y dimension of a structure.
     *
     * @return int - The Y dimension of the structure
     * @author dfsek
     * @since 2.0.0
     */
    public int getY() {
        return this.dimension[1];
    }

    /**
     * Gets the Z dimension of a structure.
     *
     * @return int - The Z dimension of the structure
     * @author dfsek
     * @since 2.0.0
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
            switch (this.rotation) {
                case 0:
                case 360:
                    rot = enumBlockRotationValueOfMethod.invoke(enumBlockRotationClass, "NONE");
                    break;
                case 90:
                    rot = enumBlockRotationValueOfMethod.invoke(enumBlockRotationClass, "CLOCKWISE_90");
                    break;
                case 180:
                    rot = enumBlockRotationValueOfMethod.invoke(enumBlockRotationClass, "CLOCKWISE_180");
                    break;
                case 270:
                    rot = enumBlockRotationValueOfMethod.invoke(enumBlockRotationClass, "COUNTERCLOCKWISE_90");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid rotation provided. Rotation must be multiple of 90.");
            }

            Object world = getCraftWorldHandleMethod.invoke(craftWorldClass.cast(this.origin.getWorld()));
            Object info = setReflectionMethod.invoke(definedStructureInfoConstructor.newInstance(),
                    enumBlockMirrorValueOfMethod.invoke(enumBlockMirrorClass, "NONE"));
            info = setRotationMethod.invoke(info, rot);
            info = mysteryBooleanMethod.invoke(info, false);
            info = chunkCoordIntPairMethod.invoke(info, chunkCoordIntPairClass.cast(null));
            info = mysteryBooleancMethod.invoke(info, false);
            info = setRandomMethod.invoke(info, new Random());

            Object pos = blockPositionConstructor.newInstance(this.origin.getBlockX(), this.origin.getBlockY(), this.origin.getBlockZ());
            try {
                if (version.startsWith("v1_15")) {
                    pasteMethod.invoke(this.structure, world, pos, info);
                } else {
                    pasteMethod.invoke(this.structure, world, pos, info, new Random());
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
