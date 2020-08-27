import com.dfsek.betterend.ProbabilityCollection;
import com.dfsek.betterend.world.generation.biomes.BlockPalette;
import org.bukkit.Material;

import java.util.Random;

public class PaletteTest {
    public static void main(String[] args) {
        long l = System.nanoTime();
        BlockPalette p = new BlockPalette(new Random())
                .add(Material.GRASS_BLOCK, 1)
                .add(Material.DIRT, 2)
                .add(new ProbabilityCollection<Material>().add(Material.STONE, 1).add(Material.DIRT, 1), 3)
                .add(Material.STONE, 1);
        System.out.println((System.nanoTime() - l)/1000000 + "ms elapsed (Instantiation)");
        l = System.nanoTime();
        for(int i = 0; i < 64; i++) {
            System.out.println(p.get(i));
        }
        System.out.println((double)(System.nanoTime() - l)/1000000 + "ms elapsed (Getters)");
    }
}
