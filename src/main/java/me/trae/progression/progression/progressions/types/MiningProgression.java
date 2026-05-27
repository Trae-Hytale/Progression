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
import me.trae.core.utility.enums.OreType;
import me.trae.progression.progression.data.ProgressionStatus;
import me.trae.progression.progression.progressions.Progression;
import me.trae.progression.progression.progressions.ProgressionSkill;

import java.awt.*;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

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
        return OreType.getById(blockType.getId()).map(oreType -> switch (oreType) {
            case COPPER -> 5;
            case IRON -> 10;
            case GOLD -> 20;
            case THORIUM -> 30;
            case COBALT -> 40;
            case SILVER -> 55;
            case ADAMANTITE -> 75;
            case MITHRIL -> 100;
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
        public void onSystem(final PlayerRef playerRef, final ProgressionStatus progressionStatus, final EventSystemContext<EntityStore, BreakBlockEvent> context) {
            if (!(progressionStatus.hasLevel(this.getRequiredLevel()))) {
                return;
            }

            final BreakBlockEvent event = context.getEvent();

            final BlockType blockType = event.getBlockType();

            final Optional<OreType> oreTypeOptional = OreType.getById(blockType.getId());
            if (oreTypeOptional.isEmpty()) {
                return;
            }

            if (ThreadLocalRandom.current().nextInt(100) >= 50) {
                return;
            }

            final BlockLocation blockLocation = BlockLocation.of(context.getStore().getExternalData().getWorld(), event.getTargetBlock());

            final Holder<EntityStore> itemEntityStoreHolder = ItemComponent.generateItemDrop(context.getStore(), new ItemStack(oreTypeOptional.get().getDroppedBlockTypeId()), blockLocation.getPosition3d(), Rotation3f.ZERO, 0.0F, 0.5F, 0.0F);

            if (itemEntityStoreHolder != null) {
                context.getCommandBuffer().addEntity(itemEntityStoreHolder, AddReason.SPAWN);

                UtilMessage.message(playerRef, this.getModule().getProgressionName(), "<green>%s</green> activated! Double ore dropped.".formatted(this.getSkillName()));
            }
        }
    }
}