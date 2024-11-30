package tech.hirsun.jade.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.hirsun.jade.result.Result;
import tech.hirsun.jade.service.TopicService;

@Slf4j
@RestController
@RequestMapping("/topic")
public class TopicController {

    @Autowired
    TopicService topicService;

    // Get Topic list
    @GetMapping("/list")
    private Result getTopicList() {
        log.info("Request topic list");
        return Result.success(topicService.list());
    }
}
