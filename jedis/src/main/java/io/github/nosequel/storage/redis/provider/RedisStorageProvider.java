package io.github.nosequel.storage.redis.provider;

import io.github.nosequel.storage.redis.RedisStorageHandler;
import io.github.nosequel.storage.serialization.Serializer;
import io.github.nosequel.storage.storage.StorageProvider;
import io.github.nosequel.storage.redis.provider.redis.CachedRedisStorageProvider;
import lombok.Getter;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

@Getter
public class RedisStorageProvider<T> extends StorageProvider<String, T> {

    protected final String key;
    protected final RedisStorageHandler storageHandler;

    protected final Class<T> clazz;
    protected final Serializer<T> serializer;

    /**
     * Constructor to make a new {@link CachedRedisStorageProvider} object.
     *
     * @param key            the key to get/set the data to in redis.
     * @param storageHandler the redis handler to get the {@link redis.clients.jedis.JedisPool} from.
     * @param clazz          the class of the generic {@link T} type.
     */
    public RedisStorageProvider(String key, RedisStorageHandler storageHandler, Class<T> clazz) {
        this.key = key;
        this.storageHandler = storageHandler;
        this.clazz = clazz;

        this.serializer = storageHandler.getSerializer(this.clazz);
    }

    /**
     * Set an entry within the storage object.
     *
     * @param key   the key of the entry
     * @param value the entry itself
     */
    @Override
    public void setEntry(String key, T value) {
        ForkJoinPool.commonPool().execute(() -> {
            final Jedis jedis = this.storageHandler.getResource();

            jedis.hset(this.key, key, this.serializer.serialize(value));
            jedis.close();
        });
    }

    /**
     * Remove an entry within the storage object.
     * <p>
     * This method will remove the entry by the key itself,
     * consult to {@link StorageProvider#removeEntryValue(Object)}
     * if you want to remove by the value instead.
     * </p>
     *
     * @param key the key to remove from the entry
     */
    @Override
    public void removeEntry(String key) {
        ForkJoinPool.commonPool().execute(() -> {
            final Jedis jedis = this.storageHandler.getResource();

            jedis.hdel(this.key, key);
            jedis.close();
        });
    }

    /**
     * Remove an entry within the storage object.
     * <p>
     * This method will remove the entry by the value itself,
     * consult to {@link StorageProvider#removeEntry(Object)}
     * if you want to remove by the key instead.
     * </p>
     *
     * @param value the value to remove from the storage object
     */
    @Override
    public void removeEntryValue(T value) {
        throw new UnsupportedOperationException("Currently unsupported within the RedisStorageProvider");
    }

    /**
     * Fetch an entry from the storage by a {@link String}.
     *
     * @param key the key to get the entry from
     * @return the entry, or null
     */
    @Override
    public CompletableFuture<T> fetchEntry(String key) {
        return CompletableFuture.supplyAsync(() -> {
            final Jedis jedis = this.storageHandler.getResource();
            final String value = jedis.hget(this.key, key);

            jedis.close();

            return this.serializer.deserialize(value);
        });
    }

    /**
     * Fetch all entries from the storage.
     *
     * @return all of the found entries by the same key within the database
     */
    @Override
    public CompletableFuture<Map<String, T>> fetchAllEntries() {
        return CompletableFuture.supplyAsync(() -> {
            final Jedis jedis = this.storageHandler.getResource();

            final Map<String, T> returnMap = new HashMap<>();
            final Map<String, String> map = jedis.hgetAll(this.key);

            for (Map.Entry<String, String> entry : map.entrySet()) {
                returnMap.put(entry.getKey(), this.serializer.deserialize(entry.getValue()));
            }

            jedis.close();

            return returnMap;
        });
    }
}