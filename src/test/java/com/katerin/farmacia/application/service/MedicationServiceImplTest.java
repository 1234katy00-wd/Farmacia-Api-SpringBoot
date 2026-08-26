package com.katerin.farmacia.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.katerin.farmacia.domain.model.Medication;
import com.katerin.farmacia.infrastructure.persistence.MedicationEntity;
import com.katerin.farmacia.infrastructure.persistence.repository.MedicationJpaRepository;
import com.katerin.farmacia.infrastructure.persistence.repository.TicketJpaRepository;

@ExtendWith(MockitoExtension.class)
public class MedicationServiceImplTest {

    @Mock
    private MedicationJpaRepository medicationJpaRepository;

    @Mock
    private TicketJpaRepository ticketJpaRepository;

    private MedicationServiceImpl medicationService;

    @BeforeEach
    void setUp() {
        medicationService = new MedicationServiceImpl(
                medicationJpaRepository,
                ticketJpaRepository);
    }

    @Test
    void testGetAllMedications() {
        MedicationEntity entity = medicationEntity("M-001", "Paracetamol", 950);
        when(medicationJpaRepository.findAll()).thenReturn(List.of(entity));

        List<Medication> medications = medicationService.getAllMedications();

        assertEquals(1, medications.size());
    }

    @Test
    void testGetMedicationBySuccess() {
        MedicationEntity entity = medicationEntity("M-001", "Paracetamol", 950);
        when(medicationJpaRepository.findById("M-001")).thenReturn(Optional.of(entity));

        Medication medication = medicationService.getMedicationById("M-001");

        assertEquals("M-001", medication.id());
        assertEquals("Paracetamol", medication.medicationName());
        assertEquals(950, medication.ticketPrice());
    }

    @Test
    void testGetMedicationByIdNotFound() {
        when(medicationJpaRepository.findById("M-999")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> medicationService.getMedicationById("M-999"));
    }

    @Test
    void testGetMedicationByIdNull() {
        when(medicationJpaRepository.findById(null)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> medicationService.getMedicationById(null));
    }

    private MedicationEntity medicationEntity(String id, String name, int price) {
        return new MedicationEntity(
                id,
                "MED-001",
                null,
                null,
                name,
                "OPEN",
                price,
                "Description",
                "Ingredient",
                price,
                3,
                3);
    }
}
