package me.trae.progression.progression;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.util.NotificationUtil;
import io.github.trae.di.annotations.type.component.Service;
import io.github.trae.hf.Manager;
import io.github.trae.hytale.framework.utility.UtilEvent;
import io.github.trae.hytale.framework.utility.UtilMessage;
import io.github.trae.hytale.framework.utility.enums.ChatColor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.trae.core.client.ClientManager;
import me.trae.progression.ProgressionPlugin;
import me.trae.progression.progression.data.ProgressionStatus;
import me.trae.progression.progression.events.ProgressionGainExperienceEvent;
import me.trae.progression.progression.events.ProgressionLevelUpEvent;
import me.trae.progression.progression.interfaces.IProgressionManager;
import me.trae.progression.progression.progressions.Progression;
import me.trae.progression.progression.properties.ProgressionDataProperty;
import me.trae.progression.progression.storages.ProgressionIdStorage;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Getter
@Service
public class ProgressionManager implements Manager<ProgressionPlugin>, IProgressionManager {

    private final ProgressionIdStorage progressionIdStorage = new ProgressionIdStorage();

    private final ProgressionRepository repository;

    private final ClientManager clientManager;

    @Override
    public List<ProgressionData> getProgressionDataList() {
        return this.progressionIdStorage.getValues();
    }

    @Override
    public void addProgressionData(final ProgressionData progressionData) {
        this.progressionIdStorage.index(progressionData);
    }

    @Override
    public void removeProgressionData(final ProgressionData progressionData) {
        this.progressionIdStorage.unIndex(progressionData);
    }

    @Override
    public Optional<ProgressionData> getProgressionDataByPlayer(final PlayerRef playerRef) {
        return this.progressionIdStorage.get(playerRef.getUuid());
    }

    @Override
    public void giveExperience(final PlayerRef playerRef, final Progression<?> progression, final int amount) {
        boolean created = false;

        ProgressionData progressionData = this.getProgressionDataByPlayer(playerRef).orElse(null);
        if (progressionData == null) {
            progressionData = new ProgressionData(playerRef);
            this.addProgressionData(progressionData);
            created = true;
        }

        ProgressionStatus progressionStatus = progressionData.getProgressionStatus(progression).orElse(null);
        if (progressionStatus == null) {
            progressionStatus = new ProgressionStatus(progression);
            progressionStatus.setLevel(1);
            progressionData.addProgressionStatus(progressionStatus);
        }

        if (progressionStatus.getLevel() >= progression.getMaxLevel()) {
            return;
        }

        progressionStatus.setExperience(progressionStatus.getExperience() + amount);

        final int previousLevel = progressionStatus.getLevel();

        while (progressionStatus.getLevel() < progression.getMaxLevel()) {
            final int requiredExperience = progression.getRequiredExperience(progressionStatus.getLevel() + 1);

            if (progressionStatus.getExperience() < requiredExperience) {
                break;
            }

            progressionStatus.setExperience(progressionStatus.getExperience() - requiredExperience);
            progressionStatus.setLevel(progressionStatus.getLevel() + 1);

            progression.onLevelUp(playerRef, progressionStatus.getLevel());
        }

        progressionStatus.setLastUpdatedAt(System.currentTimeMillis());

        if (progressionStatus.getLevel() > previousLevel) {
            UtilEvent.dispatch(new ProgressionLevelUpEvent(progression, playerRef, previousLevel, progressionStatus.getLevel()));

            NotificationUtil.sendNotification(
                    playerRef.getPacketHandler(),
                    Message.raw("%s Level Up!".formatted(progression.getProgressionName())).color(ChatColor.GREEN.getColor()),
                    Message.raw("Level %s → %s".formatted(previousLevel, progressionStatus.getLevel())).color(ChatColor.GOLD.getColor())
            );

            UtilMessage.message(playerRef, progression.getProgressionName(), "Level up! <green>%s</green> to <green>%s</green>".formatted(previousLevel, progressionStatus.getLevel()));
        } else {
            UtilEvent.dispatch(new ProgressionGainExperienceEvent(progression, playerRef, amount));

            NotificationUtil.sendNotification(
                    playerRef.getPacketHandler(),
                    Message.raw("%s +%s XP".formatted(progression.getProgressionName(), amount)).color(ChatColor.GREEN.getColor()),
                    Message.raw("%s/%s".formatted(progressionStatus.getExperience(), progression.getRequiredExperience(progressionStatus.getLevel() + 1))).color(ChatColor.GOLD.getColor())
            );
        }

        if (created) {
            this.repository.save(progressionData);
        } else {
            this.repository.update(progressionData, ProgressionDataProperty.PROGRESSIONS);
        }
    }
}