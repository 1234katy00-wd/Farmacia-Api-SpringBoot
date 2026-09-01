package com.katerin.farmacia.application.service;

import java.util.List;

import com.katerin.farmacia.domain.model.Medication;
import com.katerin.farmacia.domain.model.Ticket;

public interface MedicationService {
    List<Medication> getAllMedications();
    Medication getMedicationById(Integer id);
    Medication createMedication(Medication medication);
    Medication updateMedication(Integer id, Medication medication);
    void deleteMedication(Integer id);
    List<Ticket> purchaseTickets(Integer medicationId, String medicationName, String customerEmail, int quantity);
    List <Ticket> getMedicationTickets(Integer medicationId);
}
