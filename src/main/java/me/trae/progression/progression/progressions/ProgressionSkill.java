package me.trae.progression.progression.progressions;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import io.github.trae.hf.SubModule;
import me.trae.progression.ProgressionPlugin;

public interface ProgressionSkill<Type extends Progression<T>, T> extends SubModule<ProgressionPlugin, Type> {

    String getSkillName();

    String getDescription();

    int getRequiredLevel();

    default void onSystem(final PlayerRef playerRef, final T t) {
    }

    default void onEvent(final PlayerRef playerRef, final T t) {
    }
}