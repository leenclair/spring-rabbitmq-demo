package sample.demo.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rabbit")
public class WorkQueueController {

    private final WorkQueueProducer producer;

    /**
     * POST /api/rabbit/work
     * body: {"message":"job-1","durationMs":3000}
     */
    @PostMapping("/work")
    public ResponseEntity<?> enqueue(@RequestBody WorkMessage req) {
        if (req.message() == null || req.message().isBlank()) {
            return ResponseEntity.badRequest().body("message is required");
        }
        if (req.durationMs() < 0) {
            return ResponseEntity.badRequest().body("durationMs must be >= 0");
        }

        producer.send(req);
        return ResponseEntity.ok(req);
    }

}
