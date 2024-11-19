package tech.hirsun.jade.service;

import tech.hirsun.jade.pojo.Message;

import java.util.List;

public interface MessageService {
    List getMessagesByTopicId(Integer topicId, Long startTime, Long endTime);

    void postMessage(Message message);
}
