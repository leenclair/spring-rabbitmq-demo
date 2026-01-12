package sample.demo.v0;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Sender {

    private final RabbitTemplate rabbitTemplate;

    public void send(String message) {
        rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_NAME, message);
        System.out.println(" [x] Sent '" + message + "'");
    }
}
