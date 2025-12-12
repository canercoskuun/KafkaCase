package com.example.KafkaCase.consumer.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import java.io.IOException;
import java.util.Objects;

@Service
public class DebeziumConnectorService {
    private static final Logger logger = LoggerFactory.getLogger(DebeziumConnectorService.class);
    private final RestTemplate restTemplate = new RestTemplate();
    //Creates the Debezium PostgreSQL connector by posting its configuration
    public void createConnector() throws IOException {
        String url = "http://localhost:8083/connectors";
        String json = new String(
                Objects.requireNonNull(this.getClass().getClassLoader()
                        .getResourceAsStream("config/debezium-connector.json")).readAllBytes()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> req = new HttpEntity<>(json, headers);

        try{
            ResponseEntity<String> response =restTemplate.postForEntity(url, req, String.class);
            logger.info("Connector status: {}", response.getStatusCode());
        } catch (Exception e) {
            logger.warn("Failed to create connector : {}", e.getMessage());
        }

    }
}