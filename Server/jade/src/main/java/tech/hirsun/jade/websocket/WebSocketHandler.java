package tech.hirsun.jade.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.CloseStatus;
import tech.hirsun.jade.redis.service.RedisBasicService;
import tech.hirsun.jade.redis.WsTokenPrefix;
import tech.hirsun.jade.utils.StringAndBeanConventer;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private RedisBasicService redisBasicService;

    // 存储用户ID和WebSocketSession的映射
    private static final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 提取用户ID或token
        String query = session.getUri().getQuery();
        if (query == null || !query.startsWith("token=")) {
            session.close(CloseStatus.BAD_DATA);
            return;
        }

        String token = query.split("=")[1];

        // 验证token
        String userId = redisBasicService.get(WsTokenPrefix.byUUID, token, String.class);
        if (userId == null) {
            session.close(CloseStatus.NOT_ACCEPTABLE);
        }else{
            sessions.put(userId, session);
            redisBasicService.delete(WsTokenPrefix.byUUID, token);
        }

        log.info(sessions.toString());

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 处理收到的消息
        String payload = message.getPayload();
        // 给客户端确认反馈
         session.sendMessage(new TextMessage("Message received: " + payload));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 移除关闭的连接
        sessions.values().remove(session);
        log.info(sessions.toString());

    }

    // 服务器发送消息给指定用户
    public void sendMessageToUser(String userId, Object data) throws Exception {
        WebSocketSession session = sessions.get(userId);
        if (session != null && session.isOpen()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("uuid", UUID.randomUUID().toString());
            map.put("data", data);
            session.sendMessage(new TextMessage(StringAndBeanConventer.beanToString(map)));
        }
    }
}
