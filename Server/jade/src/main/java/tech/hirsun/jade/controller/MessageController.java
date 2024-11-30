package tech.hirsun.jade.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.hirsun.jade.pojo.Message;
import tech.hirsun.jade.result.Result;
import tech.hirsun.jade.service.MessageService;
import tech.hirsun.jade.utils.JwtUtils;

import java.util.Date;

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
     */
    @GetMapping("/get_messages_by_topic_id")
    private Result getMessagesByTopicId(@RequestParam Integer topicId,
                                           @RequestParam(required = false) Long startTime) {
        log.info("Request messages by chatroom id: {}, start time: {}", topicId, startTime);
        long endTime = new Date().getTime();

        if (startTime == null || startTime < endTime - 5 * 24 * 60 * 60 * 1000 || startTime > endTime) {
            // 5 days ago
            startTime = endTime - 5 * 24 * 60 * 60 * 1000;
        }

        return Result.success(messageService.getMessagesByTopicId(topicId, startTime, endTime));
    }

    /**
     * Post message
     * @param message: message object
     */
    @PostMapping("/post")
    private Result postMessage(@RequestHeader String jwt,
                               @RequestBody Message message) {
        log.info("Request post message: {}", message);
        message.setUserId(Integer.parseInt(JwtUtils.parseJwt(jwt).get("id").toString()));
        messageService.postMessage(message);
        return Result.success();
    }

}
