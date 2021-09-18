package io.github.nosequel.storage.sql;

import io.github.nosequel.storage.StorageHandler;
import io.github.nosequel.storage.sql.settings.SqlSettings;
import lombok.Getter;

@Getter
public class SqlStorageHandler extends StorageHandler {

    private final SqlSettings authSettings;

    public SqlStorageHandler(SqlSettings settings) {
        this.authSettings = settings;
    }
}
