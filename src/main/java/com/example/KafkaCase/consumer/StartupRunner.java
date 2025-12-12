package com.example.KafkaCase.consumer;

import com.example.KafkaCase.consumer.service.DebeziumConnectorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;



@Component
public class StartupRunner implements CommandLineRunner {

    private final DebeziumConnectorService service;
    public StartupRunner(DebeziumConnectorService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) throws Exception {
        service.createConnector();
    }
}
