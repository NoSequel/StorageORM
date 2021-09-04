package io.github.nosequel.storage.mongo.settings;

import com.mongodb.client.MongoDatabase;
import io.github.nosequel.storage.settings.Settings;

public abstract class MongoSettings implements Settings<MongoDatabase, MongoDatabase> {
    @Override
    public MongoDatabase auth(MongoDatabase object) {
        throw new UnsupportedOperationException("MongoSettings#auth is an unsupported operation.");
    }
}
