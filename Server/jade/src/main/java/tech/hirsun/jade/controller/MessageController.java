package tech.hirsun.jade.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.hirsun.jade.pojo.Message;
import tech.hirsun.jade.result.Result;
import tech.hirsun.jade.service.MessageService;

@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * Get messages by topic id
     * @param topicId: topic id
     * @param startTime: the start time of the messages range
     * @param endTime: the end time of the messages range
     */
    @GetMapping("/get_by_topic_id")
    private Result getMessagesByTopicId(@RequestParam Integer topicId,
                                           @RequestParam Long startTime,
                                           @RequestParam Long endTime) {
        log.info("Request messages by chatroom id: {}, start time: {}, end time: {}", topicId, startTime, endTime);
        return Result.success(messageService.getMessagesByTopicId(topicId, startTime, endTime));
    }

    /**
     * Post message
     * @param message: message object
     */
    @PostMapping("/post")
    private Result postMessage(@RequestBody Message message) {
        log.info("Request post message: {}", message);
        return Result.success(messageService.postMessage(message));
    }


}
