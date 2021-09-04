package io.github.nosequel.storage.redis.settings.impl;

import redis.clients.jedis.Jedis;

public class UserPasswordRedisSettings extends PasswordRedisSettings {

    private final String username;

    public UserPasswordRedisSettings(String hostname, int port, String username, String password) {
        super(hostname, port, password);
        this.username = username;
    }

    /**
     * Authenticate for the database
     *
     * @param object the object to authenticate for
     * @return the authenticated object
     */
    @Override
    public Jedis auth(Jedis object) {
        object.auth(this.username, this.password);
        return object;
    }
}