package io.github.techtastic.temperature;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.ResourceType;
import com.hypixel.hytale.component.spatial.KDTree;
import com.hypixel.hytale.component.spatial.SpatialResource;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.modules.entitystats.asset.EntityStatType;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import io.github.techtastic.temperature.component.HeatSourceComponent;
import io.github.techtastic.temperature.systems.HeatSourceRefSystem;
import io.github.techtastic.temperature.systems.SpatialHeatSystem;
import io.github.techtastic.temperature.systems.TemperatureSystem;
import io.github.techtastic.temperature.ui.TemperatureHUD;

import javax.annotation.Nonnull;

public class Temperature extends JavaPlugin {
    public static Temperature INSTANCE;
    private ComponentType<ChunkStore, HeatSourceComponent> heatSourceComponentType;
    private ResourceType<ChunkStore, SpatialResource<Ref<ChunkStore>, ChunkStore>> blockSpatialHeatResourceType;

    public Temperature(@Nonnull JavaPluginInit init) {
        super(init);
        INSTANCE = this;
    }

    @Override
    protected void setup() {
        this.heatSourceComponentType = this.getChunkStoreRegistry().registerComponent(HeatSourceComponent.class, HeatSourceComponent::new);
        this.blockSpatialHeatResourceType = this.getChunkStoreRegistry().registerSpatialResource(() -> new KDTree<>(chunk ->
                SpatialHeatSystem.QUERY.test(chunk.getStore().getArchetype(chunk))));

        this.getEntityStoreRegistry().registerSystem(new TemperatureSystem());
        this.getChunkStoreRegistry().registerSystem(new SpatialHeatSystem());
        this.getChunkStoreRegistry().registerSystem(new HeatSourceRefSystem());
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, TemperatureHUD::onPlayerReady);
    }

    public static int getTemperatureStatId() {
        return EntityStatType.getAssetMap().getIndex("Temperature");
    }

    public static ComponentType<ChunkStore, HeatSourceComponent> getHeatSourceComponentType() {
        return INSTANCE.heatSourceComponentType;
    }

    public static ResourceType<ChunkStore, SpatialResource<Ref<ChunkStore>, ChunkStore>> getBlockSpatialHeatResourceType() {
        return INSTANCE.blockSpatialHeatResourceType;
    }
}