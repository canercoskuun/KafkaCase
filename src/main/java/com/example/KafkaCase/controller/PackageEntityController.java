package com.example.KafkaCase.controller;

import com.example.KafkaCase.service.PackageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/kafka")
public class PackageEntityController {

    private final PackageService service;

    public PackageEntityController(PackageService service) {
        this.service = service;
    }

    @GetMapping("/send/{packageId}")
    public ResponseEntity<String> sendSingle(@PathVariable Long packageId) {
        return ResponseEntity.ok(service.sendSingle(packageId));
    }

    @GetMapping("/bootstrap")
    public ResponseEntity<String> bootstrap() {
        return ResponseEntity.ok(service.getAllActivePackagesAndSendKafka());
    }
}