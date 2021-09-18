package io.github.nosequel.storage.mongo.provider;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import io.github.nosequel.storage.mongo.MongoStorageHandler;
import io.github.nosequel.storage.serialization.Serializer;
import io.github.nosequel.storage.storage.StorageProvider;
import lombok.Getter;
import org.bson.Document;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

@Getter
public class MongoStorageProvider<T> extends StorageProvider<String, T> {

    protected final MongoStorageHandler storageHandler;

    protected final Class<T> clazz;
    protected final Serializer<T> serializer;

    private final MongoCollection<Document> collection;

    /**
     * Constructor to make a new {@link MongoStorageProvider} object.
     *
     * @param collection     the collection to read from/write to in the database
     * @param storageHandler the handler to get the mongo data from
     * @param clazz          the class of the generic {@link T} type.
     */
    public MongoStorageProvider(String collection, MongoStorageHandler storageHandler, Class<T> clazz, Serializer<T> serializer) {
        this.collection = storageHandler.getDatabase().getCollection(collection);
        this.storageHandler = storageHandler;
        this.clazz = clazz;

        this.serializer = storageHandler.getSerializer(clazz);
    }

    @Override
    public void setEntry(String key, T value) {
        ForkJoinPool.commonPool().execute(() -> this.collection.updateOne(
                Filters.eq("_id", key),
                new Document("$set", Document.parse(this.serializer.serialize(value).toString())),
                new UpdateOptions().upsert(true)
        ));
    }

    @Override
    public void removeEntry(String key) {
        ForkJoinPool.commonPool().execute(() -> this.collection.deleteOne(Filters.eq("_id", key)));
    }

    @Override
    public void removeEntryValue(T value) {
        throw new UnsupportedOperationException("That operation is not supported within the MongoStorageProvider.");
    }

    @Override
    public CompletableFuture<T> fetchEntry(String key) {
        return CompletableFuture.supplyAsync(() -> {
            final Document document = this.collection.find(Filters.eq("_id", key)).first();

            if (document == null) {
                return null;
            }

            return this.serializer.deserialize(document.toJson());
        });
    }

    @Override
    public CompletableFuture<Map<String, T>> fetchAllEntries() {
        return CompletableFuture.supplyAsync(() -> {
            final Map<String, T> entries = new HashMap<>();

            for (Document document : this.collection.find()) {
                entries.put(document.getString("_id"), this.serializer.deserialize(document.toJson()));
            }

            return entries;
        });
    }
}