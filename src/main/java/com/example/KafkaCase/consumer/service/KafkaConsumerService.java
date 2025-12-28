package com.example.KafkaCase.consumer.service;

import com.example.KafkaCase.consumer.mapper.PackageEntityMapper;
import com.example.KafkaCase.dto.MappedPackage;
import com.example.KafkaCase.dto.MiniPackage;
import com.example.KafkaCase.mapper.PackageMapper;
import com.example.KafkaCase.service.KafkaSenderService;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
    private final KafkaSenderService sender;
    private final PackageMapper mapper;
    private final PackageEntityMapper packageEntityMapper;
    private static final String topicName="mapped_package_cdc";

    public KafkaConsumerService(KafkaSenderService sender, PackageMapper mapper, PackageEntityMapper packageEntityMapper) {
        this.sender = sender;
        this.mapper = mapper;
        this.packageEntityMapper = packageEntityMapper;
    }

    @KafkaListener(topics = "kafka_case.public.package", groupId = "package_cg")
    public void consume(JsonNode msg) {
        JsonNode after = msg.get("after");
        //Handle delete events
        if(after.isNull()){
            sender.send(topicName,msg.get("before").get("id").toString(),null);
            logger.info("Tombstone is sent to {} with id {}.",topicName,msg.get("before").get("id").toString());
            return;
        }
        //Skip canceled packages (no need to map or send them to Kafka)
        if (after.get("cancelled").asBoolean()){
            logger.info("Package {} is cancelled.It is not sent to Kafka {}. Skipping.",after.get("id"),topicName);
            return;
        }
        // Map the CDC 'after' payload to a MiniPackage DTO
        MiniPackage entity = packageEntityMapper.mapToEntity(after);
        // Transform MiniPackage to MappedPackage
        MappedPackage mapped = mapper.map(entity);
        // Send transformed record to target Kafka topic using package id as key (ordering guarantee)
        String key = String.valueOf(mapped.getId());
        sender.send(topicName,key,mapped);
        logger.info("Package {} sent to Kafka {}.",key,topicName);
    }


}