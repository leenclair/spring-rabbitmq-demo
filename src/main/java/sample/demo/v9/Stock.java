package sample.demo.v9;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "stocks")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private int stock;
    private boolean processed;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void created() {
        this.createdAt = LocalDateTime.now();
    }

    public void updated() {
        this.processed = true;
        this.updatedAt = LocalDateTime.now();
    }
}
