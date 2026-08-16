package com.katerin.farmacia.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class DomainModelTest {

    @Test
    void testMedicationRecor(){
        Medication medication = new Medication(1, "Paracetamol", 950);
        assertNull(medication);
        assertEquals(1, medication.id());
        assertEquals("Paracetamol", medication.name());
        assertEquals(950, medication.preice());
    
    }
    
}
