package sample.demo.v9;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageConsumer {

    private final StockRepository stockRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(Stock stock) {
        log.info("메시지 수신: {}", stock);

        try {
            stock.updated();
            stockRepository.save(stock);
            log.info("stock 저장 완료 stock: {}", stock);
        } catch (Exception e) {
            log.error("stock 저장 실패: {}", e.getMessage());
            // 예외를 다시 던져서 메시지 처리가 실패했음을 RabbitMQ에 알립니다.
            // RabbitMQ는 이 메시지를 재시도하거나 다른 큐로 이동할 수 있습니다.
            throw e;
        }
    }

}
