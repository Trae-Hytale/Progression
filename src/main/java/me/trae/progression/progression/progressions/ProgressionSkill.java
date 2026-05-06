package me.trae.progression.progression.progressions;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import io.github.trae.di.InjectorApi;
import io.github.trae.hf.SubModule;
import io.github.trae.utilities.UtilGeneric;
import me.trae.progression.ProgressionPlugin;

public interface ProgressionSkill<Type extends Progression<T>, T> extends SubModule<ProgressionPlugin, Type> {

    @SuppressWarnings("unchecked")
    default Type getProgression() {
        final Class<?> progressionClass = UtilGeneric.getGenericParameter(this.getClass(), ProgressionSkill.class, 0);
        if (progressionClass == null) {
            throw new IllegalStateException("Could not resolve progression type for %s".formatted(this.getClass().getName()));
        }

        return (Type) InjectorApi.get(progressionClass);
    }

    String getSkillName();

    String getDescription();

    int getRequiredLevel();

    default void onSystem(final PlayerRef playerRef, final T t) {
    }

    default void onEvent(final PlayerRef playerRef, final T t) {
    }
}