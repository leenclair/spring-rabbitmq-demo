package sample.demo.v8;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
public class OrderConsumer {

    private int retryCount = 0;

    @RabbitListener(queues = RabbitMQConfig.ORDER_COMPLETE_QUEUE)
    public void processOrder(String message) {
        log.info("주문 처리 중인 메시지: {}", message);

        if ("fail".equals(message)) {
            log.warn("주문 처리 실패: {} (재시도 횟수: {})", message, retryCount);
            retryCount++;
            throw new RuntimeException("# 실패 처리 시도: " + retryCount);
        }

        log.info("주문 처리 완료: {}", message);
        retryCount = 0; // 성공 시 재시도 카운트 초기화
    }

}