package me.trae.progression.progression.progressions.types;

import com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.trae.hytale.framework.system.data.EventSystemContext;
import me.trae.progression.progression.progressions.Progression;
import me.trae.progression.progression.progressions.ProgressionSkill;

import java.awt.*;

//@Component
public class WoodcuttingProgression implements Progression<EventSystemContext<EntityStore, BreakBlockEvent>> {

    @Override
    public String getId() {
        return "820a4669-761d-49ba-961a-34aba1ebb6a5";
    }

    @Override
    public String getProgressionName() {
        return "Woodcutting";
    }

    @Override
    public Color getColor() {
        return new Color(0, 170, 0);
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
        return 40.0D;
    }

    @Override
    public double getExponent() {
        return 1.8D;
    }

    @Override
    public void onSystem(final PlayerRef playerRef, final EventSystemContext<EntityStore, BreakBlockEvent> context) {
        Progression.super.onSystem(playerRef, context);
    }

    //    @Component
    private static class LumberjackSkill implements ProgressionSkill<WoodcuttingProgression, EventSystemContext<EntityStore, BreakBlockEvent>> {

        @Override
        public String getSkillName() {
            return "Lumberjack";
        }

        @Override
        public String getDescription() {
            return "Chance to receive double log drops when chopping trees.";
        }

        @Override
        public int getRequiredLevel() {
            return 5;
        }

        @Override
        public void onSystem(final PlayerRef playerRef, final EventSystemContext<EntityStore, BreakBlockEvent> context) {
            ProgressionSkill.super.onSystem(playerRef, context);
        }
    }
}