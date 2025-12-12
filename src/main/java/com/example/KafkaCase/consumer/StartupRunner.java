package com.example.KafkaCase.consumer;

import com.example.KafkaCase.consumer.service.DebeziumConnectorService;
import com.example.KafkaCase.entity.PackageEntity;
import com.example.KafkaCase.repository.PackageEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class StartupRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(StartupRunner.class);
    private final DebeziumConnectorService service;
    private final PackageEntityRepository repository;
    public StartupRunner(DebeziumConnectorService service, PackageEntityRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {

        PackageEntity p0 = new PackageEntity();
        p0.setId(1001L);
        p0.setArrival_for_delivery_at(LocalDateTime.parse("2021-11-13T11:05:58.045640"));
        p0.setArrival_for_pickup_at(LocalDateTime.parse("2021-11-13T10:48:37.032078"));
        p0.setCancel_reason(null);
        p0.setCancelled(false);
        p0.setCancelled_at(null);
        p0.setCollected(true);
        p0.setCollected_at(LocalDateTime.parse("2021-11-13T10:47:52.828692"));
        p0.setCompleted_at(LocalDateTime.parse("2021-11-13T11:40:15.314340"));
        p0.setCreated_at(LocalDateTime.parse("2021-11-13T10:47:52.675248"));
        p0.setCustomer_id(20002011575015L);
        p0.setDelivery_date(LocalDateTime.parse("2021-11-13T00:00:00"));
        p0.setEta(277);
        p0.setIn_delivery_at(LocalDateTime.parse("2021-11-13T11:05:56.861614"));
        p0.setLast_updated_at(LocalDateTime.parse("2021-11-13T11:40:15.314340"));
        p0.setOrder_id(123972783L);
        p0.setOrigin_address_id(999000020443388L);
        p0.setPicked_up_at(LocalDateTime.parse("2021-11-13T10:49:50.278087"));
        p0.setReassigned(null); //
        p0.setStatus("COMPLETED");
        p0.setStore_id(20000000004103L);
        p0.setType("REGULAR");
        p0.setUser_id(50002010395213L);
        p0.setWaiting_for_assignment_at(LocalDateTime.parse("2021-11-13T10:47:52.675248"));

        PackageEntity p1 = new PackageEntity();
        p1.setId(1002L);
        p1.setCreated_at(LocalDateTime.parse("2021-11-13T10:00:00"));
        p1.setCompleted_at(LocalDateTime.parse("2021-11-13T11:00:00"));
        p1.setEta(30);
        p1.setLast_updated_at(LocalDateTime.parse("2021-11-13T11:00:00"));
        p1.setStatus("COMPLETED");
        p1.setCancelled(false);

        PackageEntity p2 = new PackageEntity();
        p2.setId(1003L);
        p2.setCreated_at(LocalDateTime.parse("2021-11-13T09:00:00"));
        p2.setLast_updated_at(LocalDateTime.parse("2021-11-13T09:10:00"));
        p2.setCancelled(true);
        p2.setCancel_reason("Customer request");
        p2.setCancelled_at(LocalDateTime.parse("2021-11-13T09:05:00"));
        p2.setStatus("CANCELLED");

        PackageEntity p3 = new PackageEntity();
        p3.setId(1004L);
        p3.setCreated_at(LocalDateTime.parse("2021-11-13T10:00:00"));
        p3.setLast_updated_at(LocalDateTime.parse("2021-11-13T10:30:00"));
        p3.setStatus("IN_DELIVERY");
        p3.setCancelled(false);
        p3.setEta(60);
        p3.setCollected(true);
        p3.setCollected_at(LocalDateTime.parse("2021-11-13T10:05:00"));
        p3.setPicked_up_at(LocalDateTime.parse("2021-11-13T10:10:00"));
        p3.setIn_delivery_at(LocalDateTime.parse("2021-11-13T10:20:00"));

        repository.saveAll(List.of(p0,p1, p2, p3));
        log.info("Test data saved to db");
        service.createConnector();
    }
}
