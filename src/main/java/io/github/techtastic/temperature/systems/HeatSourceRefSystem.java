package io.github.techtastic.temperature.systems;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefSystem;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.modules.block.BlockModule;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import io.github.techtastic.temperature.Temperature;
import io.github.techtastic.temperature.component.HeatSourceComponent;
import io.github.techtastic.temperature.util.BlockSetUtils;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class HeatSourceRefSystem extends RefSystem<ChunkStore> {
    @Override
    public void onEntityAdded(@NonNullDecl Ref<ChunkStore> ref, @NonNullDecl AddReason addReason, @NonNullDecl Store<ChunkStore> store, @NonNullDecl CommandBuffer<ChunkStore> commandBuffer) {
        if (store.isProcessing()) return;

        BlockModule.BlockStateInfo info = store.ensureAndGetComponent(ref, BlockModule.BlockStateInfo.getComponentType());
        BlockChunk blockChunk = info.getChunkRef().getStore().ensureAndGetComponent(info.getChunkRef(), BlockChunk.getComponentType());
        int worldX = blockChunk.getX() << 5 | ChunkUtil.xFromBlockInColumn(info.getIndex());
        int worldY = ChunkUtil.yFromBlockInColumn(info.getIndex());
        int worldZ = blockChunk.getZ() << 5 | ChunkUtil.zFromBlockInColumn(info.getIndex());
        BlockType type = store.getExternalData().getWorld().getBlockType(new Vector3i(worldX, worldY, worldZ));

        if (BlockSetUtils.isPotentialHeatSource(type)) {
            // TODO: Custom Handling
            HeatSourceComponent source = store.ensureAndGetComponent(ref, Temperature.getHeatSourceComponentType());
            source.setIntensity(40);
            source.setFalloffRange(7);
        }
    }

    @Override
    public void onEntityRemove(@NonNullDecl Ref<ChunkStore> ref, @NonNullDecl RemoveReason removeReason, @NonNullDecl Store<ChunkStore> store, @NonNullDecl CommandBuffer<ChunkStore> commandBuffer) {

    }

    @NullableDecl
    @Override
    public Query<ChunkStore> getQuery() {
        return Query.not(Temperature.getHeatSourceComponentType());
    }
}
