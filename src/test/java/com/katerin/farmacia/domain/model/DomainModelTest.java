package com.katerin.farmacia.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class DomainModelTest {

    @Test
    void testMedicationRecor(){
        Medication medication = new Medication(
            "M-001",
            "MED-001",
            "Paracetamol",
            950,
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2028, 1, 1),
            "OPEN",
            "Description",
            "Ingredient",
            3,
            3);
        assertNotNull(medication);
        assertEquals("M-001", medication.id());
        assertEquals("Paracetamol", medication.medicationName());
        assertEquals(950, medication.ticketPrice());
    
    }
    
}
