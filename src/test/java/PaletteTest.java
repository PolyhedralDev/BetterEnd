import com.dfsek.betterend.ProbabilityCollection;
import com.dfsek.betterend.world.generation.biomes.BlockPalette;
import org.bukkit.Material;

import java.util.Random;

public class PaletteTest {
    public static void main(String[] args) {
        long l = System.nanoTime();
        BlockPalette p = new BlockPalette(new Random())
                .add(Material.GRASS_BLOCK, 1)
                .add(Material.DIRT, 12)
                .add(new ProbabilityCollection<Material>().add(Material.STONE, 1).add(Material.DIRT, 1), 20)
                .add(Material.STONE, 30);
        System.out.println((System.nanoTime() - l)/1000000 + "ms elapsed (Instantiation)");
        l = System.nanoTime();
        for(int i = 0; i < 64; i++) {
            long l2 = System.nanoTime();
            System.out.println(p.get(i) + " retrieved in " + (System.nanoTime() - l2)/1000 + "us");
        }
        System.out.println((double)(System.nanoTime() - l)/1000000 + "ms elapsed (Getters)");
    }
}
