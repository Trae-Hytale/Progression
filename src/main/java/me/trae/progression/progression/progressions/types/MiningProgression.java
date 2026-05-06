package me.trae.progression.progression.progressions.types;

import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.trae.di.annotations.type.component.Component;
import io.github.trae.hytale.framework.system.data.EventSystemContext;
import io.github.trae.hytale.framework.utility.UtilMessage;
import me.trae.core.utility.enums.BlockOreType;
import me.trae.progression.progression.progressions.Progression;
import me.trae.progression.progression.progressions.ProgressionSkill;

import java.awt.*;

@Component
public class MiningProgression implements Progression<EventSystemContext<EntityStore, BreakBlockEvent>> {

    @Override
    public String getId() {
        return "c48b2085-57d9-4851-9382-4d5251dd291c";
    }

    @Override
    public String getProgressionName() {
        return "Mining";
    }

    @Override
    public Color getColor() {
        return new Color(85, 255, 255);
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
        return 50.0D;
    }

    @Override
    public double getExponent() {
        return 2.0D;
    }

    @Override
    public void onSystem(final PlayerRef playerRef, final EventSystemContext<EntityStore, BreakBlockEvent> context) {
        final BreakBlockEvent event = context.getEvent();

        final int experience = this.getExperienceForBlock(event.getBlockType());
        if (experience == 0) {
            return;
        }

        this.getManager().giveExperience(playerRef, this, experience);
    }

    private int getExperienceForBlock(final BlockType blockType) {
        return BlockOreType.getByName(blockType.getId()).map(blockOreType -> {
            final String name = blockOreType.name();

            if (name.startsWith("COPPER")) {
                return 5;
            } else if (name.startsWith("IRON")) {
                return 10;
            } else if (name.startsWith("GOLD")) {
                return 20;
            } else if (name.endsWith("THORIUM")) {
                return 30;
            } else if (name.endsWith("COBALT")) {
                return 40;
            } else if (name.endsWith("SILVER")) {
                return 55;
            } else if (name.endsWith("ADAMANTITE")) {
                return 75;
            } else if (name.endsWith("MITHRIL")) {
                return 100;
            } else {
                return 0;
            }
        }).orElse(0);
    }

    @Component
    private static class DoubleOreSkill implements ProgressionSkill<MiningProgression, EventSystemContext<EntityStore, BreakBlockEvent>> {

        @Override
        public String getSkillName() {
            return "Double Ore";
        }

        @Override
        public String getDescription() {
            return "Chance to receive double ore drops when mining.";
        }

        @Override
        public int getRequiredLevel() {
            return 5;
        }

        @Override
        public void onSystem(final PlayerRef playerRef, final EventSystemContext<EntityStore, BreakBlockEvent> context) {
            UtilMessage.message(playerRef, this.getProgression().getProgressionName(), "Executed <gold>%s</gold> Skill!".formatted(this.getSkillName()));
        }
    }
}