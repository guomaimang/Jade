package tech.hirsun.jade.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import tech.hirsun.jade.pojo.Message;
import tech.hirsun.jade.redis.MessagePrefix;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static tech.hirsun.jade.utils.StringAndBeanConventer.beanToString;
import static tech.hirsun.jade.utils.StringAndBeanConventer.stringToBean;

@Service

public class RedisMessageService {

    @Autowired
    JedisPool jedisPool;

    // Store messages in a Sorted Set
    public void saveMessage(String chatId, long timestamp, Message message) {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = MessagePrefix.message.getPrefix() + chatId;
            String value = beanToString(message);
            jedis.zadd(key, timestamp, value);
            jedis.expire(key, MessagePrefix.message.getExpireSeconds());
        }
    }

    // Query messages based on time range
    public List getMessages(String chatId, long startTime, long endTime) {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = MessagePrefix.message.getPrefix() + chatId;
            Set<String> messagesSet = jedis.zrangeByScore(key, startTime, endTime);
            List<Message> messages = new ArrayList<>();
            for (String message : messagesSet) {
                messages.add(stringToBean(message, Message.class));
            }
            return messages;
        }
    }

}
