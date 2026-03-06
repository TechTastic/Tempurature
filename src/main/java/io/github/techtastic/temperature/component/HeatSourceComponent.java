package io.github.techtastic.temperature.component;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.spatial.SpatialData;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class HeatSourceComponent extends SpatialData<ChunkStore> implements Component<ChunkStore> {
    private float intensityCelsius;
    private float falloffRange;

    public static final BuilderCodec<HeatSourceComponent> CODEC = BuilderCodec.builder(HeatSourceComponent.class, HeatSourceComponent::new)
            .append(new KeyedCodec<>("Intensity", Codec.FLOAT), (d, v) -> d.intensityCelsius = v, d -> d.intensityCelsius).add()
            .append(new KeyedCodec<>("Range", Codec.FLOAT), (d, v) -> d.falloffRange = v, d -> d.falloffRange).add()
            .build();

    public HeatSourceComponent() {}

    @NullableDecl
    @Override
    public HeatSourceComponent clone() {
        HeatSourceComponent source = new HeatSourceComponent();
        source.intensityCelsius = this.intensityCelsius;
        source.falloffRange = this.falloffRange;
        return source;
    }
}