package com.dfsek.betterend.structures;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Bukkit;

@SuppressWarnings({"unchecked", "rawtypes"})
public class NMSReflectorUtil {
	public static String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
	public static Class definedStructureClass;
	public static Constructor definedStructureConstructor;
	public static Method loadStructure;
	public static Constructor compoundNBTConstructor;
	public static Class compoundNBTTagClass;
	public static Class listNBTTagClass;
	public static Method getStructureAsNBTMethod;
	public static Method getNBTListMethod;
	public static Method getNBTListItemMethod;
	public static Method loadNBTStreamFromInputStream;
	public static Method pasteMethod;
	public static Class nbtStreamToolsClass;
	public static Class generatorAccessClass;
	public static Class worldServerClass;
	public static Class blockPositionClass;
	public static Class definedStructureInfoClass;
	public static Class craftWorldClass;
	public static Class enumBlockRotationClass;
	public static Method enumBlockRotationValueOfMethod;
	public static Class enumBlockMirrorClass;
	public static Method enumBlockMirrorValueOfMethod;
	public static Method setReflectionMethod;
	public static Method setRotationMethod;
	public static Method mysteryBooleanMethod;
	public static Method mysteryBooleancMethod;
	public static Method setRandomMethod;
	public static Constructor definedStructureInfoConstructor;
	public static Method getCraftWorldHandleMethod;
	public static Class chunkCoordIntPairClass;
	public static Method chunkCoordIntPairMethod;
	public static Constructor blockPositionConstructor;
    
    public static void init(Logger logger) {
        try {
        	long start = System.nanoTime();
        	logger.info("Beginning reflections...");
        	craftWorldClass = Class.forName("org.bukkit.craftbukkit." + version + ".CraftWorld");
        	compoundNBTTagClass = Class.forName("net.minecraft.server." + version + ".NBTTagCompound");
        	generatorAccessClass = Class.forName("net.minecraft.server." + version + ".GeneratorAccess");
        	definedStructureInfoClass = Class.forName("net.minecraft.server." + version + ".DefinedStructureInfo");
        	worldServerClass = Class.forName("net.minecraft.server." + version + ".WorldServer");
        	blockPositionClass = Class.forName("net.minecraft.server." + version + ".BlockPosition");
            nbtStreamToolsClass = Class.forName("net.minecraft.server." + version + ".NBTCompressedStreamTools");
            loadNBTStreamFromInputStream = nbtStreamToolsClass.getMethod("a", InputStream.class);
            definedStructureClass = Class.forName("net.minecraft.server." + version + ".DefinedStructure");
            definedStructureConstructor = definedStructureClass.getConstructor();
            loadStructure = definedStructureClass.getMethod("b", compoundNBTTagClass);
            enumBlockRotationClass = Class.forName("net.minecraft.server." + version + ".EnumBlockRotation");
            enumBlockRotationValueOfMethod = enumBlockRotationClass.getMethod("valueOf", String.class);
            enumBlockMirrorClass = Class.forName("net.minecraft.server." + version + ".EnumBlockMirror");
            enumBlockMirrorValueOfMethod = enumBlockMirrorClass.getMethod("valueOf", String.class);
            compoundNBTConstructor = compoundNBTTagClass.getConstructor();
            listNBTTagClass = Class.forName("net.minecraft.server." + version + ".NBTTagList");
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
            pasteMethod = definedStructureClass.getMethod("a", generatorAccessClass, blockPositionClass, definedStructureInfoClass);
            logger.info("Finished reflections. Time elapsed: " + ((double) (System.nanoTime()-start))/1000000 + "ms");
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
