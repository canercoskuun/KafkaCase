package com.example.KafkaCase.repository;
import com.example.KafkaCase.dto.MiniPackage;
import com.example.KafkaCase.entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;




@Repository
public interface PackageEntityRepository extends JpaRepository<PackageEntity, Long> {

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
       WHERE p.id = :id
         AND p.cancelled = false
       """)
    Optional<MiniPackage> getActiveMiniPackageById(@Param("id") Long id);

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
