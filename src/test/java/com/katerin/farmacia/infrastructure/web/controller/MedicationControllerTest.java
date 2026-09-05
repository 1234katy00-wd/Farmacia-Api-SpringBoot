package com.katerin.farmacia.infrastructure.web.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.katerin.farmacia.application.service.MedicationService;
import com.katerin.farmacia.domain.exception.OutOfStockException;
import com.katerin.farmacia.domain.exception.ResourceNotFoundException;
import com.katerin.farmacia.domain.model.Medication;
import com.katerin.farmacia.domain.model.Ticket;

@WebMvcTest(MedicationController.class)
public class MedicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicationService medicationService;
    
    @Test
    public void shouldGetAllMedication() throws Exception {
        Medication medication = new Medication(
            001,
            "MED-001",
            "Paracetamol",
            950,
            null,
            null,
            "OPEN",
            null,
            null,
            3,
            3,
            null);
        when(medicationService.getAllMedications()).thenReturn(List.of(medication));

        mockMvc.perform(get("/api/v1/medications"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(001))
            .andExpect(jsonPath("$[0].code").value("MED-001"))
            .andExpect(jsonPath("$[0].medicationName").value("Paracetamol"))
            .andExpect(jsonPath("$[0].status").value("OPEN"))
            .andExpect(jsonPath("$[0].availableMedication").value(3));
    }

    @Test
    public void shouldGetMedicationById() throws Exception {
        when(medicationService.getMedicationById(001)).thenReturn(medication());

        mockMvc.perform(get("/api/v1/medications/001"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(001))
            .andExpect(jsonPath("$.code").value("MED-001"))
            .andExpect(jsonPath("$.medicationName").value("Paracetamol"));
    }

    @Test
    public void shouldReturnNotFoundWhenMedicationDoesNotExist() throws Exception {
        when(medicationService.getMedicationById(999))
            .thenThrow(new ResourceNotFoundException("Medication with id missing not found"));

        mockMvc.perform(get("/api/v1/medications/missing"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("Invalid path parameter: id"));
    }

    @Test
    public void shouldCreateMedication() throws Exception {
        when(medicationService.createMedication(any(Medication.class))).thenReturn(medication());

        mockMvc.perform(post("/api/v1/medications")
                .contentType(APPLICATION_JSON)
                .content("""
                    {"code":"MED-001","medicationName":"Paracetamol","totalPrice":950,"status":"OPEN","availableMedication":3}
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(001))
            .andExpect(jsonPath("$.code").value("MED-001"));
    }
    
    @Test
    public void shouldReturnBadRequestWhenCreateMedicationValidationFails() throws Exception {
        mockMvc.perform(post("/api/v1/medications")
                .contentType(APPLICATION_JSON)
                .content("""
                    {"code":"","medicationName":"Paracetamol","availableMedication":3}
                    """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400));

        verifyNoInteractions(medicationService);
    }

    @Test
    public void shouldUpdateMedication() throws Exception {
        when(medicationService.updateMedication(any(Integer.class), any(Medication.class)))
            .thenReturn(medication());

        mockMvc.perform(put("/api/v1/medications/001")
                .contentType(APPLICATION_JSON)
                .content("""
                    {"code":"MED-002","medicationName":"Ibuprofeno","totalPrice":1200,"status":"OPEN","availableMedication":4}
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(001));

        verify(medicationService).updateMedication(any(Integer.class), any(Medication.class));
    }

    @Test
    public void shouldDeleteMedication() throws Exception {
        mockMvc.perform(delete("/api/v1/medications/001"))
            .andExpect(status().isNoContent());

        verify(medicationService).deleteMedication(001);
    }

    @Test
    public void shouldPurchaseTickets() throws Exception {
        when(medicationService.purchaseTickets(001, "Paracetamol", "ana@example.com", 2))
            .thenReturn(List.of(ticket(001), ticket(002)));

        mockMvc.perform(post("/api/v1/medications/001/purchase")
                .contentType(APPLICATION_JSON)
                .content("""
                    {"customerEmail":"ana@example.com","quantity":2,"medicationName":"Paracetamol"}
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(001))
            .andExpect(jsonPath("$[0].medicationId").value(001))
            .andExpect(jsonPath("$[1].id").value(002));
    }

    @Test
    public void shouldReturnUnprocessableEntityWhenPurchaseOutOfStock() throws Exception {
        when(medicationService.purchaseTickets(001, "Paracetamol", "ana@example.com", 4))
            .thenThrow(new OutOfStockException("Not enough stock"));

        mockMvc.perform(post("/api/v1/medications/001/purchase")
                .contentType(APPLICATION_JSON)
                .content("""
                    {"customerEmail":"ana@example.com","quantity":4,"medicationName":"Paracetamol"}
                    """))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.code").value(422));
    }

    @Test
    public void shouldReturnNotFoundWhenPurchaseMedicationMissing() throws Exception {
        when(medicationService.purchaseTickets(999, "Paracetamol", "ana@example.com", 1))
            .thenThrow(new ResourceNotFoundException("Medication not found"));

        mockMvc.perform(post("/api/v1/medications/999/purchase")
                .contentType(APPLICATION_JSON)
                .content("""
                    {"customerEmail":"ana@example.com","quantity":1,"medicationName":"Paracetamol"}
                    """))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    public void shouldReturnBadRequestWhenPurchaseMedicationNotStock() throws Exception {
        when(medicationService.purchaseTickets(0001, "Paracetamol", "ana@example.com", 1))
            .thenThrow(new ResourceNotFoundException("Medication not found"));

        mockMvc.perform(post("/api/v1/medications/missing/purchase")
                .contentType(APPLICATION_JSON)
                .content("""
                    {"customerEmail":"ana@example.com","quantity":1,"medicationName":"Paracetamol"}
                    """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    public void shouldGetMedicationTickets() throws Exception {
        when(medicationService.getMedicationTickets(001))
            .thenReturn(List.of(ticket(001)));

        mockMvc.perform(get("/api/v1/medications/001/tickets"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(001))
            .andExpect(jsonPath("$[0].customerEmail").value("ana@example.com"));
    }

    @Test
    public void shouldCreateMedicationWithNullStatus() throws Exception {
        when(medicationService.createMedication(any(Medication.class))).thenReturn(medication());

        mockMvc.perform(post("/api/v1/medications")
                .contentType(APPLICATION_JSON)
                .content("""
                    {"code":"MED-001","medicationName":"Paracetamol","availableMedication":3}
                    """))
            .andExpect(status().isCreated());

        org.mockito.ArgumentCaptor<Medication> captor = org.mockito.ArgumentCaptor.forClass(Medication.class);
        verify(medicationService).createMedication(captor.capture());
        org.junit.jupiter.api.Assertions.assertEquals("OPEN", captor.getValue().status());
    }

    @Test
    public void shouldPurchaseTicketsReturningEmptyList() throws Exception {
        when(medicationService.purchaseTickets(001, "Paracetamol", "ana@example.com", 1))
            .thenReturn(List.of());

        mockMvc.perform(post("/api/v1/medications/001/purchase")
                .contentType(APPLICATION_JSON)
                .content("""
                    {"customerEmail":"ana@example.com","quantity":1,"medicationName":"Paracetamol"}
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());
    }

    private Medication medication() {
        return new Medication(001, "MED-001", "Paracetamol", 950, null, null,
            "OPEN", null, null, 3, 3, null);
    }

    private Ticket ticket(Integer id) {
        return new Ticket(id, "TCK-001", 001,"Paracetamol", 950,
            "ana@example.com", "2026-08-27");
    }




}
