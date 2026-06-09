package me.trae.progression.progression.listeners;

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
import me.trae.core.client.Client;
import me.trae.progression.ProgressionPlugin;
import me.trae.progression.progression.ProgressionData;
import me.trae.progression.progression.ProgressionManager;
import me.trae.progression.progression.progressions.Progression;
import me.trae.progression.progression.progressions.ProgressionSkill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@DependsOn(values = Progression.class)
@Component
public class ProgressionBreakBlockListener implements Module<ProgressionPlugin, ProgressionManager>, SystemListener {

    private final List<Progression<EventSystemContext<EntityStore, BreakBlockEvent>>> progressionList;
    private final Map<Progression<EventSystemContext<EntityStore, BreakBlockEvent>>, List<ProgressionSkill<?, EventSystemContext<EntityStore, BreakBlockEvent>>>> progressionSkillMap;

    public ProgressionBreakBlockListener(final List<Progression<EventSystemContext<EntityStore, BreakBlockEvent>>> progressionList, final List<ProgressionSkill<Progression<EventSystemContext<EntityStore, BreakBlockEvent>>, EventSystemContext<EntityStore, BreakBlockEvent>>> progressionSkillList) {
        this.progressionList = progressionList;
        this.progressionSkillMap = Collections.unmodifiableMap(UtilJava.createMap(new HashMap<>(), map -> {
            for (final ProgressionSkill<?, EventSystemContext<EntityStore, BreakBlockEvent>> progressionSkill : progressionSkillList.stream().sorted(Comparator.comparingInt((ProgressionSkill<?, EventSystemContext<EntityStore, BreakBlockEvent>> skill) -> skill.getRequiredLevel()).reversed()).toList()) {
                map.computeIfAbsent(progressionSkill.getModule(), _ -> new ArrayList<>()).add(progressionSkill);
            }
        }));
    }

    @EventSystemHandler(query = PlayerRef.class)
    public void onBreakBlock(final EventSystemContext<EntityStore, BreakBlockEvent> context) {
        final BreakBlockEvent event = context.getEvent();

        if (event.isCancelled()) {
            return;
        }

        final BlockType blockType = event.getBlockType();
        if (blockType == BlockType.EMPTY) {
            return;
        }

        final PlayerRef playerRef = context.getComponent(PlayerRef.getComponentType());
        if (playerRef == null) {
            return;
        }

        if (this.getManager().getClientManager().getClientByPlayer(playerRef).map(Client::isAdministrating).orElse(false)) {
            return;
        }

        final Optional<ProgressionData> progressionDataOptional = this.getManager().getProgressionDataByPlayer(playerRef);

        for (final Progression<EventSystemContext<EntityStore, BreakBlockEvent>> progression : this.progressionList) {
            progression.onSystem(playerRef, context);

            progressionDataOptional.flatMap(progressionData -> progressionData.getProgressionStatus(progression)).ifPresent(progressionStatus -> {
                final Set<Class<?>> incompatibleSkills = new HashSet<>();

                for (final ProgressionSkill<?, EventSystemContext<EntityStore, BreakBlockEvent>> progressionSkill : this.progressionSkillMap.getOrDefault(progression, Collections.emptyList())) {
                    if (incompatibleSkills.contains(progressionSkill.getClass())) {
                        continue;
                    }

                    progressionSkill.onSystem(playerRef, progressionStatus, context);

                    incompatibleSkills.addAll(progressionSkill.getIncompatibleSkills());
                }
            });
        }
    }
}