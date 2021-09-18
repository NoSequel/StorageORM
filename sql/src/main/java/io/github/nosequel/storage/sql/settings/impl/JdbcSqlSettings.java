package io.github.nosequel.storage.sql.settings.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.nosequel.storage.sql.settings.SqlSettings;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JdbcSqlSettings extends SqlSettings {

    private final String jdbcUrl;

    @Override
    public HikariDataSource createObject() {
        final HikariConfig config = new HikariConfig();

        config.setJdbcUrl(this.jdbcUrl);

        return new HikariDataSource(config);
    }
}
