package io.github.techtastic.temperature.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.spatial.SpatialResource;
import com.hypixel.hytale.component.system.tick.DelayedEntitySystem;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.block.BlockModule;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.techtastic.temperature.Temperature;
import io.github.techtastic.temperature.component.HeatSourceComponent;
import io.github.techtastic.temperature.ui.TemperatureHUD;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TemperatureSystem extends DelayedEntitySystem<EntityStore> {
    public TemperatureSystem() {
        super(1f);
    }

    @Override
    public void tick(float dt, int index, @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        if (store.isProcessing()) return;

        Ref<EntityStore> playerRef = archetypeChunk.getReferenceTo(index);
        TransformComponent transform = store.ensureAndGetComponent(playerRef, TransformComponent.getComponentType());
        EntityStatMap statMap = archetypeChunk.getComponent(index, EntityStatMap.getComponentType());
        assert statMap != null;
        World world = store.getExternalData().getWorld();
        Vector3d position = transform.getPosition();
        List<Ref<ChunkStore>> heatSources = new ArrayList<>();

        SpatialResource<Ref<ChunkStore>, ChunkStore> resource = world.getChunkStore().getStore().getResource(Temperature.getBlockSpatialHeatResourceType());
        resource.getSpatialStructure().ordered(position, 32, heatSources);

        float totalHeatContribution = 0;

        for (Ref<ChunkStore> blockRef : heatSources) {
            BlockModule.BlockStateInfo info = blockRef.getStore().getComponent(blockRef, BlockModule.BlockStateInfo.getComponentType());
            if (info == null) continue;

            BlockChunk blockChunk = info.getChunkRef().getStore().getComponent(info.getChunkRef(), BlockChunk.getComponentType());
            int worldX = blockChunk.getX() << 5 | ChunkUtil.xFromBlockInColumn(info.getIndex());
            int worldY = ChunkUtil.yFromBlockInColumn(info.getIndex());
            int worldZ = blockChunk.getZ() << 5 | ChunkUtil.zFromBlockInColumn(info.getIndex());
            Vector3d sourcePos = new Vector3d(worldX, worldY, worldZ);

            HeatSourceComponent component = blockRef.getStore().ensureAndGetComponent(blockRef, Temperature.getHeatSourceComponentType());

            double dist = position.distanceTo(sourcePos);
            float factor = 1f - (float) Math.min(1.0, dist / component.getFalloffRange());
            totalHeatContribution += component.getIntensity() * factor;
        }
        statMap.addStatValue(Temperature.getTemperatureStatId(), totalHeatContribution);

        float currentTemp = Objects.requireNonNull(statMap.get(Temperature.getTemperatureStatId())).get();
        Player player = store.getComponent(playerRef, Player.getComponentType());
        if (player != null && player.getHudManager().getCustomHud() instanceof TemperatureHUD tempHud) {
            tempHud.updateTemperature(currentTemp);
        }
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return Query.and(EntityStatMap.getComponentType(), Player.getComponentType(), TransformComponent.getComponentType());
    }
}
