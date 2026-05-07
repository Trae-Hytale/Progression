package me.trae.progression.progression.progressions;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import io.github.trae.hf.SubModule;
import me.trae.progression.ProgressionPlugin;
import me.trae.progression.progression.data.ProgressionStatus;

import java.util.Collections;
import java.util.List;

public interface ProgressionSkill<Type extends Progression<T>, T> extends SubModule<ProgressionPlugin, Type> {

    String getSkillName();

    String getDescription();

    int getRequiredLevel();

    default void onUnlock(final PlayerRef playerRef) {
    }

    default List<Class<? extends ProgressionSkill<?, ?>>> getIncompatibleSkills() {
        return Collections.emptyList();
    }

    default void onSystem(final PlayerRef playerRef, final ProgressionStatus progressionStatus, final T t) {
    }

    default void onEvent(final PlayerRef playerRef, final ProgressionStatus progressionStatus, final T t) {
    }
}