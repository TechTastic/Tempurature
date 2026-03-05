package io.github.techtastic.temperature.util;

import com.hypixel.hytale.server.core.asset.type.blockset.config.BlockSet;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.modules.blockset.BlockSetModule;

public class BlockSetUtils {
    public static boolean isPotentialHeatSource(BlockType blockType) {
        int setId = BlockSet.getAssetMap().getIndex("Heat_Source");
        return BlockSetModule.getInstance().blockInSet(setId, blockType);
    }
}
