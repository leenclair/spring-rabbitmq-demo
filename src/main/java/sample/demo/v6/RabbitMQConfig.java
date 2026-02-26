package sample.demo.v6;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class RabbitMQConfig {

    public static final String ORDER_COMPLETED_QUEUE = "order_completed_queue";
    public static final String ORDER_EXCHANGE = "order_completed_exchange";
    public static final String DLQ = "dead_letter_queue";
    public static final String DLX = "dead_letter_exchange";

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(DLX);
    }

    // 메시지가 처리되지 않고 만료되면 DLX로 라우팅되어 DLQ에 저장됩니다.
    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(ORDER_COMPLETED_QUEUE)
                .deadLetterExchange(DLX) // DLX 설정
                .deadLetterRoutingKey(DLQ) // DLX로 라우팅할 때 사용할 라우팅 키 설정
//                .withArgument("x-dead-letter-exchange", DLX) // DLX 설정
//                .withArgument("x-dead-letter-routing-key", DLQ) // DLX로 라우팅할 때 사용할 라우팅 키 설정
                .ttl(5000)
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DLQ).build();
    }

    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderQueue()) // orderQueue()를 orderExchange()에 바인딩
                .to(orderExchange())
                .with("order.completed.#"); // 라우팅 키 패턴 설정(order.completed로 시작하는 모든 메시지)
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()) // deadLetterQueue()를 deadLetterExchange()에 바인딩
                .to(deadLetterExchange())
                .with(DLQ); // 라우팅 키 설정(DLQ)
    }

}
