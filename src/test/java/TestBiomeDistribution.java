import com.dfsek.betterend.world.EndBiome;
import com.dfsek.betterend.world.EndBiomeGrid;

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
        Map<EndBiome, Integer> map = new HashMap<>();
        for(EndBiome b : EndBiome.values()) {
            map.put(b, 0);
        }
        EndBiomeGrid grid = new EndBiomeGrid(ThreadLocalRandom.current().nextInt());
        for(int x = 0; x < 1000000; x++) {
            for(int z = 0; z < 1; z++) {
                long l = System.nanoTime();
                EndBiome b = grid.getBiome(x, z);
                if(x % 100000 == 0) System.out.println("Biome retrieved in " + (System.nanoTime() - l) + "ns");
                map.put(b, map.getOrDefault(b, 0) + 1);
            }
        }


        for(int x = 0; x < 25; x++) {
            for(int z = 0; z < 25; z++) {
                System.out.print(grid.getBiome(x*50, z*50));
            }
            System.out.println();
        }
        System.out.println("*-----------------------------------------*");
        for(Map.Entry<EndBiome, Integer> e : map.entrySet()) {
            System.out.println(e.getKey().toString() + ": " + e.getValue());
        }
        System.out.println("*-----------------------------------------*");
        System.out.println("Aether Aggregated: " + (map.get(EndBiome.AETHER) + map.get(EndBiome.AETHER_FOREST) + map.get(EndBiome.AETHER_HIGHLANDS) + map.get(EndBiome.AETHER_HIGHLANDS_FOREST) + map.get(EndBiome.AETHER_HIGHLANDS_BORDER)));
        System.out.println("Void Aggregated: " + (map.get(EndBiome.VOID) + map.get(EndBiome.STARFIELD) + map.get(EndBiome.VOID_AETHER_BORDER) + map.get(EndBiome.VOID_AETHER_HIGHLANDS_BORDER) + map.get(EndBiome.VOID_END_BORDER)));
        System.out.println("End Aggregated: " + (map.get(EndBiome.END)));
        System.out.println("Shattered End Aggregated: " + (map.get(EndBiome.SHATTERED_END) + map.get(EndBiome.SHATTERED_FOREST)));
        System.out.println("*-----------------------------------------*");
        System.out.println("Done. Time elapsed: " + (System.nanoTime() - t) / 1000000L + "ms. " + (System.nanoTime() - t) / (10000L) + "ns per calculation.");


    }
}
