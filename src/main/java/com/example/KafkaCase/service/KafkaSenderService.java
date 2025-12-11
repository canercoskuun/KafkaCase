package com.example.KafkaCase.service;
import com.example.KafkaCase.dto.MappedPackage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class KafkaSenderService {

    private final KafkaTemplate<String, MappedPackage> kafkaTemplate;

    public KafkaSenderService(KafkaTemplate<String, MappedPackage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    //use key to guarantee message ordering
    public void send(String topicName,String key,MappedPackage pkg) {
        kafkaTemplate.send(topicName,key,pkg);
    }
}
