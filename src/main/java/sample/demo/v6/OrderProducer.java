package sample.demo.v6;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
@RequiredArgsConstructor
public class OrderProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendShipping(String message) {
        // 메시지를 order_completed_exchange로 발행, 라우팅 키는 order.completed.shipping
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                "order.completed.shipping",
                message
        );

        log.info("주문 완료, 배송 지시 메시지 message: {}", message);
    }
}
