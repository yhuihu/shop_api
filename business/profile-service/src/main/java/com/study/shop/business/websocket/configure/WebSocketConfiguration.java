package com.study.shop.business.websocket.configure;

import com.study.shop.business.websocket.MyWebSocket;
import com.study.shop.business.websocket.handler.WebSocketHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.annotation.Resource;

/**
 * @author Tiger
 * @date 2020-03-22
 * @see com.study.shop.business.websocket.configure
 **/
@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {
    @Resource
    MyWebSocket myWebSocket;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry
                .addHandler(myWebSocket, "/chat").setAllowedOrigins("*")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .addInterceptors(new WebSocketHandshakeInterceptor());
    }
}
