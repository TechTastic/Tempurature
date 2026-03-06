package io.github.techtastic.temperature.systems;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.spatial.SpatialSystem;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.modules.block.BlockModule;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import io.github.techtastic.temperature.Temperature;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nonnull;

public class SpatialHeatSystem extends SpatialSystem<ChunkStore> {
    public static final Query<ChunkStore> QUERY = Query.and(Temperature.getHeatSourceComponentType(), BlockModule.BlockStateInfo.getComponentType());

    public SpatialHeatSystem() {
        super(Temperature.getBlockSpatialHeatResourceType());
    }

    @NullableDecl
    @Override
    public Vector3d getPosition(@Nonnull ArchetypeChunk<ChunkStore> archetypeChunk, int index) {
        BlockModule.BlockStateInfo blockInfo = archetypeChunk.getComponent(index, BlockModule.BlockStateInfo.getComponentType());
        assert blockInfo != null;
        Ref<ChunkStore> chunkRef = blockInfo.getChunkRef();
        if (chunkRef.isValid()) {
            BlockChunk blockChunk = chunkRef.getStore().getComponent(chunkRef, BlockChunk.getComponentType());
            assert blockChunk != null;
            int worldX = blockChunk.getX() << 5 | ChunkUtil.xFromBlockInColumn(blockInfo.getIndex());
            int worldY = ChunkUtil.yFromBlockInColumn(blockInfo.getIndex());
            int worldZ = blockChunk.getZ() << 5 | ChunkUtil.zFromBlockInColumn(blockInfo.getIndex());
            return new Vector3d(worldX, worldY, worldZ);
        } else {
            return null;
        }
    }

    @NullableDecl
    @Override
    public Query<ChunkStore> getQuery() {
        return QUERY;
    }
}
