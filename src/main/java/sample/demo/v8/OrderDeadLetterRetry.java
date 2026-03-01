package sample.demo.v8;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
@RequiredArgsConstructor
public class OrderDeadLetterRetry {

    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.ORDER_DLQ)
    public void retryOrder(String failedMessage) {
        try {
            log.info("retry 시도 중인 메시지: {}", failedMessage);

            // 실패한 메시지를 성공 메시지로 변환
            String message = "success";

            // 수정한 메시지를 원래 큐로 재발행
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_TOPIC_EXCHANGE,
                    "order.complete.retry",
                    message
            );

            log.info("메시지 재발행 완료: {}", message);

        } catch (Exception e) {
            log.error("메시지 재발행 실패: {}", failedMessage, e);
        }
    }

}
