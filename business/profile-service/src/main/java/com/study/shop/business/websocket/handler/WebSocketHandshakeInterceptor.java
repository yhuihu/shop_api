package com.study.shop.business.websocket.handler;

import com.mysql.cj.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * @author Tiger
 * @date 2020-03-24
 * @see com.study.shop.business.websocket.handler
 **/
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler, @NotNull Map<String, Object
            > attributes) {
        UriComponents uriComponents = UriComponentsBuilder.fromUri(request.getURI()).build();
        String id = uriComponents.getQueryParams().getFirst("id");
        String token = uriComponents.getQueryParams().getFirst("token");
        return !(StringUtils.isNullOrEmpty(id)) && !(StringUtils.isNullOrEmpty(token));
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
