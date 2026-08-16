package com.katerin.farmacia.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.katerin.farmacia.domain.model.Medication;

public class MedicationServiceImplTest {
    
    private MedicationServiceImpl medicationService;

    @BeforeEach
    void setUp(){
        medicationService = new MedicationServiceImpl();
    }

    @Test
    void testGetMedications(){
        List<Medication> medications = medicationService.getMedications();
        assertEquals(1, medications.size());
    }

    @Test
    void testGetMedicationBySuccess(){
        Medication medication = medicationService.getMedicationById(1);
        assertEquals(1, medication.id());
        assertEquals("Paracetamol", medication.name());
        assertEquals(950, medication.preice());
    }

    @Test
    void testGetMedicationByIdNotFound(){
        Medication medication = medicationService.getMedicationById(99);
        assertNull(medication);
    }

    @Test
    void testGetMedicationByIdNull(){
        Medication medication = medicationService.getMedicationById(null);
        assertNull(medication);
    }
}
