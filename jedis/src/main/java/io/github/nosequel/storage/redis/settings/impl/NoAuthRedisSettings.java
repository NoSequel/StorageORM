package io.github.nosequel.storage.redis.settings.impl;

import io.github.nosequel.storage.redis.settings.RedisSettings;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class NoAuthRedisSettings extends RedisSettings {

    public NoAuthRedisSettings(String hostname, int port) {
        super(hostname, port);
    }

    /**
     * Authenticate for the database
     *
     * @param object the object to authenticate for
     * @return the authenticated object
     */
    @Override
    public Jedis auth(Jedis object) {
        return object;
    }
}