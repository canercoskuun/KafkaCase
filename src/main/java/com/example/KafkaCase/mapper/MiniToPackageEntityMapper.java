package com.example.KafkaCase.mapper;

import com.example.KafkaCase.dto.MiniPackage;
import com.example.KafkaCase.entity.PackageEntity;
import org.springframework.stereotype.Component;

@Component
public class MiniToPackageEntityMapper {

    public PackageEntity  mapToPackage(MiniPackage mini) {
        PackageEntity entity = new PackageEntity();
        entity.setId(mini.getId());
        entity.setCompleted_at(mini.getCompleted_at());
        entity.setCreated_at(mini.getCreated_at());
        entity.setLast_updated_at(mini.getLast_updated_at());
        entity.setEta(mini.getEta());
        entity.setStatus(mini.getStatus());
        entity.setPicked_up_at(mini.getPicked_up_at());
        return entity;
    }
}