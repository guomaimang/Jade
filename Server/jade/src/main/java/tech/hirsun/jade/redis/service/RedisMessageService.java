package tech.hirsun.jade.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import tech.hirsun.jade.redis.MessagePrefix;

import java.util.Set;

import static tech.hirsun.jade.utils.StringAndBeanConventer.beanToString;

@Service

public class RedisMessageService {

    @Autowired
    JedisPool jedisPool;

    // Store messages in a Sorted Set
    public <T> void saveMessage(String chatId, long timestamp, T message) {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = MessagePrefix.message.getPrefix() + chatId;
            String value = beanToString(message);
            jedis.zadd(key, timestamp, value);
            jedis.expire(key, MessagePrefix.message.getExpireSeconds());
        }
    }

    // Query messages based on time range
    public Set<String> getMessages(String chatId, long startTime, long endTime) {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = MessagePrefix.message.getPrefix() + chatId;
            return jedis.zrangeByScore(key, startTime, endTime);
        }
    }
    
}
