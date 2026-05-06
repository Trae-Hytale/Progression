package me.trae.progression.progression;

import io.github.trae.database.driver.DatabaseDriver;
import io.github.trae.database.repository.AbstractRepository;
import io.github.trae.database.repository.annotations.Repository;
import me.trae.progression.progression.properties.ProgressionDataProperty;

@Repository(databaseName = "Progressions", collectionName = "Progressions")
public class ProgressionRepository extends AbstractRepository<ProgressionData, ProgressionDataProperty> {

    public ProgressionRepository(final DatabaseDriver databaseDriver) {
        super(databaseDriver);
    }

    @Override
    public void registerIndexes() {
    }
}