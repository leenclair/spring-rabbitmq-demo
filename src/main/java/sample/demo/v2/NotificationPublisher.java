package sample.demo.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(String message) {
        // RabbitTemplate의 convertAndSend 메서드를 사용하여 메시지를 FanoutExchange로 전송합니다.
        rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE_NAME, "", message);
        log.info("Published notification: {}", message);
    }
}
