package sample.demo.v9;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/message")
public class TransactionController {

    private final MessageProducer producer;

    @PostMapping
    public ResponseEntity<String> sendMessage(
            @RequestBody Stock stock,
            @RequestParam(required = false, defaultValue = "success") String testCase
    ) {
        log.info("메시지 전송 요청: stock={}, testCase={}", stock, testCase);

        try {
            producer.sendMessage(stock, testCase);
            return ResponseEntity.ok("메시지 전송 성공");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("MQ transaction 메시지 전송 실패: " + e.getMessage());

        }
    }
}
