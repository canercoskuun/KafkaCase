package com.example.KafkaCase.repository;

import com.example.KafkaCase.dto.MiniPackage;
import com.example.KafkaCase.entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PackageEntityRepository extends JpaRepository<PackageEntity, Long> {
    //filter where extract the data -> more effective
    @Query("""
           SELECT NEW com.example.KafkaCase.dto.MiniPackage(
               p.id,
               p.completed_at,
               p.created_at,
               p.last_updated_at,
               p.eta,
               p.status,
               p.picked_up_at
           )
           FROM PackageEntity p
           WHERE p.cancelled = false
           """)
    List<MiniPackage> getAllActiveMiniPackages();
}
