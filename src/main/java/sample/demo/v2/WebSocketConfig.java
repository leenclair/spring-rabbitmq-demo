//package sample.demo.v2;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//
//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        // 클라이언트가 WebSocket 연결을 시도할 때 사용할 엔드포인트를 등록합니다.
//        registry.addEndpoint("/ws") // 클라이언트가 "/ws" 경로로 WebSocket 연결을 시도할 수 있도록 설정합니다.
//                .setAllowedOriginPatterns("*") // 모든 출처에서의 연결을 허용합니다.
//                .withSockJS(); // SockJS는 WebSocket을 지원하지 않는 브라우저에서도 WebSocket과 유사한 기능을 사용할 수 있도록 하는 라이브러리입니다. 이를 통해 호환성을 높입니다.
//    }
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        // 클라이언트가 구독할 수 있는 메시지 브로커(구독 경로)를 설정합니다. 여기서는 "/topic"으로 시작하는 메시지를 처리합니다.
//        registry.enableSimpleBroker("/topic");
//        // 클라이언트가 메시지를 보낼 때 사용할 접두사(서버발행 경로)를 설정합니다. 여기서는 "/app"으로 시작하는 메시지를 처리합니다.
//        registry.setApplicationDestinationPrefixes("/app");
//    }
//
//}
