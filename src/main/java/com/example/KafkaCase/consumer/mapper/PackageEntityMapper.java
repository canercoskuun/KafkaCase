package com.example.KafkaCase.consumer.mapper;

import com.example.KafkaCase.entity.PackageEntity;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class PackageEntityMapper  {

    public PackageEntity mapToEntity(JsonNode after) {
        PackageEntity p = new PackageEntity();
        //creating feature
        p.setId(after.get("id").asLong());
        p.setCreated_at(fromMicros(after.get("created_at").asLong()));
        p.setEta(after.get("eta").asInt());
        p.setStatus(after.get("status").asText());
        p.setCancelled(after.get("cancelled").asBoolean());
        //if order is not COMPLETED these field could be null . maybe last_updated_at added
        p.setLast_updated_at(fromMicros(after.get("last_updated_at").asLong()));
        p.setCompleted_at(!after.get("completed_at").isNull() ? fromMicros(after.get("completed_at").asLong()) : null);
        p.setPicked_up_at(!after.get("picked_up_at").isNull() ? fromMicros(after.get("picked_up_at").asLong()) : null);
        return p;
    }
    private LocalDateTime fromMicros(Long micro) {
        if (micro == null) return null;
        return Instant.ofEpochMilli(micro / 1000)
                .atZone(ZoneOffset.UTC)
                .toLocalDateTime();
    }
}


