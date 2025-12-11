package com.example.KafkaCase.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@AllArgsConstructor
@Getter
public class MiniPackage {
    private Long id;
    private LocalDateTime completed_at;
    private LocalDateTime created_at;
    private LocalDateTime last_updated_at;
    private Integer eta;
    private String status;
    private LocalDateTime picked_up_at;
}
