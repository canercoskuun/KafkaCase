package com.example.KafkaCase.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MappedPackage {
    private Long id;
    private String createdAt;
    private String lastUpdatedAt;
    private Integer collectionDuration;
    private Integer deliveryDuration;
    private Integer eta;
    private Integer leadTime;
    private Boolean orderInTime;
}