package me.trae.progression.progression.interfaces;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import me.trae.progression.progression.ProgressionData;
import me.trae.progression.progression.progressions.Progression;

import java.util.List;
import java.util.Optional;

public interface IProgressionManager {

    List<ProgressionData> getProgressionDataList();

    void addProgressionData(final ProgressionData progressionData);

    void removeProgressionData(final ProgressionData progressionData);

    Optional<ProgressionData> getProgressionDataByPlayer(final PlayerRef playerRef);

    void giveExperience(final PlayerRef playerRef, final Progression<?> progression, final int amount);
}