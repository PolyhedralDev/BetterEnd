package com.dfsek.betterEnd;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Slab.Type;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class CustomChunkGenerator extends ChunkGenerator {
	@Override
	public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
		Main main = Main.getInstance();
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(world.getSeed(), 4);
		//SimplexOctaveGenerator biomeGenerator = new SimplexOctaveGenerator(world.getSeed(), 6);
		ChunkData chunk = createChunkData(world);
		double totalChunkDistance2D = Math.sqrt(Math.pow(chunkX, 2)+Math.pow(chunkZ, 2));
		if(totalChunkDistance2D > 16) {
			//if(Math.sqrt(chunkX*chunkX + chunkZ*chunkZ) > 62.5) { 
			int outNoise = main.getConfig().getInt("outer-islands.noise");
			boolean clouds = main.getConfig().getBoolean("aether.clouds.enable-clouds");
			int cloudNoise = main.getConfig().getInt("aether.clouds.cloud-noise");
			int cloudHeight = main.getConfig().getInt("aether.clouds.cloud-height");
			for (int X = 0; X < 16; X++)
				for (int Z = 0; Z < 16; Z++) {
					double biomeNoiseLvl = Main.getBiomeNoise(chunkX*16+X, chunkZ*16+Z, world.getSeed());
					double totalDistance2D = Math.sqrt(Math.pow(chunkX*16+X, 2)+Math.pow(chunkZ*16+Z, 2));
					int height = (int) ((generator.noise((double) (chunkX*16+X)/16, (double) (chunkZ*16+Z)/16, 0.5D, 0.7D)-0.4))+main.getConfig().getInt("outer-islands.island-height");
					int biomeNoise = 0;
					if(biomeNoiseLvl < 0) {
						biomeNoise = (int) (biomeNoiseLvl*2.5*(generator.noise((double) (chunkX*16+X)/8, (double) (chunkZ*16+Z)/8, 0.5D, 0.7D)-0.4));
						height = (int) (biomeNoiseLvl*4*(generator.noise((double) (chunkX*16+X)/16, (double) (chunkZ*16+Z)/16, 0.5D, 0.7D)-0.4))+main.getConfig().getInt("outer-islands.island-height");
					}
					int yMin, yMax;
					if(totalDistance2D < 975) {
						yMin = 255;
						yMax = 0;
					} else if(totalDistance2D < 1050) {
						double noiseM = (-Math.pow(1.1, -totalDistance2D+1000))+1;
						if(noiseM > 1) noiseM = 1;
						if(noiseM < 0) noiseM = 0;
						yMin = (int) ((-56*noiseM*(generator.noise((double) (chunkX*16+X)/outNoise, (double) (chunkZ*16+Z)/outNoise, 0.1D, 0.55D)-0.4)+height+biomeNoise)+1);
						yMax = (int) ((5*noiseM*(generator.noise((double) (chunkX*16+X)/outNoise, (double) (chunkZ*16+Z)/outNoise, 0.1D, 0.55D)-0.4)+height-biomeNoise));
					} else {
						yMin = (int) (-56*(generator.noise((double) (chunkX*16+X)/outNoise, (double) (chunkZ*16+Z)/outNoise, 0.1D, 0.55D)-0.4)+height+biomeNoise)+1;
						yMax = (int) (5*(generator.noise((double) (chunkX*16+X)/outNoise, (double) (chunkZ*16+Z)/outNoise, 0.1D, 0.55D)-0.4)+height-biomeNoise);
					}
					if(biomeNoiseLvl > 0.45 && clouds) {
						double aetherLvl = -Math.pow(256, -biomeNoiseLvl+0.45)+1;
						if(aetherLvl < 0) aetherLvl = 0;
						int yMinc, yMaxc;
						if(totalDistance2D < 975) {
							yMinc = 255;
							yMaxc = 0;
						} else if(totalDistance2D < 1050) {
							double noiseM = (-Math.pow(1.1, -totalDistance2D+1000))+1;
							if(noiseM > 1) noiseM = 1;
							if(noiseM < 0) noiseM = 0;
							yMinc = (int) ((-5*noiseM*aetherLvl*(generator.noise((double) (chunkX*16+X)/cloudNoise, (double) (chunkZ*16+Z)/cloudNoise, 0.1D, 0.55D)-0.2)+cloudHeight)+1);
							yMaxc = (int) ((5*noiseM*aetherLvl*(generator.noise((double) (chunkX*16+X)/cloudNoise, (double) (chunkZ*16+Z)/cloudNoise, 0.1D, 0.55D)-0.2)+cloudHeight-1));
						} else {
							yMinc = (int) (-5*aetherLvl*(generator.noise((double) (chunkX*16+X)/cloudNoise, (double) (chunkZ*16+Z)/cloudNoise, 0.1D, 0.55D)-0.15)+cloudHeight)+1;
							yMaxc = (int) (5*aetherLvl*(generator.noise((double) (chunkX*16+X)/cloudNoise, (double) (chunkZ*16+Z)/cloudNoise, 0.1D, 0.55D)-0.15)+cloudHeight-1);
						}
						for (int Y = yMaxc; Y > yMinc; Y--) {
							chunk.setBlock(X, Y, Z, Material.WHITE_STAINED_GLASS);
						}
						if(totalDistance2D < 975) {
							yMinc = 255;
							yMaxc = 0;
						} else if(totalDistance2D < 1050) {
							double noiseM = (-Math.pow(1.1, -totalDistance2D+1000))+1;
							if(noiseM > 1) noiseM = 1;
							if(noiseM < 0) noiseM = 0;
							yMinc = (int) ((-4*noiseM*aetherLvl*(generator.noise((double) ((chunkX*16+X)-1000)/cloudNoise, (double) ((chunkZ*16+Z)-1000)/cloudNoise, 0.1D, 0.55D)-0.125)+cloudHeight+8)+1);
							yMaxc = (int) ((4*noiseM*aetherLvl*(generator.noise((double) ((chunkX*16+X)-1000)/cloudNoise, (double) ((chunkZ*16+Z)-1000)/cloudNoise, 0.1D, 0.55D)-0.125)+cloudHeight+7));
						} else {
							yMinc = (int) (-4*aetherLvl*(generator.noise((double) ((chunkX*16+X)-1000)/cloudNoise, (double) ((chunkZ*16+Z)-1000)/cloudNoise, 0.1D, 0.55D)-0.125)+cloudHeight+8)+1;
							yMaxc = (int) (4*aetherLvl*(generator.noise((double) ((chunkX*16+X)-1000)/cloudNoise, (double) ((chunkZ*16+Z)-1000)/cloudNoise, 0.1D, 0.55D)-0.125)+cloudHeight+7);
						}
						for (int Y = yMaxc; Y > yMinc; Y--) {
							chunk.setBlock(X, Y, Z, Material.WHITE_STAINED_GLASS);
						}
					}
					for (int Y = yMax; Y > yMin; Y--) {
						//if(generator.noise((double) (chunkX*16+X)/outNoise, (double) (chunkZ*16+Z)/outNoise, 0.1D, 0.55D) > 0.6) {
						if(generator.noise((double) (chunkX*16+X)/12D, (double) Y/12D, (double) (chunkZ*16+Z)/12D, 0.5D, 0.5D) > -0.7) {
							if(biomeNoiseLvl > 0.5) {
								int grassPercent = (int) ((biomeNoiseLvl-0.5)*1500);

								if(random.nextInt(100) < grassPercent) {
									if(Y > 62) {
										if(chunk.getBlockData(X, Y+1, Z).getMaterial() == Material.AIR) {
											chunk.setBlock(X, Y, Z, Material.GRASS_BLOCK);
											if(random.nextInt(100) < 45) {
												double grassType = random.nextDouble()*100;
												if(grassType < 70) {
													chunk.setBlock(X, Y+1, Z, Material.GRASS);
												} else if(grassType < 85) {
													chunk.setBlock(X, Y+1, Z, Material.TALL_GRASS);
													chunk.setBlock(X, Y+2, Z, main.getServer().createBlockData("minecraft:tall_grass[half=upper]"));
												} else if(grassType < 88) {
													chunk.setBlock(X, Y+1, Z, Material.LILY_OF_THE_VALLEY);
												} else if(grassType < 91)  {
													chunk.setBlock(X, Y+1, Z, Material.FERN);
												} else if(grassType < 96) {
													chunk.setBlock(X, Y+1, Z, Material.AZURE_BLUET);
												} else if(grassType < 99.9) {
													chunk.setBlock(X, Y+1, Z, Material.BLUE_ORCHID);
												} else {
													chunk.setBlock(X, Y+1, Z, Material.WITHER_ROSE);	
												}
											}
										} else chunk.setBlock(X, Y, Z, Material.DIRT);
									} else {
										Material material;
										material = Material.STONE;
										chunk.setBlock(X, Y, Z, material);
										if(material == Material.STONE && chunk.getBlockData(X, Y+1, Z).getMaterial() == Material.AIR && random.nextInt(100) < 48) {
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
								}

							} else {
								chunk.setBlock(X, Y, Z, Material.END_STONE);
							}
						}
						if(chunk.getBlockData(X, Y, Z).getMaterial() == Material.AIR && chunk.getBlockData(X, Y+1, Z).getMaterial() == Material.STONE && random.nextInt(100) < 28) {
							if(Y >= yMin) {
								switch(random.nextInt(4)) {
								case 0:
									if(chunk.getBlockData(X, Y-1, Z).getMaterial() == Material.AIR && random.nextInt(100) < 20) {
										chunk.setBlock(X, Y, Z, Material.COBBLESTONE_WALL);
										chunk.setBlock(X, Y-1, Z, Material.IRON_BARS);
									}
									break;
								case 1:
									if(random.nextInt(100) < 10) chunk.setBlock(X, Y+1, Z, main.getServer().createBlockData("minecraft:lantern[hanging=true]"));
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
						
						//}
					}
					boolean doBiomeGlass = true;
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
						case "OBSIDIAN_FOREST":
							chunk.setBlock(X, 0, Z, Material.ORANGE_STAINED_GLASS);
							break;
						} 
					}

				}

		} else {
			chunk = createVanillaChunkData(world, chunkX , chunkZ); 
		}
		return chunk;

	}
	@Override
	public boolean shouldGenerateStructures() {
		return false;
	}
	@Override
	public boolean shouldGenerateDecorations() {
		return true;
	}
	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		return Arrays.asList((BlockPopulator) new TreePopulator(), new StructurePopulator(), new OrePopulator());
	}

}