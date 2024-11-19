package tech.hirsun.jade.service;

import org.springframework.stereotype.Service;
import tech.hirsun.jade.pojo.Message;

import java.util.List;

@Service
public interface MessageService {
    List getMessagesByTopicId(Integer topicId, Long startTime, Long endTime);

    Integer postMessage(Message message);
}
