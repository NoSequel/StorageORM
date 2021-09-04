package io.github.nosequel.storage.redis;

import io.github.nosequel.storage.StorageHandler;
import io.github.nosequel.storage.redis.settings.RedisSettings;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;

@Getter
public class RedisStorageHandler extends StorageHandler {

    private final JedisPool pool;
    private final RedisSettings authSettings;

    public RedisStorageHandler(RedisSettings settings) {
        this.authSettings = settings;
        this.pool = settings.createObject();
    }

    public Jedis getResource() {
        return this.authSettings.auth(this.pool.getResource());
    }

}
