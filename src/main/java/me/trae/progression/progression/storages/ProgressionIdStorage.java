package me.trae.progression.progression.storages;

import io.github.trae.database.storage.LocalStorage;
import me.trae.progression.progression.ProgressionData;

import java.util.UUID;

public class ProgressionIdStorage extends LocalStorage<UUID, ProgressionData> {

    @Override
    public void index(final ProgressionData progressionData) {
        this.put(progressionData.getId(), progressionData);
    }

    @Override
    public void unIndex(final ProgressionData progressionData) {
        this.remove(progressionData.getId());
    }
}