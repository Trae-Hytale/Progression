package me.trae.progression.progression.interfaces;

import me.trae.progression.progression.data.ProgressionStatus;
import me.trae.progression.progression.progressions.Progression;

import java.util.Optional;

public interface IProgressionData {

    void addProgressionStatus(final ProgressionStatus progressionStatus);

    void removeProgressionStatus(final ProgressionStatus progressionStatus);

    Optional<ProgressionStatus> getProgressionStatus(final Progression progressionType);
}