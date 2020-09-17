package com.dfsek.betterend.world;

import com.dfsek.betterend.world.decor.AetherDecorator;
import com.dfsek.betterend.world.decor.AetherForestDecorator;
import com.dfsek.betterend.world.decor.AetherHighlandsDecorator;
import com.dfsek.betterend.world.decor.AetherHighlandsForestDecorator;
import com.dfsek.betterend.world.decor.EndDecorator;
import com.dfsek.betterend.world.decor.MainIslandDecorator;
import com.dfsek.betterend.world.decor.ShatteredEndDecorator;
import com.dfsek.betterend.world.decor.ShatteredForestDecorator;
import com.dfsek.betterend.world.decor.StarfieldDecorator;
import com.dfsek.betterend.world.decor.VoidDecorator;
import com.dfsek.betterend.world.generators.biomes.AetherGenerator;
import com.dfsek.betterend.world.generators.biomes.AetherHighlandsGenerator;
import com.dfsek.betterend.world.generators.biomes.EndGenerator;
import com.dfsek.betterend.world.generators.biomes.MainIslandGenerator;
import com.dfsek.betterend.world.generators.biomes.ShatteredEndGenerator;
import com.dfsek.betterend.world.generators.biomes.VoidGenerator;
import com.dfsek.betterend.world.generators.border.AetherHighlandsBorderGenerator;
import com.dfsek.betterend.world.generators.border.VoidAetherBorderGenerator;
import com.dfsek.betterend.world.generators.border.VoidAetherHighlandsBorderGenerator;
import com.dfsek.betterend.world.generators.border.VoidEndBorderGenerator;
import org.bukkit.Material;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.biome.BiomeTerrain;
import org.polydev.gaea.biome.Decorator;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.structures.features.BlockReplaceFeature;
import org.polydev.gaea.structures.features.Feature;

import java.util.Collections;
import java.util.List;

/**
 * Representation of BetterEnd custom biomes.
 *
 * @author dfsek
 * @since 3.6.2
 */
public enum EndBiome implements Biome {
    END(new EndGenerator(), new EndDecorator()),
    SHATTERED_END(new ShatteredEndGenerator(), new ShatteredEndDecorator()),
    SHATTERED_FOREST(new ShatteredEndGenerator(), new ShatteredForestDecorator()),
    AETHER(new AetherGenerator(), new AetherDecorator()),
    MAIN_ISLAND(new MainIslandGenerator(), new MainIslandDecorator()),
    AETHER_HIGHLANDS(new AetherHighlandsGenerator(), new AetherHighlandsDecorator()),
    AETHER_FOREST(new AetherGenerator(), new AetherForestDecorator()),
    AETHER_HIGHLANDS_FOREST(new AetherHighlandsGenerator(), new AetherHighlandsForestDecorator()),
    VOID(new VoidGenerator(), new VoidDecorator()),
    VOID_END_BORDER(new VoidEndBorderGenerator(), new EndDecorator()),
    VOID_AETHER_BORDER(new VoidAetherBorderGenerator(), new AetherDecorator()),
    VOID_AETHER_HIGHLANDS_BORDER(new VoidAetherHighlandsBorderGenerator(), new AetherHighlandsDecorator()),
    AETHER_HIGHLANDS_BORDER(new AetherHighlandsBorderGenerator(), new AetherHighlandsDecorator()),
    STARFIELD(new VoidGenerator(), new StarfieldDecorator());

    private final BiomeTerrain generator;
    private final Decorator decorator;

    EndBiome(BiomeTerrain g, Decorator d) {
        this.generator = g;
        this.decorator = d;
    }


    /**
     * Checks whether or not the Biome is a variant of the Aether.
     *
     * @return Whether or not the Biome is an Aether variant.
     * @author dfsek
     * @since 3.6.2
     */
    public boolean isAether() {
        return (this.equals(EndBiome.AETHER)
                || this.equals(EndBiome.AETHER_FOREST)
                || this.equals(EndBiome.AETHER_HIGHLANDS)
                || this.equals(EndBiome.AETHER_HIGHLANDS_FOREST)
                || this.equals(EndBiome.VOID_AETHER_BORDER)
                || this.equals(EndBiome.VOID_AETHER_HIGHLANDS_BORDER)
                || this.equals(EndBiome.AETHER_HIGHLANDS_BORDER));
    }

    /**
     * Checks whether or not the Biome is a variant of the Highlands.
     *
     * @return Whether or not the Biome is a Highlands variant.
     * @author dfsek
     * @since 3.6.2
     */
    public boolean isHighlands() {
        return (this.equals(EndBiome.AETHER_HIGHLANDS) || this.equals(EndBiome.AETHER_HIGHLANDS_FOREST));
    }

    /**
     * Checks whether or not the Biome is a variant of the Void.
     *
     * @return Whether or not the Biome is a Void variant.
     * @author dfsek
     * @since 3.6.2
     */
    public boolean isVoid() {
        return (this.equals(EndBiome.VOID) || this.equals(EndBiome.STARFIELD));
    }

    /**
     * Checks whether or not the Biome is a variant of the Shattered End.
     *
     * @return Whether or not the Biome is a Shattered End variant.
     * @author dfsek
     * @since 3.6.2
     */
    public boolean isShattered() {
        return (this.equals(EndBiome.SHATTERED_END) || this.equals(EndBiome.SHATTERED_FOREST));
    }

    @Override
    public org.bukkit.block.Biome getVanillaBiome() {
        return decorator.getVanillaBiome();
    }

    public EndBiome getVoidBorderVariant() {
        switch(this) {
            case END:
            case SHATTERED_END:
            case SHATTERED_FOREST:
                return VOID_END_BORDER;
            case AETHER:
            case AETHER_FOREST:
                return VOID_AETHER_BORDER;
            case AETHER_HIGHLANDS:
            case AETHER_HIGHLANDS_FOREST:
                return VOID_AETHER_HIGHLANDS_BORDER;
            default:
                return this;
        }
    }

    /**
     * Gets the generator object
     *
     * @return BiomeTerrain - the terrain gen object.
     */
    @Override
    public BiomeTerrain getGenerator() {
        return this.generator;
    }

    @Override
    public List<Feature> getStructureFeatures() {
        switch(this) {
            case AETHER_HIGHLANDS_FOREST:
            case AETHER_HIGHLANDS_BORDER:
            case AETHER_HIGHLANDS:
                return Collections.singletonList(new BlockReplaceFeature(4, new ProbabilityCollection<Material>().add(Material.COBWEB, 1)));
            default:
                return Collections.emptyList();
        }
    }

    @Override
    public Decorator getDecorator() {
        return this.decorator;
    }

    public boolean shouldGenerateSnow() {
        return decorator.shouldGenerateSnow();
    }
}
