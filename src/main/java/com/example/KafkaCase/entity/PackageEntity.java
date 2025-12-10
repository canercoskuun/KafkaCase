package com.example.KafkaCase.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name = "package")
public class PackageEntity {
    @Id
    private Long id;

    private Long order_id;

    private Long user_id;

    private Long customer_id;

    private Long store_id;

    private Long origin_address_id;

    private String type;

    private Integer eta;

    private LocalDateTime delivery_date;

    private LocalDateTime created_at;

    private LocalDateTime waiting_for_assignment_at;

    private LocalDateTime collected_at;

    private LocalDateTime arrival_for_pickup_at;

    private LocalDateTime picked_up_at;

    private LocalDateTime in_delivery_at;

    private LocalDateTime arrival_for_delivery_at;

    private LocalDateTime completed_at;

    private LocalDateTime last_updated_at;

    private LocalDateTime cancelled_at;

    private Boolean collected;

    private Boolean cancelled;

    private String cancel_reason;

    private String status;

    private Boolean reassigned;

}