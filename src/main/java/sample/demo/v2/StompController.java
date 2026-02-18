package sample.demo.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class StompController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/send")
    public void sendMessage(NotificationMessage notificationMessage) {
        // 클라이언트로부터 "/app/send" 경로로 메시지가 전송되면 이 메서드가 호출됩니다.
        // 받은 메시지를 로그에 기록하고, WebSocket을 통해 클라이언트로 다시 전송합니다.
        String message = notificationMessage.message();
        log.info("Received message: {}", message);
        simpMessagingTemplate.convertAndSend("/topic/notifications", message);
    }
}
