package sample.demo.v0;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void receive(String message) {
        System.out.println(" [x] Received '" + message + "'");
    }
}
