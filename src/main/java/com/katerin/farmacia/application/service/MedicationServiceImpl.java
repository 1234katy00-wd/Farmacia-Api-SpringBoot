package com.katerin.farmacia.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.katerin.farmacia.domain.model.Medication;

@Service
public class MedicationServiceImpl implements MedicationService {
    
    public List<Medication> getMedications(){
        return List.of(
            new Medication(1, "Paracetamol", 950),
            new Medication(2, "Ibuprofeno", 1200),
            new Medication(2, "Amoxicilina", 7490)
        );       
    }

    @Override
    public Medication getMedicationById(Integer id){
        if(id == null){
            return null;
        }
        return getMedications().stream()
        .filter(medication -> id.equals(medication.id()))
        .findFirst()
        .orElse(null);
    }

}
