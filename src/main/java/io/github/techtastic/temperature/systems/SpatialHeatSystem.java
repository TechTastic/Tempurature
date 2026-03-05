package io.github.techtastic.temperature.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.ResourceType;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.spatial.SpatialResource;
import com.hypixel.hytale.component.spatial.SpatialSystem;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class SpatialHeatSystem extends SpatialSystem<ChunkStore> {
    public SpatialHeatSystem(@NonNullDecl ResourceType<ChunkStore, SpatialResource<Ref<ChunkStore>, ChunkStore>> resourceType) {
        super(resourceType);
    }

    @NullableDecl
    @Override
    public Vector3d getPosition(@NonNullDecl ArchetypeChunk<ChunkStore> archetypeChunk, int i) {
        return null;
    }

    @NullableDecl
    @Override
    public Query<ChunkStore> getQuery() {
        return null;
    }
}
