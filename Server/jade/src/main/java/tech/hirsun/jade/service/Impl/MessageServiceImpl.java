package tech.hirsun.jade.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hirsun.jade.dao.UserDao;
import tech.hirsun.jade.pojo.Message;
import tech.hirsun.jade.redis.service.RedisMessageService;
import tech.hirsun.jade.service.MessageService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private RedisMessageService redisMessageService;

    @Autowired
    private UserDao userDao;

    @Override
    public List getMessagesByTopicId(Integer topicId,
                                     Long startTime,
                                     Long endTime) {
        return redisMessageService.getMessages(topicId.toString(), startTime, endTime);
    }

    @Override
    public void postMessage(Message message) {
        Integer topicId = message.getTopicId();
        long timestamp = new Date().getTime();
        message.setId(UUID.randomUUID().toString());
        message.setNickname(userDao.getUserById(message.getUserId()).getNickname());
        message.setCreateTime(new Date());
        redisMessageService.saveMessage(topicId.toString(), timestamp, message);
    }
}
