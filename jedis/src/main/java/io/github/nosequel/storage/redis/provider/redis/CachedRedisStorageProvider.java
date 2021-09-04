package io.github.nosequel.storage.redis.provider.redis;

import io.github.nosequel.storage.redis.RedisStorageHandler;
import io.github.nosequel.storage.redis.provider.RedisStorageProvider;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CachedRedisStorageProvider<T> extends RedisStorageProvider<T> {

    private final long cacheInterval;
    private final TimeUnit timeUnit;

    private final Map<String, T> cache = new HashMap<>();

    private long lastCache;

    /**
     * Constructor to make a new {@link CachedRedisStorageProvider} object.
     *
     * @param key            the key to get/set the data to in redis.
     * @param storageHandler the redis handler to get the {@link redis.clients.jedis.JedisPool} from.
     * @param clazz          the class of the generic {@link T} type.
     * @param cacheInterval  the interval it should take to cache the objects within the {@link CachedRedisStorageProvider#cache} map.
     * @param timeUnit       the unit of the cache time.
     */
    public CachedRedisStorageProvider(String key, RedisStorageHandler storageHandler, Class<T> clazz, long cacheInterval, TimeUnit timeUnit) {
        super(key, storageHandler, clazz);

        this.cacheInterval = cacheInterval;
        this.timeUnit = timeUnit;
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
            if (this.shouldCache() || !this.cache.containsKey(key)) {
                final Jedis jedis = this.storageHandler.getResource();
                final T value = this.serializer.deserialize(jedis.hget(this.key, key));

                this.cache.put(key, value);
                this.lastCache = System.currentTimeMillis();

                return value;
            }

            return this.cache.get(key);
        });
    }

    private boolean shouldCache() {
        return this.lastCache + this.timeUnit.toMillis(this.cacheInterval) <= System.currentTimeMillis();
    }
}