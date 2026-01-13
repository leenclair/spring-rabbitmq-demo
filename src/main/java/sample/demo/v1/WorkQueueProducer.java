package sample.demo.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkQueueProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(WorkMessage workMessage) {
        // 메시지 persistence를 위한 설정 추가 고려
        // MessageProperties props = new MessageProperties();
        // props.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        // Queue로 바로 보내는 가장 단순한 방식 (exchange 없이 default exchange 사용)
        rabbitTemplate.convertAndSend(RabbitConfig.WORK_QUEUE, workMessage);
        System.out.println("[P] Sent: " + workMessage);
    }

}
