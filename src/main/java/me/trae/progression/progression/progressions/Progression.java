package me.trae.progression.progression.progressions;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import io.github.trae.hf.Module;
import me.trae.progression.ProgressionPlugin;
import me.trae.progression.progression.ProgressionManager;

import java.awt.*;

public interface Progression<T> extends Module<ProgressionPlugin, ProgressionManager> {

    String getId();

    String getProgressionName();

    Color getColor();

    int getMaxLevel();

    double getBaseExperience();

    double getExponent();

    default int getRequiredExperience(final int level) {
        return (int) (this.getBaseExperience() * Math.pow(level, this.getExponent()));
    }

    default void onLevelUp(final PlayerRef playerRef, final int level) {
    }

    default void onSystem(final PlayerRef playerRef, final T t) {
    }

    default void onEvent(final PlayerRef playerRef, final T t) {
    }
}