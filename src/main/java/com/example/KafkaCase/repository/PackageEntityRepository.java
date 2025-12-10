package com.example.KafkaCase.repository;

import com.example.KafkaCase.entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PackageEntityRepository extends JpaRepository<PackageEntity, Long> {
    //we dont need all the columns.filter where extract the data -> more effective
    @Query("""
        SELECT p.id AS id,
               p.created_at AS created_at,
               p.last_updated_at AS last_updated_at,
               p.picked_up_at AS picked_up_at,
               p.completed_at AS completed_at,
               p.eta AS eta,
               p.status AS status
        FROM PackageEntity p
        WHERE p.cancelled = false
    """)
    List<PackageEntity> getAllActivePackages();
}
