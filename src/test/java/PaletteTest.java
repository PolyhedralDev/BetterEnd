import com.dfsek.betterend.ProbabilityCollection;
import com.dfsek.betterend.world.generation.biomes.BlockPalette;
import org.bukkit.Material;

import java.util.Random;

public class PaletteTest {
    public static void main(String[] args) {
        BlockPalette p = new BlockPalette()
                .add(Material.GRASS_BLOCK, 1)
                .add(Material.DIRT, 2)
                .add(new ProbabilityCollection<Material>().add(Material.STONE, 1).add(Material.DIRT, 1), 3)
                .add(Material.STONE, 1);
        for(int i = 0; i < 10; i++) {
            System.out.println(p.get(i, new Random()));
        }

    }
}
