package sample.demo.v6;

import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private static final int MAX_RETRIES = 3; // 최대 재시도 횟수
    private static final String RETRY_HEADER = "x-retry-count"; // 재시도 횟수를 저장하는 헤더 키

    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.ORDER_COMPLETED_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void process(Message message, Channel channel, @Header("amqp_deliveryTag") long tag) {
        String body = new String(message.getBody());
        try {
            // 실패 유발
            if ("fail".equalsIgnoreCase(body)) {
                int retryCount = getRetryCount(message);
                if(retryCount < MAX_RETRIES) {
                    int next = retryCount + 1;
                    log.warn("메시지 처리 실패, 재시도 {}회: {}", retryCount, body);

                    // 재시도 횟수를 메시지 헤더에 저장하여 다음 재시도 시 참조할 수 있도록 설정
                    Map<String, Object> headers =
                            new HashMap<>(message.getMessageProperties().getHeaders());

                    Message retryMessage = MessageBuilder
                            .withBody(message.getBody())
                            .copyHeaders(headers)
                            .setHeader(RETRY_HEADER, next)
                            .build();

                    // 재시도 메시지를 원래의 라우팅 키로 다시 발행하여 재처리 시도
                    rabbitTemplate.send(
                            RabbitMQConfig.ORDER_EXCHANGE,
                            "order.completed.shipping",
                            retryMessage
                    );

                    // 같은 exchange/routingKey로 재발행하는 게 이상적이지만
                    // 재발행 시점에 메시지가 이미 ACK 처리되어 있을 수 있으므로, ACK를 먼저 보내고 재발행하는 방식으로 구현
                    channel.basicAck(tag, false);
                    return;
                } else {
                    log.error("최대 재시도 횟수 초과, 메시지 DLQ로 이동: {}", body);
                    channel.basicNack(tag, false, false); // 메시지를 거부하고 재큐잉하지 않음 (DLQ로 이동)
                    return;
                }
            }
            // 메시지 정상 처리 로직
            log.info("주문 완료 성공, 배송 지시 메시지 수신: {}", body);
            channel.basicAck(tag, false); // 메시지 처리 성공, ACK 전송

        } catch (Exception e) {
            log.error("메시지 처리 중 오류 발생: {}", e.getMessage());
            try {
                channel.basicNack(tag, false, false); // 메시지를 거부하고 재큐잉하지 않음 (DLQ로 이동)
            } catch (Exception ex) {
                log.error("메시지 Nack 전송 중 오류 발생: {}", ex.getMessage());
            }
        }
    }

    private int getRetryCount(Message message) {
        Object retryHeader = message.getMessageProperties().getHeaders().get(RETRY_HEADER);
        switch (retryHeader) {
            case null -> {
                log.info("재시도 헤더가 존재하지 않습니다. 초기 시도로 간주합니다.");
                return 0;
            }
            case Integer i -> {
                log.info("재시도 헤더에서 정수 값 추출: {}", i);
                return i;
            }
            case String s -> {
                try {
                    return Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    log.warn("재시도 헤더 값이 유효한 정수가 아닙니다: {}", retryHeader);
                    return 0;
                }
            }
            default -> {
                log.warn("재시도 헤더 값이 예상치 못한 타입입니다: {}", retryHeader);
                return 0;
            }
        }
    }

}
