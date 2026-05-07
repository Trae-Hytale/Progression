package me.trae.progression.progression.progressions.types;

import com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.trae.hytale.framework.system.data.EventSystemContext;
import me.trae.progression.progression.data.ProgressionStatus;
import me.trae.progression.progression.progressions.Progression;
import me.trae.progression.progression.progressions.ProgressionSkill;

import java.awt.*;

//@Component
public class FarmingProgression implements Progression<EventSystemContext<EntityStore, BreakBlockEvent>> {

    @Override
    public String getId() {
        return "29981c36-4a8e-49a4-9d78-f63d700aac95";
    }

    @Override
    public String getProgressionName() {
        return "Farming";
    }

    @Override
    public Color getColor() {
        return new Color(255, 170, 0);
    }

    @Override
    public void onLevelUp(final PlayerRef playerRef, final int level) {
    }

    @Override
    public int getMaxLevel() {
        return 100;
    }

    @Override
    public double getBaseExperience() {
        return 30.0D;
    }

    @Override
    public double getExponent() {
        return 1.6D;
    }

    @Override
    public void onSystem(final PlayerRef playerRef, final EventSystemContext<EntityStore, BreakBlockEvent> context) {
        Progression.super.onSystem(playerRef, context);
    }

    //    @Component
    private static class HarvestSkill implements ProgressionSkill<FarmingProgression, EventSystemContext<EntityStore, BreakBlockEvent>> {

        @Override
        public String getSkillName() {
            return "Harvest";
        }

        @Override
        public String getDescription() {
            return "Automatically replants crops when harvested.";
        }

        @Override
        public int getRequiredLevel() {
            return 5;
        }

        @Override
        public void onUnlock(final PlayerRef playerRef) {
        }

        @Override
        public void onSystem(final PlayerRef playerRef, final ProgressionStatus progressionStatus, final EventSystemContext<EntityStore, BreakBlockEvent> context) {
            ProgressionSkill.super.onSystem(playerRef, progressionStatus, context);
        }
    }
}