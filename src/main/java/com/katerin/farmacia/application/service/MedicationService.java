package com.katerin.farmacia.application.service;

import java.util.List;

import com.katerin.farmacia.domain.model.Medication;

public interface MedicationService {
    List<Medication> getMedications();
    Medication getMedicationById(Integer id);
}
