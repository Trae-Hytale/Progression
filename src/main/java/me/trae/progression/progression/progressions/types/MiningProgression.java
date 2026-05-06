package me.trae.progression.progression.progressions.types;

import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.item.ItemComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.trae.di.annotations.type.component.Component;
import io.github.trae.hytale.framework.system.data.EventSystemContext;
import io.github.trae.hytale.framework.utility.UtilMessage;
import me.trae.core.utility.enums.BlockOreType;
import me.trae.progression.progression.progressions.Progression;
import me.trae.progression.progression.progressions.ProgressionSkill;

import java.awt.*;
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
        return BlockOreType.getById(blockType.getId()).map(blockOreType -> {
            final String name = blockOreType.name();

            if (name.startsWith("COPPER")) {
                return 5;
            } else if (name.startsWith("IRON")) {
                return 10;
            } else if (name.startsWith("GOLD")) {
                return 20;
            } else if (name.startsWith("THORIUM")) {
                return 30;
            } else if (name.startsWith("COBALT")) {
                return 40;
            } else if (name.startsWith("SILVER")) {
                return 55;
            } else if (name.startsWith("ADAMANTITE")) {
                return 75;
            } else if (name.startsWith("MITHRIL")) {
                return 100;
            } else {
                return 0;
            }
        }).orElse(0);
    }

    private BlockOreType getDroppedBlockByBlockType(final BlockType blockType) {
        return BlockOreType.getById(blockType.getId()).map(blockOreType -> {
            final String name = blockOreType.name();

            if (name.startsWith("COPPER")) {
                return BlockOreType.COPPER;
            } else if (name.startsWith("IRON")) {
                return BlockOreType.IRON;
            } else if (name.startsWith("GOLD")) {
                return BlockOreType.GOLD;
            } else if (name.startsWith("THORIUM")) {
                return BlockOreType.THORIUM;
            } else if (name.startsWith("COBALT")) {
                return BlockOreType.COBALT;
            } else if (name.startsWith("SILVER")) {
                return BlockOreType.SILVER;
            } else if (name.startsWith("ADAMANTITE")) {
                return BlockOreType.ADAMANTITE;
            } else if (name.startsWith("MITHRIL")) {
                return BlockOreType.MITHRIL;
            } else {
                return null;
            }
        }).orElse(null);
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
            final BreakBlockEvent event = context.getEvent();

            final BlockType blockType = event.getBlockType();

            final BlockOreType droppedBlock = this.getModule().getDroppedBlockByBlockType(blockType);
            if (droppedBlock == null) {
                return;
            }

            if (ThreadLocalRandom.current().nextInt(100) >= 50) {
                return;
            }

            final Holder<EntityStore> itemEntityStoreHolder = ItemComponent.generateItemDrop(context.getStore(), new ItemStack(droppedBlock.getId()), event.getTargetBlock().toVector3d(), Vector3f.ZERO, 0.0F, 0.5F, 0.0F);

            if (itemEntityStoreHolder != null) {
                context.getCommandBuffer().addEntity(itemEntityStoreHolder, AddReason.SPAWN);

                UtilMessage.message(playerRef, this.getModule().getProgressionName(), "<green>%s</green> activated! Double ore dropped.".formatted(this.getSkillName()));
            }
        }
    }
}