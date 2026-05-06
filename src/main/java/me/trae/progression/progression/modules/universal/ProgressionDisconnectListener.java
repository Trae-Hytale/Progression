package me.trae.progression.progression.modules.universal;

import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import io.github.trae.di.annotations.type.component.Component;
import io.github.trae.hf.Module;
import io.github.trae.hytale.framework.event.EventListener;
import io.github.trae.hytale.framework.event.annotations.EventHandler;
import io.github.trae.hytale.framework.event.constants.EventPriority;
import io.github.trae.hytale.framework.utility.UtilMessage;
import me.trae.progression.ProgressionPlugin;
import me.trae.progression.progression.ProgressionManager;

@Component
public class ProgressionDisconnectListener implements Module<ProgressionPlugin, ProgressionManager>, EventListener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDisconnect(final PlayerDisconnectEvent event) {
        final PlayerRef playerRef = event.getPlayerRef();

        this.getManager().getProgressionDataByPlayer(playerRef).ifPresent(progressionData -> {
            this.getManager().removeProgressionData(progressionData);

            UtilMessage.log("Progression", "Unloaded Data: <yellow>%s</yellow>".formatted(playerRef.getUsername()));
        });
    }
}