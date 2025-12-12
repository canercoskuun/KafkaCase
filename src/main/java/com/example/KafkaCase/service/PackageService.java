package com.example.KafkaCase.service;
import com.example.KafkaCase.dto.MiniPackage;
import com.example.KafkaCase.exception.PackageNotFoundException;
import com.example.KafkaCase.mapper.PackageMapper;
import com.example.KafkaCase.repository.PackageEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class PackageService {
    private static final Logger logger = LoggerFactory.getLogger(PackageService.class);
    private final PackageEntityRepository repository;
    private final PackageMapper mapper;
    private final KafkaSenderService sender;

    public PackageService(PackageEntityRepository repository, PackageMapper mapper,KafkaSenderService sender) {
        this.repository = repository;
        this.mapper = mapper;
        this.sender = sender;
    }

    public String sendSingle(Long id) {
            MiniPackage p = repository.getActiveMiniPackageById(id)
                    .orElseThrow(() -> new PackageNotFoundException("Package not found or cancelled: " + id));

            sender.send("single_mapped_packages",String.valueOf(p.getId()),mapper.map(p));
            logger.info("Package {} sent to Kafka single_mapped_packages.", id);
            return "Package "+ id + " sent to Kafka.";
    }

    public String getAllActivePackagesAndSendKafka() {
        logger.info("Get all active packages");
        List<MiniPackage> list = repository.getAllActiveMiniPackages();

        if (list.isEmpty()) {
            logger.info("No packages found. Nothing to send.");
            return "No packages found.Nothing to send";
        }

        list.stream().
                map(mapper::map).
                forEach(mappedPackage  ->
                        sender.send("bootstrap_mapped_packages",String.valueOf(mappedPackage .getId()),mappedPackage));
        logger.info("Bootstrap sent {} packages to Kafka bootstrap_mapped_packages.", list.size());
        return "All packages sent to kafka.";
    }
}