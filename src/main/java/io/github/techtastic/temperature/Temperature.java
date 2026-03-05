package io.github.techtastic.temperature;

import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.modules.entitystats.asset.EntityStatType;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import io.github.techtastic.temperature.commands.ExampleCommand;
import io.github.techtastic.temperature.ui.TemperatureHUD;

import javax.annotation.Nonnull;

public class Temperature extends JavaPlugin {
    public static Temperature INSTANCE;

    public Temperature(@Nonnull JavaPluginInit init) {
        super(init);
        INSTANCE = this;
    }

    @Override
    protected void setup() {

        this.getCommandRegistry().registerCommand(new ExampleCommand("example", "An example command"));
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, TemperatureHUD::onPlayerReady);
    }

    public static int getTemperatureStatId() {
        return EntityStatType.getAssetMap().getIndex("Temperature");
    }
}