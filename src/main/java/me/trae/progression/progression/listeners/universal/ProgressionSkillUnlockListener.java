package me.trae.progression.progression.listeners.universal;

import io.github.trae.di.annotations.type.component.Component;
import io.github.trae.hf.Module;
import io.github.trae.hytale.framework.event.EventListener;
import io.github.trae.hytale.framework.event.annotations.EventHandler;
import io.github.trae.hytale.framework.event.constants.EventPriority;
import io.github.trae.hytale.framework.utility.UtilMessage;
import io.github.trae.utilities.UtilJava;
import me.trae.progression.ProgressionPlugin;
import me.trae.progression.progression.ProgressionManager;
import me.trae.progression.progression.events.ProgressionLevelUpEvent;
import me.trae.progression.progression.progressions.Progression;
import me.trae.progression.progression.progressions.ProgressionSkill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProgressionSkillUnlockListener implements Module<ProgressionPlugin, ProgressionManager>, EventListener {

    private final Map<Progression<?>, List<ProgressionSkill<?, ?>>> progressionSkillMap;

    public ProgressionSkillUnlockListener(final List<ProgressionSkill<?, ?>> progressionSkillList) {
        this.progressionSkillMap = Collections.unmodifiableMap(UtilJava.createMap(new HashMap<>(), map -> {
            for (final ProgressionSkill<?, ?> progressionSkill : progressionSkillList) {
                map.computeIfAbsent(progressionSkill.getModule(), __ -> new ArrayList<>()).add(progressionSkill);
            }
        }));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onProgressionLevelUp(final ProgressionLevelUpEvent event) {
        final List<ProgressionSkill<?, ?>> progressionSkillList = this.progressionSkillMap.get(event.getProgression());
        if (progressionSkillList == null) {
            return;
        }

        for (final ProgressionSkill<?, ?> progressionSkill : progressionSkillList) {
            if (progressionSkill.getRequiredLevel() == event.getLevel()) {
                UtilMessage.message(event.getPlayerRef(), progressionSkill.getModule().getProgressionName(), "You have unlocked <gold>%s</gold> for hitting level <green>%s</green>!".formatted(progressionSkill.getSkillName(), progressionSkill.getRequiredLevel()));

                progressionSkill.onUnlock(event.getPlayerRef());
            }
        }
    }
}