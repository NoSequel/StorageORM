package io.github.nosequel.storage.sql.settings;

import com.zaxxer.hikari.HikariDataSource;
import io.github.nosequel.storage.settings.Settings;

public abstract class SqlSettings implements Settings<HikariDataSource, HikariDataSource> {

    @Override
    public HikariDataSource auth(HikariDataSource object) {
        throw new UnsupportedOperationException("MongoSettings#auth is an unsupported operation.");
    }
}
