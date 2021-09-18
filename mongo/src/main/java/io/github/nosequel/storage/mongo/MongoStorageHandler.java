package io.github.nosequel.storage.mongo;

import com.mongodb.client.MongoDatabase;
import io.github.nosequel.storage.StorageHandler;
import io.github.nosequel.storage.mongo.settings.MongoSettings;
import io.github.nosequel.storage.serialization.Serializer;
import lombok.Getter;

@Getter
public class MongoStorageHandler extends StorageHandler {

    private final MongoSettings authSettings;
    private final MongoDatabase database;

    public MongoStorageHandler(MongoSettings settings) {
        this.authSettings = settings;
        this.database = settings.createObject();
    }
}
