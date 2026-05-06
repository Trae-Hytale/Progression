package me.trae.progression.progression.modules.systems;

import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.trae.di.annotations.type.DependsOn;
import io.github.trae.di.annotations.type.component.Component;
import io.github.trae.hf.Module;
import io.github.trae.hytale.framework.system.SystemListener;
import io.github.trae.hytale.framework.system.annotations.EventSystemHandler;
import io.github.trae.hytale.framework.system.data.EventSystemContext;
import io.github.trae.utilities.UtilJava;
import me.trae.progression.ProgressionPlugin;
import me.trae.progression.progression.ProgressionData;
import me.trae.progression.progression.ProgressionManager;
import me.trae.progression.progression.progressions.Progression;
import me.trae.progression.progression.progressions.ProgressionSkill;

import java.util.*;

@DependsOn(values = Progression.class)
@Component
public class ProgressionBreakBlockSystem implements Module<ProgressionPlugin, ProgressionManager>, SystemListener {

    private final List<Progression<EventSystemContext<EntityStore, BreakBlockEvent>>> progressionList;
    private final Map<Progression<EventSystemContext<EntityStore, BreakBlockEvent>>, List<ProgressionSkill<?, EventSystemContext<EntityStore, BreakBlockEvent>>>> progressionSkillMap;

    public ProgressionBreakBlockSystem(final List<Progression<EventSystemContext<EntityStore, BreakBlockEvent>>> progressionList, final List<ProgressionSkill<Progression<EventSystemContext<EntityStore, BreakBlockEvent>>, EventSystemContext<EntityStore, BreakBlockEvent>>> progressionSkillList) {
        this.progressionList = progressionList;
        this.progressionSkillMap = Collections.unmodifiableMap(UtilJava.createMap(new HashMap<>(), map -> {
            for (final ProgressionSkill<?, EventSystemContext<EntityStore, BreakBlockEvent>> progressionSkill : progressionSkillList) {
                map.computeIfAbsent(progressionSkill.getProgression(), __ -> new ArrayList<>()).add(progressionSkill);
            }
        }));
    }

    @EventSystemHandler(query = PlayerRef.class)
    public void onBreakBlock(final EventSystemContext<EntityStore, BreakBlockEvent> context) {
        final BreakBlockEvent event = context.getEvent();

        final BlockType blockType = event.getBlockType();
        if (blockType == BlockType.EMPTY) {
            return;
        }

        final PlayerRef playerRef = context.getComponent(PlayerRef.getComponentType());
        if (playerRef == null) {
            return;
        }

        final Optional<ProgressionData> progressionDataOptional = this.getManager().getProgressionDataByPlayer(playerRef);

        for (final Progression<EventSystemContext<EntityStore, BreakBlockEvent>> progression : this.progressionList) {
            progression.onSystem(playerRef, context);

            progressionDataOptional.flatMap(progressionData -> progressionData.getProgressionStatus(progression)).ifPresent(progressionStatus -> {
                for (final ProgressionSkill<?, EventSystemContext<EntityStore, BreakBlockEvent>> progressionSkill : this.progressionSkillMap.getOrDefault(progression, Collections.emptyList())) {
                    if (progressionStatus.getLevel() < progressionSkill.getRequiredLevel()) {
                        continue;
                    }

                    progressionSkill.onSystem(playerRef, context);
                }
            });
        }
    }
}