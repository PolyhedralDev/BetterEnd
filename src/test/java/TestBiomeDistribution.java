import com.dfsek.betterend.biomes.Biome;
import com.dfsek.betterend.biomes.BiomeGrid;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Test class for testing and troubleshooting distribution of BiomeGrid biomes
 */
public class TestBiomeDistribution {
    public static void main(String[] args) {
        System.out.println("Beginning biome distribution test.");
        long t = System.nanoTime();
        System.out.println("*-----------------------------------------*");
        Map<Biome, Integer> map = new HashMap<>();
        BiomeGrid grid = new BiomeGrid(ThreadLocalRandom.current().nextInt());
        for(int x = 0; x < 10000; x++) {
            for(int z = 0; z < 1; z++) {
                long l = System.nanoTime();
                Biome b = grid.getBiome(x, z);
                if(x%100 == 0) System.out.println("Biome retrieved in " + (System.nanoTime() - l) + "ns");
                map.put(b, map.getOrDefault(b, 0) + 1);
            }
        }
        System.out.println("*-----------------------------------------*");
        for(Map.Entry<Biome, Integer> e : map.entrySet()) {
            System.out.println(e.getKey().toString() + ": " + e.getValue());
        }
        System.out.println("*-----------------------------------------*");
        System.out.println("Aether Aggregated: " + (map.get(Biome.AETHER) + map.get(Biome.AETHER_FOREST) + map.get(Biome.AETHER_HIGHLANDS) + map.get(Biome.AETHER_HIGHLANDS_FOREST) + map.get(Biome.AETHER_HIGHLANDS_BORDER)));
        System.out.println("Void Aggregated: " + (map.get(Biome.VOID) + map.get(Biome.STARFIELD) + map.get(Biome.VOID_AETHER_BORDER) + map.get(Biome.VOID_AETHER_HIGHLANDS_BORDER) + map.get(Biome.VOID_END_BORDER)));
        System.out.println("End Aggregated: " + (map.get(Biome.END)));
        System.out.println("Shattered End Aggregated: " + (map.get(Biome.SHATTERED_END) + map.get(Biome.SHATTERED_FOREST)));
        System.out.println("*-----------------------------------------*");
        System.out.println("Done. Time elapsed: " + (System.nanoTime() - t)/1000000L + "ms. " + (System.nanoTime() - t)/(10000L) + "ns per calculation.");
    }
}
