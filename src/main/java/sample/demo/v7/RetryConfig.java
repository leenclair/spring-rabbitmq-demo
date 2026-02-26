package sample.demo.v7;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.retry.*;

import java.time.Duration;

@Slf4j
@Configuration
public class RetryConfig {

    @Bean
    public RetryTemplate retryTemplate() {

        RetryPolicy policy = RetryPolicy.builder()
                .maxRetries(3) // 최대 3회 재시도
                .delay(Duration.ofSeconds(1)) // 1초마다 재시도
                .build();

        return new RetryTemplate(policy);
    }
}
