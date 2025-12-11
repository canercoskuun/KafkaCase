package com.example.KafkaCase.consumer.mapper;

import com.example.KafkaCase.dto.MiniPackage;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class PackageEntityMapper  {

    public MiniPackage mapToEntity(JsonNode after) {
        MiniPackage m=new MiniPackage();
        //creating feature
        m.setId(after.get("id").asLong());
        m.setCreated_at(fromMicros(after.get("created_at").asLong()));
        m.setEta(after.get("eta").asInt());
        m.setStatus(after.get("status").asText());
        //if order is not COMPLETED these field could be null . maybe last_updated_at added
        m.setLast_updated_at(fromMicros(after.get("last_updated_at").asLong()));
        m.setCompleted_at(!after.get("completed_at").isNull() ? fromMicros(after.get("completed_at").asLong()) : null);
        m.setPicked_up_at(!after.get("picked_up_at").isNull() ? fromMicros(after.get("picked_up_at").asLong()) : null);
        return m;
    }
    private LocalDateTime fromMicros(Long micro) {
        if (micro == null) return null;
        return Instant.ofEpochMilli(micro / 1000)
                .atZone(ZoneOffset.UTC)
                .toLocalDateTime();
    }
}


