package com.example.KafkaCase.consumer.service;

import com.example.KafkaCase.consumer.mapper.PackageEntityMapper;
import com.example.KafkaCase.dto.MappedPackage;
import com.example.KafkaCase.entity.PackageEntity;
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

    public KafkaConsumerService(KafkaSenderService sender, PackageMapper mapper, PackageEntityMapper packageEntityMapper) {
        this.sender = sender;
        this.mapper = mapper;
        this.packageEntityMapper = packageEntityMapper;
    }

    @KafkaListener(topics = "kafka_case.public.package", groupId = "package_cg")
    public void consume(JsonNode msg) {
        JsonNode after = msg.get("after");
        //if deleted - > tombstone
        if(after.isNull()){
            sender.send("mapped_package_cdc",msg.get("before").get("id").toString(),null);
            logger.info("Tombstone is sent to mapped_package_cdc with id {}.",msg.get("before").get("id").toString());
            return;
        }
        //first cancelled check then map (if it is cancelled no need to map)
        if (after.get("cancelled").asBoolean()){
            logger.info("Package {} is cancelled. Skipping.", after.get("id"));
            return;
        }

        PackageEntity entity = packageEntityMapper.mapToEntity(after);
        MappedPackage mapped = mapper.map(entity);

        String key = String.valueOf(mapped.getId());
        sender.send("mapped_package_cdc",key,mapped);
        logger.info("Package {} sent to Kafka mapped_package_cdc.",mapped.getId());
    }


}