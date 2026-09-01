package com.katerin.farmacia.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.katerin.farmacia.infrastructure.persistence.repository.MedicationJpaRepository;
import com.katerin.farmacia.infrastructure.persistence.repository.TicketJpaRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class MedicationJpaRepositoryTest {
    
    @Autowired
    private MedicationJpaRepository medicationJpaRepository;

    @Autowired
    private TicketJpaRepository ticketJpaRepository;

    @Test
    public void shouldSaveAndRetrieveMedicationSuccessfully() {
        MedicationEntity entity = medication(001, "Paracetamol");

        medicationJpaRepository.save(entity);
        Optional<MedicationEntity> searchId = medicationJpaRepository.findById(001);

        assertTrue(searchId.isPresent(), "El medicamento ya existe.");
        assertEquals("Paracetamol", searchId.get().getMedicationName());
        assertEquals("OPEN", searchId.get().getStatus());
        assertEquals(950, searchId.get().getTicketPrice());
    }

    @Test
    public void shouldFindAllSavedMedications() {
        medicationJpaRepository.save(medication(001, "Paracetamol"));
        medicationJpaRepository.save(medication(002, "Ibuprofeno"));

        assertEquals(2, medicationJpaRepository.findAll().size());
    }

    @Test
    public void shouldUpdateMedicationSuccessfully() {
        MedicationEntity entity = medication(001, "Paracetamol");
        medicationJpaRepository.save(entity);

        entity.setMedicationName("Ibuprofeno");
        entity.setAvailableTickets(2);
        medicationJpaRepository.save(entity);

        MedicationEntity updated = medicationJpaRepository.findById(001).orElseThrow();
        assertEquals("Ibuprofeno", updated.getMedicationName());
        assertEquals(2, updated.getAvailableTickets());
    }

    @Test
    public void shouldDeleteMedicationSuccessfully() {
        medicationJpaRepository.save(medication(001, "Paracetamol"));
        medicationJpaRepository.deleteById(001);

        assertFalse(medicationJpaRepository.existsById(001));
    }

    @Test
    public void shouldFindTicketsByMedicationId() {
        MedicationEntity medication = medication(001, "Paracetamol");
        medicationJpaRepository.save(medication);
        ticketJpaRepository.save(new TicketEntity(
                001,
                "TCK-001",
                medication,
                "ana@example.com",
                "Paracetamol",
                950,
                "2026-08-27"));

        assertEquals(1, ticketJpaRepository.findByMedicationId(001).size());
        assertEquals("T-001", ticketJpaRepository.findByMedicationId(001).get(0).getId());
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
