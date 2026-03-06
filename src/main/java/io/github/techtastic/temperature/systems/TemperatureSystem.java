package io.github.techtastic.temperature.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.DelayedEntitySystem;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.techtastic.temperature.Temperature;
import io.github.techtastic.temperature.ui.TemperatureHUD;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.Objects;

public class TemperatureSystem extends DelayedEntitySystem<EntityStore> {
    public TemperatureSystem() {
        super(1f);
    }

    @Override
    public void tick(float dt, int index, @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        Ref<EntityStore> playerRef = archetypeChunk.getReferenceTo(index);
        EntityStatMap statMap = archetypeChunk.getComponent(index, EntityStatMap.getComponentType());

        if (statMap != null) {
            float currentTemp = Objects.requireNonNull(statMap.get(Temperature.getTemperatureStatId())).get();

            // Do stuff

            Player player = store.getComponent(playerRef, Player.getComponentType());
            if (player != null && player.getHudManager().getCustomHud() instanceof TemperatureHUD tempHud) {
                tempHud.updateTemperature(currentTemp);
            }
        }
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return Query.and(EntityStatMap.getComponentType(), Player.getComponentType());
    }
}
