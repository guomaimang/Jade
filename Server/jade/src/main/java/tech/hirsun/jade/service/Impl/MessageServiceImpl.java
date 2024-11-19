package tech.hirsun.jade.service.Impl;

import tech.hirsun.jade.pojo.Message;
import tech.hirsun.jade.service.MessageService;

import java.util.List;

public class MessageServiceImpl implements MessageService {
    @Override
    public List getMessagesByTopicId(Integer topicId, Long startTime, Long endTime) {
        return List.of();
    }

    @Override
    public Integer postMessage(Message message) {
        return 0;
    }
}
