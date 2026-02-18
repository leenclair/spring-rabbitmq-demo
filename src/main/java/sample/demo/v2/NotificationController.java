package sample.demo.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationPublisher publisher;

    // 이 메서드는 "/notifications/send" 경로로 POST 요청이 들어오면 호출됩니다.
    // 요청이 들어오면 NotificationPublisher를 사용하여 메시지를 발행합니다.
    @PostMapping
    public String sendNotification(@RequestBody String message) {
        log.info("Received request to send notification: {}", message);

        publisher.publish(message);
        return "[#] Notification sent: " + message + "\n";
    }
}
