package me.trae.progression.progression.events;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import io.github.trae.hytale.framework.event.types.CustomEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.trae.progression.progression.progressions.Progression;

@AllArgsConstructor
@Getter
public class ProgressionLevelUpEvent extends CustomEvent {

    private final Progression<?> progression;
    private final PlayerRef playerRef;
    private final int previousLevel, level;
}