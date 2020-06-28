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

import com.dfsek.betterend.populators.CustomStructurePopulator;
import com.dfsek.betterend.populators.EnvironmentPopulator;
import com.dfsek.betterend.populators.OrePopulator;
import com.dfsek.betterend.populators.StructurePopulator;
import com.dfsek.betterend.util.ConfigUtil;
import com.dfsek.betterend.util.Util;

public class EndChunkGenerator extends ChunkGenerator {
	private Main main = Main.getInstance();
	private Material[] plants = {Material.GRASS, Material.TALL_GRASS, Material.LILY_OF_THE_VALLEY, Material.FERN, Material.AZURE_BLUET, Material.BLUE_ORCHID, Material.WITHER_ROSE, Material.SWEET_BERRY_BUSH, Material.DANDELION, Material.POPPY};
	private int[] weight = {670, 100, 30, 40, 30, 30, 5, 10, 10, 10};

	

	@SuppressWarnings("deprecation")
	@Override
	public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(world.getSeed(), 4);
		SimplexOctaveGenerator biomeGenerator = new SimplexOctaveGenerator(world.getSeed(), 4);
		ChunkData chunk = createChunkData(world);
		double totalChunkDistance2D = Math.sqrt(Math.pow(chunkX, 2)+Math.pow(chunkZ, 2));
		if(totalChunkDistance2D > 50 || ConfigUtil.ALL_AETHER) {
			for(int X = 0; X < 16; X++)
				for(int Z = 0; Z < 16; Z++) {
					double biomeNoiseLvl = (ConfigUtil.ALL_AETHER) ? 1 : biomeGenerator.noise((double) (chunkX*16+X)/ConfigUtil.BIOME_SIZE, (double) (chunkZ*16+Z)/ConfigUtil.BIOME_SIZE, 0.5D, 0.5D);
					double heatNoiseLvl = biomeGenerator.noise((double) (chunkX*16+X)/ConfigUtil.HEAT_NOISE, (double) (chunkZ*16+Z)/ConfigUtil.HEAT_NOISE, 0.5D, 0.5D);
					double totalDistance2D = (chunkX*16+X > 1250 || chunkZ*16+Z > 1250)  ? totalDistance2D = 2000 : Math.sqrt(Math.pow(chunkX*16+X, 2)+Math.pow(chunkZ*16+Z, 2));

					if (biomeNoiseLvl < -0.5) biome.setBiome(X, Z, Biome.END_BARRENS);
					else if(biomeNoiseLvl < 0) biome.setBiome(X, Z, Biome.SMALL_END_ISLANDS);
					else if(biomeNoiseLvl < 0.5) biome.setBiome(X, Z, Biome.END_MIDLANDS);
					else if(!ConfigUtil.OVERWORLD) biome.setBiome(X, Z, Biome.END_HIGHLANDS);
					else biome.setBiome(X, Z, Biome.PLAINS);
					if((biomeNoiseLvl > 0.45 || ConfigUtil.ALL_AETHER) && ConfigUtil.DO_CLOUDS) {
						double aetherLvl = -Math.pow(256, -biomeNoiseLvl+0.45)+1;
						if(aetherLvl < 0) aetherLvl = 0;
						if(ConfigUtil.ALL_AETHER) aetherLvl = 1;
						int yMinc = 255, yMaxc = 0, yMinc2 = 255, yMaxc2 = 0;
						double c1Noise = generator.noise((double) (chunkX*16+X)/ConfigUtil.CLOUD_NOISE, (double) (chunkZ*16+Z)/ConfigUtil.CLOUD_NOISE, 0.1D, 0.55D);
						double c2Noise = generator.noise((double) ((chunkX*16+X)-1000)/ConfigUtil.CLOUD_NOISE, (double) ((chunkZ*16+Z)-1000)/ConfigUtil.CLOUD_NOISE, 0.1D, 0.55D);
						if(totalDistance2D < 1050 && totalDistance2D > 975) {
							double noiseM = (-Math.pow(1.1, -totalDistance2D+1000))+1;
							if(noiseM > 1) noiseM = 1;
							if(noiseM < 0) noiseM = 0;
							if(ConfigUtil.ALL_AETHER) noiseM = 1;
							yMinc = (int) ((-5*noiseM*aetherLvl*(c1Noise-0.2)+ConfigUtil.CLOUD_HEIGHT)+1);
							yMaxc = (int) ((5*noiseM*aetherLvl*(c1Noise-0.2)+ConfigUtil.CLOUD_HEIGHT-1));
							yMinc2 = (int) ((-4*noiseM*aetherLvl*(c2Noise-0.125)+ConfigUtil.CLOUD_HEIGHT+8)+1);
							yMaxc2 = (int) ((4*noiseM*aetherLvl*(c2Noise-0.125)+ConfigUtil.CLOUD_HEIGHT+7));
						} else if(totalDistance2D > 1050) {
							yMinc = (int) (-5*aetherLvl*(c1Noise-0.15)+ConfigUtil.CLOUD_HEIGHT)+1;
							yMaxc = (int) (5*aetherLvl*(c1Noise-0.15)+ConfigUtil.CLOUD_HEIGHT-1);
							yMinc2 = (int) (-4*aetherLvl*(c2Noise-0.125)+ConfigUtil.CLOUD_HEIGHT+8)+1;
							yMaxc2 = (int) (4*aetherLvl*(c2Noise-0.125)+ConfigUtil.CLOUD_HEIGHT+7);
						}
						chunk.setRegion(X, yMinc, Z, X+1, yMaxc, Z+1, Material.WHITE_STAINED_GLASS);
						chunk.setRegion(X, yMinc2, Z, X+1, yMaxc2, Z+1, Material.WHITE_STAINED_GLASS);
					}

					if(ConfigUtil.ALL_AETHER) totalDistance2D = 2000;
					int height = (int) ((generator.noise((double) (chunkX*16+X)/24, (double) (chunkZ*16+Z)/24, 0.5D, 0.7D)-ConfigUtil.LAND_PERCENT))+ConfigUtil.ISLAND_HEIGHT;
					int biomeNoise = 0;
					if(biomeNoiseLvl < 0) {
						biomeNoise = (int) (biomeNoiseLvl*2.5*(generator.noise((double) (chunkX*16+X)/12, (double) (chunkZ*16+Z)/12, 0.5D, 0.7D)-ConfigUtil.LAND_PERCENT));
						height = (int) (biomeNoiseLvl*4*(generator.noise((double) (chunkX*16+X)/16, (double) (chunkZ*16+Z)/16, 0.5D, 0.7D)-ConfigUtil.LAND_PERCENT))+ConfigUtil.ISLAND_HEIGHT;
					}
					int yMin = 255, yMax = 0;
					double voidM = 1;
					if(biomeNoiseLvl > 0 && biomeNoiseLvl < 0.25) voidM = -4*biomeNoiseLvl+1;
					else if(biomeNoiseLvl > 0.25 && biomeNoiseLvl < 0.5) voidM = 0;
					else if(biomeNoiseLvl > 0.5 && biomeNoiseLvl < 0.625) voidM = 8*biomeNoiseLvl-4;
					double iNoise = generator.noise((double) (chunkX*16+X)/ConfigUtil.OUTER_END_NOISE, (double) (chunkZ*16+Z)/ConfigUtil.OUTER_END_NOISE, 0.1D, 0.55D);
					if(totalDistance2D < 1050 && totalDistance2D > 975) {
						double noiseM = (-Math.pow(1.1, -totalDistance2D+1000))+1;
						if(noiseM > 1) noiseM = 1;
						if(noiseM < 0) noiseM = 0;
						yMin = (int) ((-ConfigUtil.ISLAND_HEIGHT_MULTIPLIER_BOTTOM*voidM*noiseM*(iNoise-ConfigUtil.LAND_PERCENT)+height+biomeNoise)+1);
						yMax = (int) ((ConfigUtil.ISLAND_HEIGHT_MULTIPLIER_TOP*voidM*noiseM*(iNoise-ConfigUtil.LAND_PERCENT)+height-biomeNoise));
					} else if(totalDistance2D > 1050) {
						yMin = (int) (-ConfigUtil.ISLAND_HEIGHT_MULTIPLIER_BOTTOM*voidM*(iNoise-ConfigUtil.LAND_PERCENT)+height+biomeNoise)+1;
						yMax = (int) (ConfigUtil.ISLAND_HEIGHT_MULTIPLIER_TOP*voidM*(iNoise-ConfigUtil.LAND_PERCENT)+height-biomeNoise);
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
											Material plant = (Material) Util.chooseOnWeight(plants, weight);
											if(plant == Material.TALL_GRASS) {
												chunk.setBlock(X, Y+1, Z, Material.TALL_GRASS);
												chunk.setBlock(X, Y+2, Z, main.getServer().createBlockData("minecraft:tall_grass[half=upper]"));
											} else if(plant == Material.SWEET_BERRY_BUSH) { 
												if(heatNoiseLvl < -0.5 && (biomeNoiseLvl > 0.5 || ConfigUtil.ALL_AETHER)) chunk.setBlock(X, Y+1, Z, main.getServer().createBlockData("minecraft:sweet_berry_bush[age=" + (random.nextInt(2) + 2) + "]"));
											} else {
												if((plant != Material.WITHER_ROSE) || (heatNoiseLvl < -0.5 && (biomeNoiseLvl > 0.5 || ConfigUtil.ALL_AETHER))) chunk.setBlock(X, Y+1, Z, plant);
											}
										}
									} else chunk.setBlock(X, Y, Z, Material.DIRT);
								} else {
									chunk.setBlock(X, Y, Z, Material.STONE);
									if(upOne == Material.AIR && random.nextInt(100) < 36 && ConfigUtil.DO_AETHER_CAVE_DEC) {
										switch(random.nextInt(7)) {
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
											default:
											}
										case 6:
											if(heatNoiseLvl < -0.5 && random.nextInt(100) < 25) {
												chunk.setBlock(X, Y+1, Z, Material.COBWEB);
											}
											break;
										default:
										}

									}

								}

							} else {
								chunk.setBlock(X, Y, Z, Material.END_STONE);
								if(Y < yMax-1 && random.nextInt(100) < 32 && upOne == Material.AIR && chunk.getBlockData(X, Y, Z).getMaterial() == Material.END_STONE && ConfigUtil.DO_END_CAVE_DEC) {
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
						if(currentBlock == Material.AIR && chunk.getBlockData(X, Y+1, Z).getMaterial() == Material.END_STONE && random.nextInt(100) < 28 && ConfigUtil.DO_END_CAVE_DEC && Y >= yMin) {
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
						if(currentBlock == Material.AIR && chunk.getBlockData(X, Y+1, Z).getMaterial() == Material.STONE && random.nextInt(100) < 36 && ConfigUtil.DO_AETHER_CAVE_DEC && Y >= yMin) {
							switch(random.nextInt(5)) {
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
							case 4:
								if(heatNoiseLvl < -0.5 && random.nextInt(100) < 25) {
									chunk.setBlock(X, Y+1, Z, Material.COBWEB);
								}
								break;
							default:
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
		return !ConfigUtil.OVERWORLD;
	}
	public boolean shouldGenerateMobs() {
		return false;
	}
	public boolean shouldGenerateCaves() {
		return false;
	}
	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		if(Main.isPremium()) return Arrays.asList((BlockPopulator) new CustomStructurePopulator(), new StructurePopulator(), new EnvironmentPopulator(), new OrePopulator());
		else return Arrays.asList((BlockPopulator) new StructurePopulator(), new EnvironmentPopulator(), new OrePopulator());
	}

}