package sample.demo.v2;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "notification_queue";
    public static final String FANOUT_EXCHANGE_NAME = "notification_exchange";

    @Bean
    public Queue notificationQueue() {
        return new Queue(QUEUE_NAME, false);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        // FanoutExchange는 메시지를 모든 바인딩된 큐로 전달하는 역할을 합니다.
        return new FanoutExchange(FANOUT_EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue notificationQueue, FanoutExchange fanoutExchange) {
        // BindingBuilder.bind(연결 큐).to(연결 Exchange)를 사용하여 큐와 교환기(Exchange)를 바인딩합니다.
        return BindingBuilder.bind(notificationQueue).to(fanoutExchange);
    }

}
