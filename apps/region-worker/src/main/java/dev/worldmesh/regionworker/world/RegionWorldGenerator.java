package dev.worldmesh.regionworker.world;

import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generator.Generator;

public final class RegionWorldGenerator {

    private RegionWorldGenerator() {
    }

    public static Generator flatGrassWorld() {
        return unit -> unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK);
    }
}