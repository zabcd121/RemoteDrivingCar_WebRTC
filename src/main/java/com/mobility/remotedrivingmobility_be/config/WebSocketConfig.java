package com.mobility.remotedrivingmobility_be.config;

import com.mobility.remotedrivingmobility_be.service.client.ClientService;
import com.mobility.remotedrivingmobility_be.service.remotedrivingroom.RemoteDrivingRoomService;
import com.mobility.remotedrivingmobility_be.socket.SocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final SocketHandler socketHandler;


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        System.out.println("WebSocketConfig.registerWebSocketHandlers");

        registry.addHandler(socketHandler, "/ws")
                .setAllowedOriginPatterns("*");

        registry.addHandler(socketHandler, "/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS(); //Client의 web socket 지원 여부에 따라 Long Polling or Polling으로 통신한다.
        System.out.println("WebSocketConfig.registerWebSocketHandlers");
    }

    /*@Bean
    public WebSocketHandler signalHandler() {
        return new SocketHandler(roomService, clientService);
    }*/

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }
}

/*
@Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        // stomp 최대 버퍼 사이즈를 늘리기 위한 설정
        registry.setMessageSizeLimit(50000 * 1024);
        registry.setSendBufferSizeLimit(10240 * 1024);
        registry.setSendTimeLimit(20000);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS(); //Client의 web socket 지원 여부에 따라 Long Polling or Polling으로 통신한다.
    }

    //Application 내부에서 사용할 path를 지정
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Client에서 SEND 요청을 처리한다.
        // 즉 도착 경로에 대한 prefix 설정
        // 예를 들어 /app으로 설정해두면 /topic/hello라는 토픽에 대해 구독을 신청했을 때, 실제 경로는 /app/topic/hello가 되는 것

        // 일반적으로 /topic은 한 명이 메시지를 발행했을 때, 해당 토픽을 구독하고 있는 n명에게 메시지를 뿌릴 때 사용 (1:N)
        // 반면에 /queue는 한 명이 메시지를 발행했을 때, 발행한 한 명에게 다시 정보를 보내는 경우 (1:1)
        registry.setApplicationDestinationPrefixes("/pub"); //app

        // 해당 경로로 SimpleBroker를 등록한다.
        // SimpleBroker는 해당하는 경로를 SUBSCRIBE 하는 client에게 메시지를 전달하는 간단한 작업 수행
        // 메세지 브로커는 아래 주소로 시작하는 주소의 Subscriber 에게 메시지를 전달하는 역할을 한다.
        registry.enableSimpleBroker("/sub"); //topic
    }
*/