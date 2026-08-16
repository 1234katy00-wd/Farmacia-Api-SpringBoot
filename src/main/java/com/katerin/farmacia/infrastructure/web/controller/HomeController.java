package com.katerin.farmacia.infrastructure.web.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.katerin.farmacia.application.service.MedicationService;
import com.katerin.farmacia.domain.model.Medication;

@RestController
public class HomeController {
    private final MedicationService medicationService;

    public HomeController(MedicationService medicationService){
        this.medicationService = medicationService;
    }

    @GetMapping(value = "/healthcheck", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> healthcheck() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }

    @GetMapping("/api/v1/medications")
    public List<Medication> getMedications(){
        return medicationService.getMedications();
    }

    @GetMapping("/api/v1/medications/{id}")
    public ResponseEntity<Medication> getMedicationById(@PathVariable(name = "id") Integer id){
        Medication medication = medicationService.getMedicationById(id);
        if(medication == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(medication);
    }
    
}
