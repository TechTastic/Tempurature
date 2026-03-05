package io.github.techtastic.temperature.ui;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class TemperatureHUD extends CustomUIHud {
    public TemperatureHUD(@NonNullDecl PlayerRef playerRef) {
        super(playerRef);
    }

    @Override
    protected void build(@NonNullDecl UICommandBuilder uiCommandBuilder) {
        uiCommandBuilder.append("TemperatureHUD.ui");
    }

    public void updateTemperature(float celsius) {
        UICommandBuilder builder = new UICommandBuilder();
        String formattedTemp = String.format("%.1f°C", celsius);
        builder.set("#Temperature.Text", Message.raw(formattedTemp));

        this.update(false, builder);
    }

    public static void onPlayerReady(PlayerReadyEvent event) {
        PlayerRef ref = event.getPlayer().getPlayerRef();
        event.getPlayer().getHudManager().setCustomHud(ref, new TemperatureHUD(ref));
    }
}
