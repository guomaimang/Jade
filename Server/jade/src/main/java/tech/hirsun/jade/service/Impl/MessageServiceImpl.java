package tech.hirsun.jade.service.Impl;

import org.springframework.stereotype.Service;
import tech.hirsun.jade.pojo.Message;
import tech.hirsun.jade.service.MessageService;

import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Override
    public List getMessagesByTopicId(Integer topicId,
                                     Long startTime,
                                     Long endTime) {
        return null;
    }

    @Override
    public void postMessage(Message message) {
        Integer topicId = message.getTopicId();
        long timestamp = new Date().getTime();
    }
}
