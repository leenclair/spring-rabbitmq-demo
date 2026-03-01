package sample.demo.v9;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.amqp.autoconfigure.RabbitTemplateConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "transaction-queue";

    @Bean
    public Queue queue() {
        // durable: true로 설정하여 RabbitMQ 서버가 재시작되어도 큐가 유지되도록 설정
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter(); // 메시지를 JSON으로 변환하는 MessageConverter를 Bean으로 등록
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            RabbitTemplateConfigurer configurer,
            ConnectionFactory connectionFactory
    ) {

        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        // RabbitTemplateConfigurer를 사용하여 RabbitTemplate을 구성
        // spring-boot-starter-amqp가 제공하는 RabbitTemplateConfigurer는 RabbitTemplate의 설정을 자동으로 적용해줍니다.
        // messageConverter 빈도 자동으로 적용됩니다.
        configurer.configure(rabbitTemplate, connectionFactory);

        return rabbitTemplate;
    }

}
