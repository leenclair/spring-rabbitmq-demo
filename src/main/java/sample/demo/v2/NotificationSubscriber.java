package sample.demo.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
@RequiredArgsConstructor
public class NotificationSubscriber {

    public static final String CLIENT_URL = "/topic/notifications";
    private final SimpMessagingTemplate simpMessagingTemplate;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void subscribeNotification(String message) {
        // RabbitMQ Queue에서 메시지를 수신하면 로그에 기록하고 WebSocket을 통해 클라이언트로 전송합니다.
        log.info("Received notification: {}", message);
        // WebSocket을 통해 클라이언트로 메시지를 전송합니다.
        simpMessagingTemplate.convertAndSend(CLIENT_URL, message);
    }
}
