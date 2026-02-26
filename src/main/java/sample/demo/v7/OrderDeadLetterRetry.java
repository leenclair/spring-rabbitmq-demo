package sample.demo.v7;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderDeadLetterRetry {

    private final RabbitTemplate rabbitTemplate;

    public OrderDeadLetterRetry(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.DLQ)
    public void processDeadLetter(String message) {
        log.info("[DLQ] Received message: {}", message);

        try {
            // "fail" 메시지를 수정하여 성공적으로 처리되도록 변경
            if ("fail".equalsIgnoreCase(message)) {
                message = "success";
                log.info("[DLQ] Message modified to: {}", message);
            } else {
                // 이미 수정된 메시지는 다시 처리하지 않음
                log.info("[DLQ] Message already modified, skipping reprocessing: {}", message);
                return;
            }

            // 수정된 메시지를 원래 큐로 다시 전송
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_TOPIC_EXCHANGE, // 원래 큐에 연결된 Exchange
                    "order.completed", // 원래 큐의 라우팅 키
                    message // 수정된 메시지
            );
            log.info("[DLQ] Reprocessed message sent to original queue: {}", message);
        } catch (Exception e) {
            log.info("[DLQ] Failed to reprocess message: {}", message, e);
        }
    }
}
