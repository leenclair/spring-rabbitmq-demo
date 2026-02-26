package sample.demo.v6;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
@RequiredArgsConstructor
public class OrderDLQConsumer {

    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.DLQ)
    public void process(String message) {
        log.info("DLQ에서 메시지 수신: {}", message);

        // DLQ에서 메시지를 처리하는 로직을 구현합니다.
        // 예를 들어, 메시지를 재처리하거나, 알림을 보내거나,
        // 또는 메시지를 별도의 저장소에 기록할 수 있습니다.
        try {
            // 메시지 재처리 로직
            String fixMessage = "successfully fixed: " + message;

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_EXCHANGE,
                    "order.completed.shipping",
                    fixMessage
            );

            log.info("DLQ 메시지 재처리 완료, 재발행된 메시지: {}", fixMessage);
        } catch (Exception e) {
            log.error("DLQ 메시지 처리 중 오류 발생: {}", e.getMessage());
        }

    }

}
