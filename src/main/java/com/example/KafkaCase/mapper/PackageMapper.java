package com.example.KafkaCase.mapper;
import com.example.KafkaCase.dto.MappedPackage;
import com.example.KafkaCase.entity.PackageEntity;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class PackageMapper {

    private static final String DATE_FORMAT="yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

    public MappedPackage map(PackageEntity p) {

        MappedPackage m = new MappedPackage();

        //creating feature
        m.setId(p.getId());
        m.setCreatedAt(p.getCreated_at().format(formatter));
        m.setLastUpdatedAt(p.getLast_updated_at().format(formatter));
        m.setEta(p.getEta());

        //if not complete
        if (!"COMPLETED".equals(p.getStatus())) {
            m.setCollectionDuration(null);
            m.setDeliveryDuration(null);
            m.setLeadTime(null);
            m.setOrderInTime(null);
            return m;
        }
        // no need null check because status completed.
        // calculate lead time
        Integer leadTime = calculateMinutes(p.getCreated_at(),p.getCompleted_at());
        m.setLeadTime(leadTime);

        //check order in time
        m.setOrderInTime(leadTime <= p.getEta());

        //calculate collection duration
        Integer collectionDuration=calculateMinutes(p.getCreated_at(),p.getPicked_up_at());
        m.setCollectionDuration(collectionDuration);

        //calculate delivery duration
        Integer deliveryDuration=calculateMinutes(p.getPicked_up_at(),p.getCompleted_at());
        m.setDeliveryDuration(deliveryDuration);

        return m;
    }
    // Returns the number of whole minutes between start and end.
    private Integer calculateMinutes(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return null;
        }
        return (int) Duration.between(start, end).toMinutes();
    }
}