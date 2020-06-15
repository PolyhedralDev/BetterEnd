package com.dfsek.betterend;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import com.dfsek.betterend.populators.EnvironmentPopulator;
import com.dfsek.betterend.populators.OrePopulator;
import com.dfsek.betterend.populators.StructurePopulator;

public class EndChunkGenerator extends ChunkGenerator {
	private Main main = Main.getInstance();
	private Material[] plants = {Material.GRASS, Material.TALL_GRASS, Material.LILY_OF_THE_VALLEY, Material.FERN, Material.AZURE_BLUET, Material.BLUE_ORCHID, Material.WITHER_ROSE};
	private int[] weight = {700, 100, 30, 40, 30, 30, 5};

	private int outNoise = main.getConfig().getInt("outer-islands.noise", 56);
	private boolean clouds = main.getConfig().getBoolean("aether.clouds.enable-clouds", true);
	private boolean aetherCaveDec = main.getConfig().getBoolean("aether.cave-decoration", true);
	private boolean endCaveDec = main.getConfig().getBoolean("outer-islands.cave-decoration", true);
	private boolean allAether = main.getConfig().getBoolean("all-aether", false);
	private int cloudNoise = main.getConfig().getInt("aether.clouds.cloud-noise", 36);
	private int cloudHeight = main.getConfig().getInt("aether.clouds.cloud-height", 128);
	private int baseH = main.getConfig().getInt("outer-islands.island-height", 64);
	private int biomeSize = main.getConfig().getInt("outer-islands.biome-size", 1024);
	private int heatNoise = main.getConfig().getInt("outer-islands.heat-noise", 512);
	private int topH = main.getConfig().getInt("outer-islands.height-multiplier.top", 6);
	private int bottomH = main.getConfig().getInt("outer-islands.height-multiplier.bottom", 52);
	private double landPercent = 1-((double) ((main.getConfig().getInt("outer-islands.island-threshold", 30))/50D));

