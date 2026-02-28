package sample.demo.v8;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderProducer orderProducer;

    @GetMapping
    public ResponseEntity<String> sendOrderMessage(String message) {
        log.info("주문 생성 요청: {}", message);
        orderProducer.sendOrder(message);
        return ResponseEntity.ok("주문 메시지 발행 완료: message: " + message);
    }

}
