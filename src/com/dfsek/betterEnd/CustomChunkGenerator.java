package com.dfsek.betterEnd;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
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
			for (int X = 0; X < 16; X++)
				for (int Z = 0; Z < 16; Z++) {
					double biomeNoiseLvl = Main.getBiomeNoise(chunkX*16+X, chunkZ*16+Z, world.getSeed());
					double totalDistance2D = Math.sqrt(Math.pow(chunkX*16+X, 2)+Math.pow(chunkZ*16+Z, 2));
					int height = (int) ((generator.noise((double) (chunkX*16+X)/16, (double) (chunkZ*16+Z)/16, 0.5D, 0.7D)-0.4))+64;
					int biomeNoise = 0;
					if(biomeNoiseLvl < 0) {
						biomeNoise = (int) (biomeNoiseLvl*2.5*(generator.noise((double) (chunkX*16+X)/8, (double) (chunkZ*16+Z)/8, 0.5D, 0.7D)-0.4));
						height = (int) (biomeNoiseLvl*4*(generator.noise((double) (chunkX*16+X)/16, (double) (chunkZ*16+Z)/16, 0.5D, 0.7D)-0.4))+64;
					}
					int yMin = 255, yMax = 0;
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
					for (int Y = yMax; Y > yMin; Y--) {
						//if(generator.noise((double) (chunkX*16+X)/outNoise, (double) (chunkZ*16+Z)/outNoise, 0.1D, 0.55D) > 0.6) {
						if(generator.noise((double) (chunkX*16+X)/12D, (double) Y/12D, (double) (chunkZ*16+Z)/12D, 0.5D, 0.5D) > -0.7) {
							if(biomeNoiseLvl > 0.5) {
								int grassPercent = (int) ((biomeNoiseLvl-0.5)*1500);

								if(random.nextInt(100) < grassPercent) {
									if(Y > 62) {
										if(chunk.getBlockData(X, Y+1, Z).getMaterial() == Material.AIR) {
											chunk.setBlock(X, Y, Z, Material.GRASS_BLOCK);
											if(random.nextInt(100) < 55) {
												double grassType = random.nextDouble()*100;
												if(grassType < 85) {
													chunk.setBlock(X, Y+1, Z, Material.GRASS);
												} else if(grassType < 88) {
													chunk.setBlock(X, Y+1, Z, Material.LILY_OF_THE_VALLEY);
												} else if(grassType < 91)  {
													chunk.setBlock(X, Y+1, Z, Material.FERN);
												} else if(grassType < 96) {
													chunk.setBlock(X, Y+1, Z, Material.AZURE_BLUET);
												} else if(grassType < 99.95) {
													chunk.setBlock(X, Y+1, Z, Material.BLUE_ORCHID);
												} else {
													chunk.setBlock(X, Y+1, Z, Material.WITHER_ROSE);	
												}
											}
										} else chunk.setBlock(X, Y, Z, Material.DIRT);
									} else {
										double ore = 1;//generator.noise((double) (chunkX*16+X)/16D, (double) Y/16D, (double) (chunkZ*16+Z)/16D, 0.25D, 0.5D);
										Material material;
										if(ore < -0.4 && ore > -0.5) material = Material.GRANITE;
										else if(ore < 0.05 && ore > -0.05) material = Material.ANDESITE;
										else if(ore < 0.5 && ore > 0.4) material = Material.DIORITE;
										else material = Material.STONE;
										chunk.setBlock(X, Y, Z, material);

									}
								} else {
									chunk.setBlock(X, Y, Z, Material.END_STONE);
								}

							} else {
								chunk.setBlock(X, Y, Z, Material.END_STONE);
							}
						}
						//}
					}
					boolean doBiomeGlass = false;
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
		return true;
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