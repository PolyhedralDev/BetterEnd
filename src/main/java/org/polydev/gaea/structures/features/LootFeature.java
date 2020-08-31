package org.polydev.gaea.structures.features;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.block.Structure;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.IOUtils;
import org.bukkit.inventory.Inventory;
import org.json.simple.parser.ParseException;
import org.polydev.gaea.structures.NMSStructure;
import org.polydev.gaea.structures.StructureUtil;
import org.polydev.gaea.structures.loot.LootTable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class LootFeature implements Feature {
    private final LootTable table;
    public LootFeature(InputStream lootFile) {
        LootTable table1;
        try {
            String loot = IOUtils.toString(lootFile, StandardCharsets.UTF_8.name());
            table1 = new LootTable(loot);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
            table1 = null;
        }
        this.table = table1;
    }
    @Override
    public void populate(NMSStructure s, Random r) {
        for(Location chestLoc : StructureUtil.getChestsIn(s.getBoundingLocations()[0], s.getBoundingLocations()[1])) {
            if(chestLoc.getBlock().getState() instanceof Container) {
                BlockState blockState = chestLoc.getBlock().getState();
                Container container = (Container) blockState;
                Inventory containerInventory = container.getInventory();
                this.table.fillInventory(containerInventory, r);
            }
        }
    }
}
