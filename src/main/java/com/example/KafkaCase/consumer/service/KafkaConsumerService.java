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
        //deleted check
        if(after.isNull()){
            sender.send(topicName,msg.get("before").get("id").toString(),null);
            logger.info("Tombstone is sent to {} with id {}.",topicName,msg.get("before").get("id").toString());
            return;
        }
        //first cancelled check then map (if it is cancelled no need to map)
        if (after.get("cancelled").asBoolean()){
            logger.info("Package {} is cancelled.It is not sent to Kafka {}. Skipping.",after.get("id"),topicName);
            return;
        }
        //after to minipackage
        MiniPackage entity = packageEntityMapper.mapToEntity(after);
        //minipackage to mappedpackage
        MappedPackage mapped = mapper.map(entity);
        //send kafka
        String key = String.valueOf(mapped.getId());
        sender.send(topicName,key,mapped);
        logger.info("Package {} sent to Kafka {}.",mapped.getId(),topicName);
    }


}