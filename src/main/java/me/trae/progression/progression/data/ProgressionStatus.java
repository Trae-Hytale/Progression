package me.trae.progression.progression.data;

import io.github.trae.database.domain.data.DomainData;
import io.github.trae.database.domain.models.SubDomain;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.trae.progression.progression.data.properties.ProgressionStatusProperty;
import me.trae.progression.progression.progressions.Progression;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@Setter
public class ProgressionStatus implements SubDomain<ProgressionStatusProperty> {

    private final UUID id;

    private int level, experience;

    private long createdAt, lastUpdatedAt;

    public ProgressionStatus(final DomainData<ProgressionStatusProperty> domainData) {
        this(domainData.getIdentifier());

        this.level = domainData.get(Integer.class, ProgressionStatusProperty.LEVEL);
        this.experience = domainData.get(Integer.class, ProgressionStatusProperty.EXPERIENCE);
        this.createdAt = domainData.get(Long.class, ProgressionStatusProperty.CREATED_AT);
        this.lastUpdatedAt = domainData.get(Long.class, ProgressionStatusProperty.LAST_UPDATED_AT);
    }

    public ProgressionStatus(final Progression<?> progression) {
        this(UUID.fromString(progression.getId()));

        this.createdAt = System.currentTimeMillis();
        this.lastUpdatedAt = System.currentTimeMillis();
    }

    @Override
    public Object getValueByProperty(final ProgressionStatusProperty progressionStatusProperty) {
        return switch (progressionStatusProperty) {
            case LEVEL -> this.getLevel();
            case EXPERIENCE -> this.getExperience();
            case CREATED_AT -> this.getCreatedAt();
            case LAST_UPDATED_AT -> this.getLastUpdatedAt();
        };
    }
}