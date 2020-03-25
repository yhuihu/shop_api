package com.study.shop.business.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.shop.provider.vo.MessageVO;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

/**
 * @author Tiger
 * @date 2020-03-22
 * @see com.study.shop.business.websocket
 **/
public class RedisConsumer implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] bytes) {
        byte[] bytes1 = message.getBody();
        String encoded = new String(bytes1).split(",", 2)[1];
        ObjectMapper om = new ObjectMapper();
        try {
            MessageVO messageEntity = om.readValue(encoded, MessageVO.class);
            MyWebSocket.sendToUser(messageEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
