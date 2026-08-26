package com.katerin.farmacia.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.katerin.farmacia.infrastructure.persistence.repository.MedicationJpaRepository;

@SpringBootTest
public class MedicationJpaRepositoryTest {
    
    @Autowired
    private MedicationJpaRepository medicationJpaRepository;

    @Test 
    public void  shouldHaveSaveAndRetrieveConcertEntitySuccessfully () {

        MedicationEntity entity = new MedicationEntity(
            "M-001",
            "MED-001",
            LocalDate.of(2025, 2, 19),
            LocalDate.of(2028, 9, 12),
            "Paracetamol",
            "OPEN",
            950,
            "Medicamento utilizado para la fiebre y dolores leves.",
            "acetaminofén",
            950,
            3,
            3);

        medicationJpaRepository.save(entity);
        Optional<MedicationEntity> searchId = medicationJpaRepository.findById("M-001");

        assertTrue(searchId.isPresent(), "El medicamento ya existe.");
        assertEquals("Paracetamol", searchId.get().getMedicationName());
        
    }
    
}
