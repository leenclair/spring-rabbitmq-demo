package sample.demo.v8;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_COMPLETE_QUEUE = "order.complete.queue";

    public static final String ORDER_TOPIC_EXCHANGE = "order.exchange";

    public static final String ORDER_TOPIC_DLX = "order.dlx";
    public static final String ORDER_DLQ = "order.dlq";
    public static final String ORDER_DLQ_ROUTING_KEY = "order.dlq";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(ORDER_TOPIC_EXCHANGE);
    }

    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(ORDER_TOPIC_DLX);
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(ORDER_COMPLETE_QUEUE)
                .withArgument("x-dead-letter-exchange", ORDER_TOPIC_DLX)
                .withArgument("x-dead-letter-routing-key", ORDER_DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue dlq() {
        return new Queue(ORDER_DLQ);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue())
                .to(exchange()).with("order.complete.#");
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(dlq())
                .to(deadLetterExchange()).with(ORDER_DLQ_ROUTING_KEY);
    }

}
