package me.trae.progression.progression.progressions.types;

import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.math.vector.Rotation3f;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.item.ItemComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.trae.di.annotations.type.component.Component;
import io.github.trae.hytale.framework.system.data.EventSystemContext;
import io.github.trae.hytale.framework.utility.UtilMessage;
import io.github.trae.hytale.framework.wrappers.BlockLocation;
import me.trae.core.utility.enums.WoodType;
import me.trae.progression.progression.data.ProgressionStatus;
import me.trae.progression.progression.progressions.Progression;
import me.trae.progression.progression.progressions.ProgressionSkill;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

@Component
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
        final BreakBlockEvent event = context.getEvent();

        final int experience = this.getExperienceForBlock(event.getBlockType());
        if (experience == 0) {
            return;
        }

        this.getManager().giveExperience(playerRef, this, experience);
    }

    private int getExperienceForBlock(final BlockType blockType) {
        return WoodType.getById(blockType.getId()).map(woodType -> switch (woodType) {
            case OAK, ASH, BEECH, ASPEN, BIRCH, MAPLE -> 5;
            case AZURE, APPLE, FIR, CEDAR -> 10;
            case DRY, BOTTLETREE, PALO, GUMBOAB -> 15;
            case AMBER, SALLOW, PALM, REDWOOD -> 20;
            case WINDWILLOW, STORMBARK, BAMBOO -> 30;
            case JUNGLE, CAMPHOR, BANYAN, BLUE_FIG -> 40;
            case FIRE, BURNT, POISON, PETRIFIED -> 55;
            case WISTERIA, SPIRAL -> 75;
            case CRYSTALWOOD, FROSTWOOD -> 100;
        }).orElse(0);
    }

    @Component
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
        public void onSystem(final PlayerRef playerRef, final ProgressionStatus progressionStatus, final EventSystemContext<EntityStore, BreakBlockEvent> context) {
            if (!(progressionStatus.hasLevel(this.getRequiredLevel()))) {
                return;
            }

            final BreakBlockEvent event = context.getEvent();

            final BlockType blockType = event.getBlockType();

            if (ThreadLocalRandom.current().nextInt(100) >= 50) {
                return;
            }

            final BlockLocation blockLocation = BlockLocation.of(context.getStore().getExternalData().getWorld(), event.getTargetBlock());

            final Holder<EntityStore> itemEntityStoreHolder = ItemComponent.generateItemDrop(context.getStore(), new ItemStack(blockType.getId()), blockLocation.getPosition3d(), Rotation3f.ZERO, 0.0F, 0.5F, 0.0F);

            if (itemEntityStoreHolder != null) {
                context.getCommandBuffer().addEntity(itemEntityStoreHolder, AddReason.SPAWN);

                UtilMessage.message(playerRef, this.getModule().getProgressionName(), "<green>%s</green> activated! Double log dropped.".formatted(this.getSkillName()));
            }
        }
    }
}