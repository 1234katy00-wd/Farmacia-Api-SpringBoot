package com.katerin.farmacia.application.service;

import java.util.List;

import com.katerin.farmacia.domain.model.Medication;
import com.katerin.farmacia.domain.model.Ticket;

public interface MedicationService {
    List<Medication> getAllMedications();
    Medication getMedicationById(String id);
    Medication createMedication(Medication medication);
    Medication updateMedication(String id, Medication medication);
    void deleteMedication(String id);
    List<Ticket> purchaTickets(String medicationId, String medicationName, String customerEmail, int quantity);
    List <Ticket> getMedicationTickets(String medicationId);
}
