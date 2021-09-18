package io.github.nosequel.storage.sql.settings.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.nosequel.storage.sql.settings.SqlSettings;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.Properties;

@RequiredArgsConstructor
public class JdbcPasswordSqlSettings extends SqlSettings {

    private final String jdbcUrl;

    private final String username;
    private final String password;

    @Override
    @SneakyThrows
    public HikariDataSource createObject() {
        final HikariConfig config = new HikariConfig();

        config.setJdbcUrl(this.jdbcUrl);
        config.setUsername(this.username);
        config.setPassword(this.password);

        return new HikariDataSource(config);
    }
}