package com.study.shop.business.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.shop.provider.dto.CommunicationUserListDTO;
import com.study.shop.provider.vo.MessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tiger
 * @date 2020-03-22
 * @see com.study.shop.business.websocket
 **/
@Component
@Slf4j
public class MyWebSocket extends TextWebSocketHandler {
    @Autowired
    private RedisTemplate redisTemplate;

    private static ConcurrentHashMap<String, WebSocketSession> userSession = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        String id = getUserId(session);
        userSession.put(id, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String id = getUserId(session);
        if (userSession.get(id) != null) {
            userSession.remove(id);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        CommunicationUserListDTO messageEntity = mapper.readValue(message.getPayload(), CommunicationUserListDTO.class);
        redisTemplate.convertAndSend("CHAT_CHANNEL", messageEntity);
    }

    private String getUserId(WebSocketSession session) {
        UriComponents uriComponents = UriComponentsBuilder.fromUri(session.getUri()).build();
        return Objects.requireNonNull(uriComponents.getQueryParams().getFirst("id"));
    }

    static void sendToUser(MessageVO messageVO) throws IOException {
        WebSocketSession toUser = userSession.get(messageVO.getToId());
        messageVO.setCreateTime(new Date());
        if (toUser != null) {
            messageVO.setIsMine(0);
            ObjectMapper mapper = new ObjectMapper();
            TextMessage textMessage = new TextMessage(mapper.writeValueAsString(messageVO));
            toUser.sendMessage(textMessage);
        }
        WebSocketSession fromUser = userSession.get(messageVO.getFromId());
        if (fromUser != null) {
            messageVO.setIsMine(1);
            ObjectMapper mapper = new ObjectMapper();
            TextMessage textMessage = new TextMessage(mapper.writeValueAsString(messageVO));
            fromUser.sendMessage(textMessage);
        }
    }
}
