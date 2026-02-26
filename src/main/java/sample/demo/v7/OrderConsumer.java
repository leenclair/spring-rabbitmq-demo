package sample.demo.v7;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.retry.RetryException;
import org.springframework.core.retry.RetryTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final RabbitTemplate rabbitTemplate;
    private final RetryTemplate retryTemplate;

    @RabbitListener(queues = RabbitMQConfig.ORDER_COMPLETED_QUEUE)
    public void consume(String message) {
        try {
            retryTemplate.execute(() -> {
                log.info("Processing message: {}", message);
                if ("fail".equalsIgnoreCase(message)) {
                    throw new RuntimeException(message);
                }
                log.info("메시지 처리 성공! message: {}", message);
                return null;
            });
        } catch (RetryException e) {
            // 재시도 소진 시, DLQ로 메시지 전송
            Throwable last = e.getCause();              // 마지막 실패 원인
            Throwable[] prev = e.getSuppressed();       // 이전 시도들(있을 수 있음)

            if (prev.length >= 2) {
                log.warn("재시도 소진, DLQ로 전송. message={}, suppressedCount={}, lastCause={}",
                        message, prev.length, last.getMessage());
                rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_TOPIC_DLX,
                        RabbitMQConfig.DEAD_LETTER_ROUTING_KEY, message);
            } else {
                throw new RuntimeException(message); // 재시도 소진이 아닌 경우, 예외를 다시 던짐
            }
        }
    }

}