	private Material chooseOnWeight(Material[] items, int[] weights) {
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

	@SuppressWarnings("deprecation")
	@Override
	public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(world.getSeed(), 4);
		SimplexOctaveGenerator biomeGenerator = new SimplexOctaveGenerator(world.getSeed(), 4);
		ChunkData chunk = createChunkData(world);
		double totalChunkDistance2D = Math.sqrt(Math.pow(chunkX, 2)+Math.pow(chunkZ, 2));
		if(totalChunkDistance2D > 50 || allAether) {
			for(int X = 0; X < 16; X++)
				for(int Z = 0; Z < 16; Z++) {
					//double biomeNoiseLvl = Main.getBiomeNoise(chunkX*16+X, chunkZ*16+Z, world.getSeed());
					double biomeNoiseLvl = (allAether) ? 1 : biomeGenerator.noise((double) (chunkX*16+X)/biomeSize, (double) (chunkZ*16+Z)/biomeSize, 0.5D, 0.5D);
					double heatNoiseLvl = biomeGenerator.noise((double) (chunkX*16+X)/heatNoise, (double) (chunkZ*16+Z)/heatNoise, 0.5D, 0.5D);
					double totalDistance2D = (chunkX*16+X > 1250 || chunkZ*16+Z > 1250)  ? totalDistance2D = 2000 : Math.sqrt(Math.pow(chunkX*16+X, 2)+Math.pow(chunkZ*16+Z, 2));

					if (biomeNoiseLvl < -0.5) biome.setBiome(X, Z, Biome.END_BARRENS);
					else if (biomeNoiseLvl < 0) biome.setBiome(X, Z, Biome.SMALL_END_ISLANDS);
					else if (biomeNoiseLvl < 0.5) biome.setBiome(X, Z, Biome.END_MIDLANDS);
					else biome.setBiome(X, Z, Biome.END_HIGHLANDS);
					if(biomeNoiseLvl > 0.45 && clouds) {
						double aetherLvl = -Math.pow(256, -biomeNoiseLvl+0.45)+1;
						if(aetherLvl < 0) aetherLvl = 0;
						int yMinc = 255, yMaxc = 0, yMinc2 = 255, yMaxc2 = 0;
						double c1Noise = generator.noise((double) (chunkX*16+X)/cloudNoise, (double) (chunkZ*16+Z)/cloudNoise, 0.1D, 0.55D);
						double c2Noise = generator.noise((double) ((chunkX*16+X)-1000)/cloudNoise, (double) ((chunkZ*16+Z)-1000)/cloudNoise, 0.1D, 0.55D);
						if(totalDistance2D < 1050 && totalDistance2D > 975) {
							double noiseM = (-Math.pow(1.1, -totalDistance2D+1000))+1;
							if(noiseM > 1) noiseM = 1;
							if(noiseM < 0) noiseM = 0;
							yMinc = (int) ((-5*noiseM*aetherLvl*(c1Noise-0.2)+cloudHeight)+1);
							yMaxc = (int) ((5*noiseM*aetherLvl*(c1Noise-0.2)+cloudHeight-1));
							yMinc2 = (int) ((-4*noiseM*aetherLvl*(c2Noise-0.125)+cloudHeight+8)+1);
							yMaxc2 = (int) ((4*noiseM*aetherLvl*(c2Noise-0.125)+cloudHeight+7));
						} else if(totalDistance2D > 1050) {
							yMinc = (int) (-5*aetherLvl*(c1Noise-0.15)+cloudHeight)+1;
							yMaxc = (int) (5*aetherLvl*(c1Noise-0.15)+cloudHeight-1);
							yMinc2 = (int) (-4*aetherLvl*(c2Noise-0.125)+cloudHeight+8)+1;
							yMaxc2 = (int) (4*aetherLvl*(c2Noise-0.125)+cloudHeight+7);
						}
						chunk.setRegion(X, yMinc, Z, X+1, yMaxc, Z+1, Material.WHITE_STAINED_GLASS);
						chunk.setRegion(X, yMinc2, Z, X+1, yMaxc2, Z+1, Material.WHITE_STAINED_GLASS);
					}

					if(allAether) totalDistance2D = 2000;
					int height = (int) ((generator.noise((double) (chunkX*16+X)/24, (double) (chunkZ*16+Z)/24, 0.5D, 0.7D)-landPercent))+baseH;
					int biomeNoise = 0;
					if(biomeNoiseLvl < 0) {
						biomeNoise = (int) (biomeNoiseLvl*2.5*(generator.noise((double) (chunkX*16+X)/12, (double) (chunkZ*16+Z)/12, 0.5D, 0.7D)-landPercent));
						height = (int) (biomeNoiseLvl*4*(generator.noise((double) (chunkX*16+X)/16, (double) (chunkZ*16+Z)/16, 0.5D, 0.7D)-landPercent))+baseH;
					}
					int yMin = 255, yMax = 0;
					double voidM = 1;
					if(biomeNoiseLvl > 0 && biomeNoiseLvl < 0.25) voidM = -4*biomeNoiseLvl+1;
					else if(biomeNoiseLvl > 0.25 && biomeNoiseLvl < 0.5) voidM = 0;
					else if(biomeNoiseLvl > 0.5 && biomeNoiseLvl < 0.625) voidM = 8*biomeNoiseLvl-4;
					double iNoise = generator.noise((double) (chunkX*16+X)/outNoise, (double) (chunkZ*16+Z)/outNoise, 0.1D, 0.55D);
					if(totalDistance2D < 1050 && totalDistance2D > 975) {
						double noiseM = (-Math.pow(1.1, -totalDistance2D+1000))+1;
						if(noiseM > 1) noiseM = 1;
						if(noiseM < 0) noiseM = 0;
						yMin = (int) ((-bottomH*voidM*noiseM*(iNoise-landPercent)+height+biomeNoise)+1);
						yMax = (int) ((topH*voidM*noiseM*(iNoise-landPercent)+height-biomeNoise));
					} else if(totalDistance2D > 1050) {
						yMin = (int) (-bottomH*voidM*(iNoise-landPercent)+height+biomeNoise)+1;
						yMax = (int) (topH*voidM*(iNoise-landPercent)+height-biomeNoise);
					}

					for (int Y = yMax; Y > yMin; Y--) {
						if(Math.abs(generator.noise((double) (chunkX*16+X)/12D, (double) Y/12D, (double) (chunkZ*16+Z)/12D, 0.5D, 0.5D)) < 0.8) {
							Material upOne = chunk.getBlockData(X, Y+1, Z).getMaterial();
							if(biomeNoiseLvl > 0.5) {
								if(Y > yMax-2) {
									if(upOne == Material.AIR) {
										if(heatNoiseLvl > -0.5) {
											chunk.setBlock(X, Y, Z, Material.GRASS_BLOCK);
										} else {											
											int type = random.nextInt(100);
											if(type < (-2*(heatNoiseLvl+0.5)*40)) {
												chunk.setBlock(X, Y, Z, Material.PODZOL);
											} else if(type < (-2*(heatNoiseLvl+0.5)*50)) {
												chunk.setBlock(X, Y, Z, Material.GRAVEL);
											} else if(type < (-2*(heatNoiseLvl+0.5)*60)) {
												chunk.setBlock(X, Y, Z, Material.COARSE_DIRT);
											} else chunk.setBlock(X, Y, Z, Material.GRASS_BLOCK);
										}
										if(heatNoiseLvl < -0.5 && random.nextInt(100) < -75*(heatNoiseLvl+0.5) && chunk.getBlockData(X, Y, Z).getMaterial() != Material.AIR) {
											chunk.setBlock(X, Y+1, Z, Material.SNOW);
										} else if(random.nextInt(100) < 40 && chunk.getBlockData(X, Y, Z).getMaterial() == Material.GRASS_BLOCK) {
											Material plant = chooseOnWeight(plants, weight);
											if(plant != Material.TALL_GRASS) {
												if(plant != Material.WITHER_ROSE || (heatNoiseLvl < -0.5 && (biomeNoiseLvl > 0.5 || allAether))) chunk.setBlock(X, Y+1, Z, plant);
											} else {
												chunk.setBlock(X, Y+1, Z, Material.TALL_GRASS);
												chunk.setBlock(X, Y+2, Z, main.getServer().createBlockData("minecraft:tall_grass[half=upper]"));
											}
										}
									} else chunk.setBlock(X, Y, Z, Material.DIRT);
								} else {
									chunk.setBlock(X, Y, Z, Material.STONE);
									if(upOne == Material.AIR && random.nextInt(100) < 48 && aetherCaveDec) {
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
										default:
										}

									}

								}

							} else {
								chunk.setBlock(X, Y, Z, Material.END_STONE);
								if(Y < yMax-1 && random.nextInt(100) < 32 && upOne == Material.AIR && chunk.getBlockData(X, Y, Z).getMaterial() == Material.END_STONE && endCaveDec) {
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
									default:
									}

								}
							}

						}
						Material currentBlock = chunk.getBlockData(X, Y, Z).getMaterial();
						if(currentBlock == Material.AIR && chunk.getBlockData(X, Y+1, Z).getMaterial() == Material.END_STONE && random.nextInt(100) < 28 && endCaveDec) {
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
								default:
								}
							}
						}
						if(currentBlock == Material.AIR && chunk.getBlockData(X, Y+1, Z).getMaterial() == Material.STONE && random.nextInt(100) < 28 && aetherCaveDec) {
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
								default:
								}
							}
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
	@Override
	public boolean shouldGenerateStructures() {
		return main.getConfig().getBoolean("outer-islands.generate-end-cities", false);
	}
	@Override
	public boolean shouldGenerateDecorations() {
		return true;
	}
	public boolean shouldGenerateMobs() {
		return false;
	}
	public boolean shouldGenerateCaves() {
		return false;
	}
	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		return Arrays.asList((BlockPopulator) new StructurePopulator(), new EnvironmentPopulator(), new OrePopulator());
	}

}