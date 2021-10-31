package io.github.nosequel.storage.sql.provider;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zaxxer.hikari.HikariDataSource;
import io.github.nosequel.storage.StorageHandler;
import io.github.nosequel.storage.serialization.Serializer;
import io.github.nosequel.storage.sql.SqlStorageHandler;
import io.github.nosequel.storage.storage.StorageProvider;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Deprecated
public class DynamicSqlStorageProvider<T> extends StorageProvider<String, T> {

    private final StorageHandler storageHandler;
    private final HikariDataSource dataSource;

    private final String table;
    private final Serializer<T> serializer;

    @Deprecated
    public DynamicSqlStorageProvider(SqlStorageHandler storageHandler, String table, Class<T> clazz) {
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

                final StringBuilder valueStatementPart = new StringBuilder("VALUES (?, ");
                final StringBuilder statement = new StringBuilder("INSERT INTO ")
                        .append(this.table)
                        .append("(_id, ");

                final List<Map.Entry<String, JsonElement>> entries = new ArrayList<>(object.getAsJsonObject().entrySet());

                for (int i = 0; i < entries.size(); i++) {
                    final Map.Entry<String, JsonElement> entry = entries.get(i);

                    statement.append(entry.getKey());
                    valueStatementPart.append("?");

                    if (i < entries.size() - 1) {
                        statement.append(", ");
                        valueStatementPart.append(", ");
                    } else {
                        statement.append(")");
                        valueStatementPart.append(")");
                    }
                }

                statement.append(valueStatementPart);

                final PreparedStatement preparedStatement = this.dataSource.getConnection().prepareStatement(
                        statement.toString()
                );

                preparedStatement.setString(1, key);

                for (int i = 0; i < entries.size(); i++) {
                    final Map.Entry<String, JsonElement> entry = entries.get(i);

                    preparedStatement.setString(i + 2, entry.getValue().toString());
                }

                preparedStatement.executeUpdate();
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

                final JsonObject object = new JsonObject();
                final AtomicInteger index = new AtomicInteger(2);
                String element;

                //while ((element = resultSet.getString(index.getAndIncrement())) != null) {
                //    object.add(element);
                //}

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