package me.trae.progression.progression.commands;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import io.github.trae.di.annotations.type.component.Component;
import io.github.trae.hytale.framework.utility.UtilMessage;
import me.trae.core.client.enums.Rank;
import me.trae.core.command.Command;
import me.trae.progression.ProgressionPlugin;
import me.trae.progression.progression.ProgressionData;
import me.trae.progression.progression.ProgressionManager;

@Component
public class ProgressionCommand extends Command<ProgressionPlugin, ProgressionManager, PlayerRef> {

    public ProgressionCommand() {
        super("progression", "Progression management", Rank.OWNER);
    }

    @Override
    public void execute(final PlayerRef playerRef, final String[] args) {
        int count = 0;

        for (final ProgressionData progressionData : this.getManager().getProgressionDataList()) {
            this.getManager().removeProgressionData(progressionData);
            this.getManager().getRepository().delete(progressionData);
            count++;
        }

        UtilMessage.message(playerRef, "Progression", "Deleted: <yellow>%s</yellow>".formatted(count));
    }
}