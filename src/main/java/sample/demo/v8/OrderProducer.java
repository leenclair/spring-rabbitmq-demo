package sample.demo.v8;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
@RequiredArgsConstructor
public class OrderProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendOrder(String message) {
        log.info("주문 메시지 발행: {}", message);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_TOPIC_EXCHANGE,
                "order.complete",
                message
        );
        log.info("주문 메시지 발행 완료");
    }

}
