package me.trae.progression.progression.modules.universal;

import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import io.github.trae.di.annotations.type.component.Component;
import io.github.trae.hf.Module;
import io.github.trae.hytale.framework.event.annotations.EventHandler;
import io.github.trae.hytale.framework.event.constants.EventPriority;
import io.github.trae.hytale.framework.utility.UtilMessage;
import me.trae.progression.ProgressionPlugin;
import me.trae.progression.progression.ProgressionManager;

import java.util.EventListener;

@Component
public class ProgressionConnectListener implements Module<ProgressionPlugin, ProgressionManager>, EventListener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerConnect(final PlayerConnectEvent event) {
        final PlayerRef playerRef = event.getPlayerRef();

        this.getManager().getRepository().findOneSynchronously(playerRef.getUuid()).ifPresent(progressionData -> {
            this.getManager().addProgressionData(progressionData);

            UtilMessage.log("Progression", "Loaded Data: <yellow>%s</yellow>".formatted(playerRef.getUsername()));
        });
    }
}