package com.example.ociredis;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPooled;

public class RedisClient {

    private JedisClientConfig jedis;
    private JedisPooled client;

    public RedisClient(String host, int port, String user, String password) {
        jedis = DefaultJedisClientConfig.builder()
                .ssl(true)
                .user(user)
                .password(password)
                .build();
        client = new JedisPooled(new HostAndPort(host, port) ,jedis);
    }

    public void set(String key, String value) {
        client.set(key, value);
    }

    public String get(String key) {
        return client.get(key);
    }

    public void close() {
        client.close();
    }

}
