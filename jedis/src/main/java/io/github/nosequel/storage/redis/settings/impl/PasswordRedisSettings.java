package io.github.nosequel.storage.redis.settings.impl;

import io.github.nosequel.storage.redis.settings.RedisSettings;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class PasswordRedisSettings extends RedisSettings {

    protected final String password;

    public PasswordRedisSettings(String hostname, int port, String password) {
        super(hostname, port);
        this.password = password;
    }

    /**
     * Authenticate for the database
     *
     * @param object the object to authenticate for
     * @return the authenticated object
     */
    @Override
    public Jedis auth(Jedis object) {
        object.auth(password);
        return object;
    }
}
