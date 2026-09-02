package com.katerin.farmacia.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.katerin.farmacia.infrastructure.persistence.repository.MedicationJpaRepository;
import com.katerin.farmacia.infrastructure.persistence.repository.TicketJpaRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MedicationJpaRepositoryTest {

    @BeforeEach
    void cleanDatabase() {
        ticketJpaRepository.deleteAll();
        medicationJpaRepository.deleteAll();
    }
    
    @Autowired
    private MedicationJpaRepository medicationJpaRepository;

    @Autowired
    private TicketJpaRepository ticketJpaRepository;

    @Test
    public void shouldSaveAndRetrieveMedicationSuccessfully() {
        MedicationEntity entity = medication(null, "Paracetamol");

        MedicationEntity saved = medicationJpaRepository.saveAndFlush(entity);
        Optional<MedicationEntity> searchId = medicationJpaRepository.findById(saved.getId());

        assertTrue(searchId.isPresent(), "El medicamento ya existe.");
        assertEquals("Paracetamol", searchId.get().getMedicationName());
        assertEquals("OPEN", searchId.get().getStatus());
        assertEquals(950, searchId.get().getTicketPrice());
    }

    @Test
    public void shouldFindAllSavedMedications() {
        medicationJpaRepository.saveAndFlush(medication(null, "Paracetamol"));
        medicationJpaRepository.saveAndFlush(medication(null, "Ibuprofeno"));

        assertEquals(2, medicationJpaRepository.findAll().size());
    }

    @Test
    public void shouldUpdateMedicationSuccessfully() {
        MedicationEntity entity = medication(null, "Paracetamol");
        MedicationEntity saved = medicationJpaRepository.saveAndFlush(entity);

        saved.setMedicationName("Ibuprofeno");
        saved.setAvailableTickets(2);
        medicationJpaRepository.saveAndFlush(saved);

        MedicationEntity updated = medicationJpaRepository.findById(saved.getId()).orElseThrow();
        assertEquals("Ibuprofeno", updated.getMedicationName());
        assertEquals(2, updated.getAvailableTickets());
    }

    @Test
    public void shouldDeleteMedicationSuccessfully() {
        MedicationEntity saved = medicationJpaRepository.saveAndFlush(medication(null, "Paracetamol"));
        medicationJpaRepository.deleteById(saved.getId());

        assertFalse(medicationJpaRepository.existsById(saved.getId()));
    }

    @Test
    public void shouldFindTicketsByMedicationId() {
        MedicationEntity medication = medication(null, "Paracetamol");
        MedicationEntity savedMedication = medicationJpaRepository.saveAndFlush(medication);
        ticketJpaRepository.save(new TicketEntity(
                null,
                "TCK-001",
                savedMedication,
                "ana@example.com",
                "Paracetamol",
                950,
                "2026-08-27"));

        assertEquals(1, ticketJpaRepository.findByMedicationId(savedMedication.getId()).size());
        assertEquals("TCK-001", ticketJpaRepository.findByMedicationId(savedMedication.getId()).get(0).getCode());
    }

    private MedicationEntity medication(Integer id, String name) {
        return new MedicationEntity(
                id,
                "MED-001",
                LocalDate.of(2025, 2, 19),
                LocalDate.of(2028, 9, 12),
                name,
                "OPEN",
                950,
                "Medicamento utilizado para la fiebre y dolores leves.",
                "acetaminofén",
                950,
                3,
                3);
    }
    
}
