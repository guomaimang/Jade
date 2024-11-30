package tech.hirsun.jade.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hirsun.jade.dao.TopicDao;
import tech.hirsun.jade.pojo.Topic;
import tech.hirsun.jade.service.TopicService;

import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicDao TopicDao;

    @Override
    public List<Topic> list() {
        return TopicDao.list();
    }

}
