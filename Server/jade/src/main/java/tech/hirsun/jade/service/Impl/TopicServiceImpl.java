package tech.hirsun.jade.service.Impl;

import org.springframework.stereotype.Service;
import tech.hirsun.jade.pojo.Topic;
import tech.hirsun.jade.service.TopicService;

import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {
    @Override
    public List<Topic> list() {
        return null;
    }

    @Override
    public Boolean checkExist(Integer topicId) {
        return null;
    }
}
