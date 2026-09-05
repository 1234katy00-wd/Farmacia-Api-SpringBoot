package com.katerin.farmacia.infrastructure.web.controller;

import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import java.time.LocalDate;
import java.util.UUID;

import com.katerin.farmacia.infrastructure.persistence.MedicationEntity;
import com.katerin.farmacia.infrastructure.persistence.repository.MedicationJpaRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MedicationIntegrationTest {


    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private MedicationJpaRepository medicationJpaRepository;

    @Test
    @DisplayName("GET /api/v1/medications debe retornar la lista de medicamentos con DTO alineado para el Frontend")
    public void shouldReturnAllMedicationMatchingFrontendContract() throws Exception {
        String uniqueMedicationCode = "MED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String uniqueMedicationId = "MED-ID-" + UUID.randomUUID().toString().substring(0, 8);


        MedicationEntity medication = new MedicationEntity(
                null,
                uniqueMedicationCode,
                LocalDate.of(2025, 1, 10),
                LocalDate.of(2028, 1, 10),
                "Paracetamol",
                "OPEN",
                950,
                "Medicamento para dolor y fiebre",
                "Acetaminofén",
                950,
                3,
                3,
                "Laboratorio Chile"
        );

        medicationJpaRepository.save(medication);

        mockMvc.perform(get("/api/v1/medications")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[*].code", hasItem(uniqueMedicationCode)))
            .andExpect(jsonPath("$[?(@.code == '" + uniqueMedicationCode + "')].medicationName").value("Paracetamol"))
            .andExpect(jsonPath("$[?(@.code == '" + uniqueMedicationCode + "')].status").value("OPEN"))
            .andExpect(jsonPath("$[?(@.code == '" + uniqueMedicationCode + "')].description").value("Medicamento para dolor y fiebre"))  
            .andExpect(jsonPath("$[?(@.code == '" + uniqueMedicationCode + "')].activeIngredient").value("Acetaminofén"))
            .andExpect(jsonPath("$[?(@.code == '" + uniqueMedicationCode + "')].laboratory").value("Laboratorio Chile"))
            .andExpect(jsonPath("$[?(@.code == '" + uniqueMedicationCode + "')].ticketPrice").value(950))
            .andExpect(jsonPath("$[?(@.code == '" + uniqueMedicationCode + "')].availableTickets").value(3))    
            .andExpect(jsonPath("$[?(@.code == '" + uniqueMedicationCode + "')].totalTickets").value(3))    
            .andExpect(jsonPath("$[?(@.code == '" + uniqueMedicationCode + "')].LocalDate.of").value("2025, 1, 10"))
            .andExpect(jsonPath("$[?(@.code == '" + uniqueMedicationCode + "')].LocalDate.of").value("2028, 1, 10"))
            .andExpect(jsonPath("$[?(@.code == '" + uniqueMedicationCode + "')].availableMedication").value(3));
            
    }
}
