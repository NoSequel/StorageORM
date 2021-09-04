package io.github.nosequel.storage.mongo.settings.impl;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import io.github.nosequel.storage.mongo.settings.MongoSettings;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PasswordMongoSettings extends MongoSettings {

    private final String hostname;
    private final int port;

    private final String username;
    private final String password;

    private final String database;

    @Override
    public MongoDatabase createObject() {
        final MongoClient client = MongoClients.create("mongodb://" + username + ":" + password + "@" + hostname + ":" + port + "?authSource=" + database + "1&ssl=true");
        return client.getDatabase(this.database);
    }
}