package sample.demo.v1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WorkQueueConsumer {

    @RabbitListener(
            queues = RabbitConfig.WORK_QUEUE,
            containerFactory = "workQueueListenerFactory"
    )
    public void workQueueTask(WorkMessage msg) {
        log.info("[Consumer] Received: {} (duration: {} ms)",
                msg.message(), msg.durationMs());

        String originMessage = msg.message();
        int durationMs = msg.durationMs();



        // durationMs 만큼 "일하는 척" (테스트 용)
        try {
            int seconds = Math.max(0, durationMs / 1000);

            for (int i = 0; i < seconds; i++) {
                Thread.sleep(1000);
                System.out.print(".");
            }

            // 1000ms 미만 잔여 처리 (있으면)
            int remainder = durationMs % 1000;
            if (remainder > 0) {
                Thread.sleep(remainder);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // 인터럽트는 보통 "작업 중단" 신호라서 로그만 찍고 끝내거나,
            // 필요하면 RuntimeException 던져서 메시지 재처리로 보낼 수도 있음.
            // new AmqpRejectAndDontRequeueException로 명시적 reject 가능
            log.error("[!] Interrupted while processing: {}", originMessage);
            return;
        } catch (Exception e) {
            // 여기서 예외 던지면 메시지가 reject/requeue 될 수 있어.
            log.error("Failed: {} reason={}", originMessage, e.getMessage());
            // 무한 재시도 방지를 위한 로직 필요 (dead letter queue 고려)
            throw e;
        }

        log.info("[Consumer] Completed: {}", msg.message());
    }
}

