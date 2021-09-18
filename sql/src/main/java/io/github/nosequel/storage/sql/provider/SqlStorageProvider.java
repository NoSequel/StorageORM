package io.github.nosequel.storage.sql.provider;

import com.google.gson.JsonElement;
import com.zaxxer.hikari.HikariDataSource;
import io.github.nosequel.storage.StorageHandler;
import io.github.nosequel.storage.serialization.Serializer;
import io.github.nosequel.storage.sql.SqlStorageHandler;
import io.github.nosequel.storage.storage.StorageProvider;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

@Getter
public class SqlStorageProvider<T> extends StorageProvider<String, T> {

    private final StorageHandler storageHandler;
    private final HikariDataSource dataSource;

    private final String table;
    private final Serializer<T> serializer;

    public SqlStorageProvider(SqlStorageHandler storageHandler, String table, Class<T> clazz) {
        this.storageHandler = storageHandler;
        this.table = table;

        this.serializer = storageHandler.getSerializer(clazz);
        this.dataSource = storageHandler.getAuthSettings().createObject();
    }

    @Override
    public void setEntry(String key, T value) {
        ForkJoinPool.commonPool().execute(() -> {
            try {
                this.dataSource.getConnection().prepareStatement(
                        "DELETE FROM " + this.table + " WHERE _id='" + key + "'"
                ).executeUpdate();

                final JsonElement object = this.serializer.serialize(value);
                final PreparedStatement statement = this.dataSource.getConnection().prepareStatement(
                        "INSERT INTO " + this.table + " (_id, json) VALUES (?, ?)"
                );

                statement.setString(1, key);
                statement.setString(2, object.toString());

                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void removeEntry(String key) {
        ForkJoinPool.commonPool().execute(() -> {
            try {
                this.dataSource.getConnection().prepareStatement(
                        "DELETE FROM " + this.table + " WHERE _id='" + key + "'"
                ).executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void removeEntryValue(T value) {
        throw new UnsupportedOperationException("That operation is not supported within the SqlStorageProvider.");
    }

    @Override
    public CompletableFuture<T> fetchEntry(String key) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                final PreparedStatement statement = this.dataSource.getConnection().prepareStatement(
                        "SELECT * FROM " + this.table + " WHERE _id='" + key + "'"
                );

                final ResultSet resultSet = statement.executeQuery();
                resultSet.next();

                return this.serializer.deserialize(resultSet.getString(2));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    @Override
    public CompletableFuture<Map<String, T>> fetchAllEntries() {
        throw new UnsupportedOperationException("That operation is not supported within the SqlStorageProvider.");
    }
}