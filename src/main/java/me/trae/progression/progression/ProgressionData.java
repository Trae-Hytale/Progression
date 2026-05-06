package me.trae.progression.progression;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import io.github.trae.database.domain.data.DomainData;
import io.github.trae.database.domain.models.Domain;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.trae.progression.progression.data.ProgressionStatus;
import me.trae.progression.progression.data.properties.ProgressionStatusProperty;
import me.trae.progression.progression.interfaces.IProgressionData;
import me.trae.progression.progression.progressions.Progression;
import me.trae.progression.progression.properties.ProgressionDataProperty;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@Setter
public class ProgressionData implements Domain<ProgressionDataProperty>, IProgressionData {

    private final UUID id;

    private final LinkedHashMap<UUID, ProgressionStatus> progressions = new LinkedHashMap<>();

    public ProgressionData(final DomainData<ProgressionDataProperty> domainData) {
        this(domainData.getIdentifier());

        this.progressions.putAll(domainData.<ProgressionStatusProperty, ProgressionStatus>getSubDomainMap(ProgressionDataProperty.PROGRESSIONS, ProgressionStatus::new));
    }

    public ProgressionData(final PlayerRef playerRef) {
        this(playerRef.getUuid());
    }

    @Override
    public Object getValueByProperty(final ProgressionDataProperty progressionProperty) {
        return switch (progressionProperty) {
            case PROGRESSIONS -> this.getProgressions();
        };
    }

    @Override
    public void addProgressionStatus(final ProgressionStatus progressionStatus) {
        this.progressions.put(progressionStatus.getId(), progressionStatus);
    }

    @Override
    public void removeProgressionStatus(final ProgressionStatus progressionStatus) {
        this.progressions.remove(progressionStatus.getId());
    }

    @Override
    public Optional<ProgressionStatus> getProgressionStatus(final Progression progressionType) {
        return Optional.ofNullable(this.progressions.get(UUID.fromString(progressionType.getId())));
    }
}