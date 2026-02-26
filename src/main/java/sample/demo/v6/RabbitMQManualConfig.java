package sample.demo.v6;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit // RabbitMQ 관련 어노테이션 활성화, RabbitListener 설정 커스텀
//@Configuration
public class RabbitMQManualConfig {

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        // 수동 ACK 모드로 설정하여 메시지 처리가 완료된 후 명시적으로 ACK를 보내도록 설정
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }
}
