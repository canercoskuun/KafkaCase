package com.example.KafkaCase.repository;

import com.example.KafkaCase.entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PackageEntityRepository extends JpaRepository<PackageEntity, Long> {
    //filter where extract the data -> more effective
    @Query("SELECT p FROM PackageEntity p WHERE p.cancelled = false")
    List<PackageEntity> getAllActivePackages();
}
