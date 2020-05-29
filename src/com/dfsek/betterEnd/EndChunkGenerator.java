package com.dfsek.betterEnd;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class EndChunkGenerator extends ChunkGenerator {
	boolean doBiomeGlass = false;
	Main main = Main.getInstance();
	Material[] plants = {Material.GRASS, Material.TALL_GRASS, Material.LILY_OF_THE_VALLEY, Material.FERN, Material.AZURE_BLUET, Material.BLUE_ORCHID, Material.WITHER_ROSE};
	int[] weight = {702, 100, 30, 40, 30, 30, 3};
	public Material chooseOnWeight(Material[] items, int[] weights) {
		double completeWeight = 0.0;
		for (int weight : weights)
			completeWeight += weight;
		double r = Math.random() * completeWeight;
		double countWeight = 0.0;
		for (int i = 0; i < items.length; i++) {
			countWeight += weights[i];
			if (countWeight >= r)
				return items[i];
		}
		return null;
	}
	@Override
	public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(world.getSeed(), 4);
		ChunkData chunk = createChunkData(world);
		double totalChunkDistance2D = Math.sqrt(Math.pow(chunkX, 2)+Math.pow(chunkZ, 2));
		if(totalChunkDistance2D > 50) {
			int outNoise = main.getConfig().getInt("outer-islands.noise");
			boolean clouds = main.getConfig().getBoolean("aether.clouds.enable-clouds");
			boolean aetherCaveDec = main.getConfig().getBoolean("aether.cave-decoration");
			boolean endCaveDec = main.getConfig().getBoolean("outer-islands.cave-decoration");
			int cloudNoise = main.getConfig().getInt("aether.clouds.cloud-noise");
			int cloudHeight = main.getConfig().getInt("aether.clouds.cloud-height");
			for(int X = 0; X < 16; X++)
				for(int Z = 0; Z < 16; Z++) {
					switch(Main.getBiome(chunkX*16+X, chunkZ*16+Z, world.getSeed())) {
					case "END":
						for (int Y = world.getMaxHeight()-1; Y > 0; Y--) biome.setBiome(X, Y, Z, Biome.SMALL_END_ISLANDS);
						break;
					case "SHATTERED_END":
						for (int Y = world.getMaxHeight()-1; Y > 0; Y--) biome.setBiome(X, Y, Z, Biome.END_BARRENS);
						break;
					case "AETHER":
						for (int Y = world.getMaxHeight()-1; Y > 0; Y--) biome.setBiome(X, Y, Z, Biome.END_HIGHLANDS);
						break; 
					case "VOID":
						for (int Y = world.getMaxHeight()-1; Y > 0; Y--) biome.setBiome(X, Y, Z, Biome.END_MIDLANDS);
						break;
					} 
					double biomeNoiseLvl = Main.getBiomeNoise(chunkX*16+X, chunkZ*16+Z, world.getSeed());
					double totalDistance2D = Math.sqrt(Math.pow(chunkX*16+X, 2)+Math.pow(chunkZ*16+Z, 2));
					int height = (int) ((generator.noise((double) (chunkX*16+X)/24, (double) (chunkZ*16+Z)/24, 0.5D, 0.7D)-0.4))+main.getConfig().getInt("outer-islands.island-height");
					int biomeNoise = 0;
					if(biomeNoiseLvl < 0) {
						biomeNoise = (int) (biomeNoiseLvl*2.5*(generator.noise((double) (chunkX*16+X)/12, (double) (chunkZ*16+Z)/12, 0.5D, 0.7D)-0.4));
						height = (int) (biomeNoiseLvl*4*(generator.noise((double) (chunkX*16+X)/16, (double) (chunkZ*16+Z)/16, 0.5D, 0.7D)-0.4))+main.getConfig().getInt("outer-islands.island-height");
					}
					int yMin = 255, yMax = 0;
					double voidM = 1;
					if(biomeNoiseLvl > 0 && biomeNoiseLvl < 0.25) voidM = -4*biomeNoiseLvl+1;
					else if(biomeNoiseLvl > 0.25 && biomeNoiseLvl < 0.5) voidM = 0;
					else if(biomeNoiseLvl > 0.5 && biomeNoiseLvl < 0.625) voidM = 8*biomeNoiseLvl-4;
					if(totalDistance2D < 1050 && totalDistance2D > 975) {
						double noiseM = (-Math.pow(1.1, -totalDistance2D+1000))+1;
						if(noiseM > 1) noiseM = 1;
						if(noiseM < 0) noiseM = 0;
						yMin = (int) ((-52*voidM*noiseM*(generator.noise((double) (chunkX*16+X)/outNoise, (double) (chunkZ*16+Z)/outNoise, 0.1D, 0.55D)-0.4)+height+biomeNoise)+1);
						yMax = (int) ((6*voidM*noiseM*(generator.noise((double) (chunkX*16+X)/outNoise, (double) (chunkZ*16+Z)/outNoise, 0.1D, 0.55D)-0.4)+height-biomeNoise));
					} else if(totalDistance2D > 1050) {
						yMin = (int) (-52*voidM*(generator.noise((double) (chunkX*16+X)/outNoise, (double) (chunkZ*16+Z)/outNoise, 0.1D, 0.55D)-0.4)+height+biomeNoise)+1;
						yMax = (int) (6*voidM*(generator.noise((double) (chunkX*16+X)/outNoise, (double) (chunkZ*16+Z)/outNoise, 0.1D, 0.55D)-0.4)+height-biomeNoise);
					}
					if(biomeNoiseLvl > 0.45 && clouds) {
						double aetherLvl = -Math.pow(256, -biomeNoiseLvl+0.45)+1;
						if(aetherLvl < 0) aetherLvl = 0;
						int yMinc = 255, yMaxc = 0, yMinc2 = 255, yMaxc2 = 0;
						if(totalDistance2D < 1050 && totalDistance2D > 975) {
							double noiseM = (-Math.pow(1.1, -totalDistance2D+1000))+1;
							if(noiseM > 1) noiseM = 1;
							if(noiseM < 0) noiseM = 0;
							yMinc = (int) ((-5*noiseM*aetherLvl*(generator.noise((double) (chunkX*16+X)/cloudNoise, (double) (chunkZ*16+Z)/cloudNoise, 0.1D, 0.55D)-0.2)+cloudHeight)+1);
							yMaxc = (int) ((5*noiseM*aetherLvl*(generator.noise((double) (chunkX*16+X)/cloudNoise, (double) (chunkZ*16+Z)/cloudNoise, 0.1D, 0.55D)-0.2)+cloudHeight-1));
							yMinc2 = (int) ((-4*noiseM*aetherLvl*(generator.noise((double) ((chunkX*16+X)-1000)/cloudNoise, (double) ((chunkZ*16+Z)-1000)/cloudNoise, 0.1D, 0.55D)-0.125)+cloudHeight+8)+1);
							yMaxc2 = (int) ((4*noiseM*aetherLvl*(generator.noise((double) ((chunkX*16+X)-1000)/cloudNoise, (double) ((chunkZ*16+Z)-1000)/cloudNoise, 0.1D, 0.55D)-0.125)+cloudHeight+7));
						} else if(totalDistance2D > 1050) {
							yMinc = (int) (-5*aetherLvl*(generator.noise((double) (chunkX*16+X)/cloudNoise, (double) (chunkZ*16+Z)/cloudNoise, 0.1D, 0.55D)-0.15)+cloudHeight)+1;
							yMaxc = (int) (5*aetherLvl*(generator.noise((double) (chunkX*16+X)/cloudNoise, (double) (chunkZ*16+Z)/cloudNoise, 0.1D, 0.55D)-0.15)+cloudHeight-1);
							yMinc2 = (int) (-4*aetherLvl*(generator.noise((double) ((chunkX*16+X)-1000)/cloudNoise, (double) ((chunkZ*16+Z)-1000)/cloudNoise, 0.1D, 0.55D)-0.125)+cloudHeight+8)+1;
							yMaxc2 = (int) (4*aetherLvl*(generator.noise((double) ((chunkX*16+X)-1000)/cloudNoise, (double) ((chunkZ*16+Z)-1000)/cloudNoise, 0.1D, 0.55D)-0.125)+cloudHeight+7);
						}
						for (int Y = yMaxc; Y > yMinc; Y--) {
							chunk.setBlock(X, Y, Z, Material.WHITE_STAINED_GLASS);
						}
						for (int Y = yMaxc2; Y > yMinc2; Y--) {
							chunk.setBlock(X, Y, Z, Material.WHITE_STAINED_GLASS);
						}
					}
					for (int Y = yMax; Y > yMin; Y--) {
						if(Math.abs(generator.noise((double) (chunkX*16+X)/12D, (double) Y/12D, (double) (chunkZ*16+Z)/12D, 0.5D, 0.5D)) < 0.8) {
							if(biomeNoiseLvl > 0.5) {
								if(Y > yMax-2) {
									if(chunk.getBlockData(X, Y+1, Z).getMaterial() == Material.AIR) {
										chunk.setBlock(X, Y, Z, Material.GRASS_BLOCK);
										if(random.nextInt(100) < 40) {
											Material plant = chooseOnWeight(plants, weight);
											if(plant != Material.TALL_GRASS) {
												chunk.setBlock(X, Y+1, Z, plant);
											} else {
												chunk.setBlock(X, Y+1, Z, Material.TALL_GRASS);
												chunk.setBlock(X, Y+2, Z, main.getServer().createBlockData("minecraft:tall_grass[half=upper]"));
											}
										}
									} else chunk.setBlock(X, Y, Z, Material.DIRT);
								} else {
									Material ore = Material.STONE;
									if(random.nextInt(1000) < main.getConfig().getInt("aether.ores.ore-chance", 5) && main.getConfig().getBoolean("aether.ores.enable-ores", true)) {
										ore = chooseOreWeight();
									}
									chunk.setBlock(X, Y, Z, ore);
									if(chunk.getBlockData(X, Y+1, Z).getMaterial() == Material.AIR && random.nextInt(100) < 48 && aetherCaveDec) {
										switch(random.nextInt(6)) {
										case 0:
											if(random.nextInt(100) < 35)chunk.setBlock(X, Y+1, Z, Material.BROWN_MUSHROOM);
											break;
										case 1:
											if(random.nextInt(100) < 35)chunk.setBlock(X, Y+1, Z, Material.RED_MUSHROOM);
											break;
										case 2:
											chunk.setBlock(X, Y+1, Z, Material.STONE_SLAB);
											break;
										case 3:
											if(random.nextInt(100) < 25) chunk.setBlock(X, Y+1, Z, Material.COBBLESTONE_WALL);
											break;
										case 4:
											if(chunk.getBlockData(X, Y+2, Z).getMaterial() == Material.AIR && random.nextInt(100) < 20) {
												chunk.setBlock(X, Y+1, Z, Material.COBBLESTONE_WALL);
												chunk.setBlock(X, Y+2, Z, Material.IRON_BARS);
											}
											break;
										case 5:
											switch(random.nextInt(2)) {
											case 0:
												if(random.nextInt(100) < 50) chunk.setBlock(X, Y+1, Z, main.getServer().createBlockData("minecraft:stone_button[face=floor,facing=north]"));
												break;
											case 1:
												if(random.nextInt(100) < 50) chunk.setBlock(X, Y+1, Z, main.getServer().createBlockData("minecraft:stone_button[face=floor,facing=east]"));
												break;
											}
											break;
										}

									}

								}

							} else {
								chunk.setBlock(X, Y, Z, Material.END_STONE);
								if(Y < yMax-1 && random.nextInt(100) < 32 && chunk.getBlockData(X, Y+1, Z).getMaterial() == Material.AIR && chunk.getBlockData(X, Y, Z).getMaterial() == Material.END_STONE && endCaveDec) {
									switch(random.nextInt(3)) {
									case 0:
										chunk.setBlock(X, Y+1, Z, Material.END_STONE_BRICK_SLAB);
										break;
									case 1:
										if(random.nextInt(100) < 25) chunk.setBlock(X, Y+1, Z, Material.END_STONE_BRICK_WALL);
										break;
									case 2:
										if(chunk.getBlockData(X, Y+2, Z).getMaterial() == Material.AIR && random.nextInt(100) < 20) {
											chunk.setBlock(X, Y+1, Z, Material.END_STONE_BRICK_WALL);
											chunk.setBlock(X, Y+2, Z, Material.END_ROD);
										}
										break;
									}

								}
							}

						}
						if(chunk.getBlockData(X, Y, Z).getMaterial() == Material.AIR && chunk.getBlockData(X, Y+1, Z).getMaterial() == Material.END_STONE && random.nextInt(100) < 28 && endCaveDec) {
							if(Y >= yMin) {
								switch(random.nextInt(3)) {
								case 0:
									if(chunk.getBlockData(X, Y-1, Z).getMaterial() == Material.AIR && random.nextInt(100) < 20) {
										chunk.setBlock(X, Y, Z, Material.END_STONE_BRICK_WALL);
										chunk.setBlock(X, Y-1, Z, main.getServer().createBlockData("minecraft:end_rod[facing=down]"));
									}
									break;
								case 1:
									if(random.nextInt(100) < 25) chunk.setBlock(X, Y, Z, Material.END_STONE_BRICK_WALL);
									break;
								case 2:
									chunk.setBlock(X, Y, Z, main.getServer().createBlockData("minecraft:end_stone_brick_slab[type=top]"));
									break;
								}
							}
						}
						if(chunk.getBlockData(X, Y, Z).getMaterial() == Material.AIR && chunk.getBlockData(X, Y+1, Z).getMaterial() == Material.STONE && random.nextInt(100) < 28 && aetherCaveDec) {
							if(Y >= yMin) {
								switch(random.nextInt(4)) {
								case 0:
									if(chunk.getBlockData(X, Y-1, Z).getMaterial() == Material.AIR && random.nextInt(100) < 20) {
										chunk.setBlock(X, Y, Z, Material.COBBLESTONE_WALL);
										chunk.setBlock(X, Y-1, Z, Material.IRON_BARS);
									}
									break;
								case 1:
									if(random.nextInt(100) < 10) chunk.setBlock(X, Y, Z, main.getServer().createBlockData("minecraft:lantern[hanging=true]"));
									break;
								case 2:
									if(random.nextInt(100) < 25) chunk.setBlock(X, Y, Z, Material.COBBLESTONE_WALL);
									break;
								case 3:
									chunk.setBlock(X, Y, Z, main.getServer().createBlockData("minecraft:stone_slab[type=top]"));
									break;
								}
							}
						}
					}
					if(doBiomeGlass && totalDistance2D > 975) {
						switch(Main.getBiome(chunkX*16+X, chunkZ*16+Z, world.getSeed())) {
						case "END":
							chunk.setBlock(X, 0, Z, Material.RED_STAINED_GLASS);
							break;
						case "SHATTERED_END":
							chunk.setBlock(X, 0, Z, Material.GREEN_STAINED_GLASS);
							break;
						case "AETHER":
							chunk.setBlock(X, 0, Z, Material.BLUE_STAINED_GLASS);
							break; 
						case "VOID":
							chunk.setBlock(X, 0, Z, Material.ORANGE_STAINED_GLASS);
							break;
						} 
					}

				}

		} else {
			for(int X = 0; X < 16; X++) {
				for(int Z = 0; Z < 16; Z++) {
					for (int Y = -64; Y < 64; Y++) {
						double totalDistance3D = Math.sqrt(Math.pow(chunkX*16+X, 2)+Math.pow(chunkZ*16+Z, 2)+Math.pow(Y*3, 2));
						if(totalDistance3D*((generator.noise((double) (chunkX*16+X)/36, (double) (Y)/48, (double) (chunkZ*16+Z)/36, 0.1D, 0.55D)+2)/3) < 50) {
							if(Y > 0) {
								chunk.setBlock(X, (Y/6)+56, Z, Material.END_STONE);
							} else {
								chunk.setBlock(X, (Y)+56, Z, Material.END_STONE);
							}
						}
					}
				}
			}
		}
		return chunk;
	}
	public Material chooseOreWeight() {
		Material[] items = {Material.COAL_ORE, Material.IRON_ORE, Material.GOLD_ORE, Material.REDSTONE_ORE, Material.LAPIS_ORE, Material.DIAMOND_ORE, Material.EMERALD_ORE};
		int[] weights = {20, 15, 10, 10, 10, 4, 1};
		double completeWeight = 0.0;
		for (int weight : weights)
			completeWeight += weight;
		double r = Math.random() * completeWeight;
		double countWeight = 0.0;
		for (int i = 0; i < items.length; i++) {
			countWeight += weights[i];
			if (countWeight >= r)
				return items[i];
		}
		return null;
	}
	@Override
	public boolean shouldGenerateStructures() {
		return main.getConfig().getBoolean("outer-islands.generate-end-cities", false);
	}
	@Override
	public boolean shouldGenerateDecorations() {
		return true;
	}
	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		return Arrays.asList((BlockPopulator) new TreePopulator(), new StructurePopulator());
	}

}