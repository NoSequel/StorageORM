package io.github.nosequel.storage.redis.settings;

import io.github.nosequel.storage.settings.Settings;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Getter
@RequiredArgsConstructor
public abstract class RedisSettings implements Settings<JedisPool, Jedis> {

    protected final String hostname;
    protected final int port;

    /**
     * Create the database handling object.
     *
     * @return the newly created object
     */
    @Override
    public JedisPool createObject() {
        return new JedisPool(
                this.hostname,
                this.port
        );
    }
}