//package sample.demo.v6;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/orders")
//public class OrderController {
//
//    private final OrderProducer orderProducer;
//
//    /**
//     * 단순 메시지 전송
//     * curl -i "http://localhost:8080/api/orders?message=success"
//     * 실패 유발
//     * curl -i "http://localhost:8080/api/orders?message=fail"
//     * */
//    @GetMapping
//    public ResponseEntity<String> completeOrder(@RequestParam String message) {
//        orderProducer.sendShipping(message);
//        return ResponseEntity.ok("주문 완료, 배송 지시 메시지 발행: " + message);
//    }
//
//}
