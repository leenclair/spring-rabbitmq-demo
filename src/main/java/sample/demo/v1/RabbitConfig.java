package sample.demo.v1;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class RabbitConfig {

    public static final String WORK_QUEUE = "v1.queue";

    // durable=true: 서버 재시작 시에도 큐 유지
    // exclusive=false, autoDelete=false (기본값)
    @Bean
    public Queue workQueue() {
        return new Queue(WORK_QUEUE, true);
    }

    /**
     * Work Queue 학습용 튜닝
     * - prefetch=1 : 한 consumer가 한 번에 1개씩만 가져오게 해서 "fair dispatch" 느낌
     * - concurrentConsumers : 동시에 몇 명이 소비할지
     */
    @Bean(name = "workQueueListenerFactory")
    public SimpleRabbitListenerContainerFactory workQueueListenerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);

        factory.setPrefetchCount(1);
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(3);

        // 기본 AUTO ack: 리스너 메서드가 정상 종료되면 ack, 예외면 reject(기본 requeue 가능)
        return factory;
    }
}
