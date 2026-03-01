package sample.demo.v9;

import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Component
@RequiredArgsConstructor
public class MessageProducer {

    private final StockRepository stockRepository;
    private final ConnectionFactory connectionFactory;
    private final MessageConverter messageConverter;

    @Transactional
    public void sendMessage(Stock stock, String testCase) {
        Channel channel = null;

        try {
            var connection = connectionFactory.createConnection();
            channel = connection.createChannel(true);
            channel.txSelect();

            // DB 저장
            stock.created();
            Stock savedStock = stockRepository.save(stock);
            log.info("Saved stock: {}", savedStock);

            // 같은 채널로 직접 publish
            var message = messageConverter.toMessage(
                    savedStock,
                    new MessageProperties()
            );
            channel.basicPublish(
                    "",
                    "transaction-queue",
                    null,
                    message.getBody()
            );

            if ("fail".equals(testCase)) {
                throw new RuntimeException("트랜잭션 실패 시나리오");
            }

            channel.txCommit();
            log.info("RabbitMQ 트랜잭션 커밋 완료");

        } catch (Exception e) {
            log.error("트랜잭션 실패: {}", e.getMessage());
            if (channel != null && channel.isOpen()) {
                try {
                    channel.txRollback();
                    log.info("RabbitMQ 트랜잭션 롤백 완료");
                } catch (Exception rollbackEx) {
                    log.error("롤백 실패: {}", rollbackEx.getMessage());
                }
            }
            throw new RuntimeException("트랜잭션 실패", e);
        } finally {
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (Exception e) {
                    log.error("채널 닫기 실패: {}", e.getMessage());
                }
            }
        }
    }
}
