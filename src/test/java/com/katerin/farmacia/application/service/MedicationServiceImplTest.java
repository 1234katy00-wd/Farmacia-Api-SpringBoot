package com.katerin.farmacia.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.katerin.farmacia.domain.model.Medication;
import com.katerin.farmacia.domain.model.Ticket;
import com.katerin.farmacia.infrastructure.persistence.MedicationEntity;
import com.katerin.farmacia.infrastructure.persistence.TicketEntity;
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
        MedicationEntity entity = medicationEntity(001, "Paracetamol", 950);
        when(medicationJpaRepository.findAll()).thenReturn(List.of(entity));

        List<Medication> medications = medicationService.getAllMedications();

        assertEquals(1, medications.size());
        assertEquals("Paracetamol", medications.get(0).medicationName());
    }

    @Test
    void testGetMedicationBySuccess() {
        MedicationEntity entity = medicationEntity(001, "Paracetamol", 950);
        when(medicationJpaRepository.findById(001)).thenReturn(Optional.of(entity));

        Medication medication = medicationService.getMedicationById(001);

        assertEquals(001, medication.id());
        assertEquals("Paracetamol", medication.medicationName());
        assertEquals(950, medication.ticketPrice());
    }

    @Test
    void testGetMedicationByIdNotFound() {
        when(medicationJpaRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> medicationService.getMedicationById(999));
    }

    @Test
    void testGetMedicationByIdNull() {
        Medication medication = new Medication(
                1, "MED-001", "Paracetamol", 0,null, null,
                null, "Description", "Ingredient", null, null);
        when(medicationJpaRepository.save(org.mockito.ArgumentMatchers.any(MedicationEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Medication created = medicationService.createMedication(medication);

        assertEquals(1, created.id());
        assertEquals("OPEN", created.status());
        assertEquals(0, created.ticketPrice()); 
        assertEquals(0, created.totalTickets());
        assertEquals(0, created.availableTickets());
        verify(medicationJpaRepository).save(org.mockito.ArgumentMatchers.any(MedicationEntity.class));
    }

    @Test
    void testCreateMedicationWithDefaults() {
        Medication medication = new Medication(
                001, "MED-001", "Paracetamol", null, null, null,
                null, "Description", "Ingredient", null, null);
        when(medicationJpaRepository.save(org.mockito.ArgumentMatchers.any(MedicationEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Medication created = medicationService.createMedication(medication);

        assertEquals(001, created.id());
        assertEquals("OPEN", created.status());
        assertNull(created.ticketPrice());
        assertEquals(0, created.totalTickets());
        assertEquals(0, created.availableTickets());
        verify(medicationJpaRepository).save(org.mockito.ArgumentMatchers.any(MedicationEntity.class));
    }

    @Test
    void testCreateMedicationRejectsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> medicationService.createMedication(null));

        assertEquals("Medication must not be null", exception.getMessage());
    }

    @Test
    void testUpdateMedication() {
        MedicationEntity entity = medicationEntity(001, "Paracetamol", 950);
        Medication update = new Medication(
                null, "MED-002", "Ibuprofeno", 1200, null, null,
                "CLOSED", null, null, null, 2);
        when(medicationJpaRepository.findById(001)).thenReturn(Optional.of(entity));
        when(medicationJpaRepository.save(entity)).thenReturn(entity);

        Medication updated = medicationService.updateMedication(001, update);

        assertEquals(001, updated.id());
        assertEquals("MED-002", updated.code());
        assertEquals("Ibuprofeno", updated.medicationName());
        assertEquals("CLOSED", updated.status());
        assertEquals(2, updated.availableTickets());
        assertEquals(1200, updated.ticketPrice());
    }

    @Test
    void testUpdateMedicationNotFound() {
        when(medicationJpaRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> medicationService.updateMedication(999, medication(999, "Missing", 0)));
    }

    @Test
    void testDeleteMedicationDeletesTicketsAndMedication() {
        MedicationEntity entity = medicationEntity(001, "Paracetamol", 950);
        TicketEntity ticket = ticketEntity(001, entity);
        when(medicationJpaRepository.existsById(001)).thenReturn(true);
        when(ticketJpaRepository.findByMedicationId(001)).thenReturn(List.of(ticket));

        medicationService.deleteMedication(001);

        verify(ticketJpaRepository).deleteAll(List.of(ticket));
        verify(medicationJpaRepository).deleteById(001);
    }

    @Test
    void testDeleteMedicationNotFound() {
        when(medicationJpaRepository.existsById(999)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> medicationService.deleteMedication(999));
    }

    @Test
    void testPurchaseTickets() {
        MedicationEntity medication = medicationEntity(001, "Paracetamol", 950);
        when(medicationJpaRepository.findById(001)).thenReturn(Optional.of(medication));
        when(ticketJpaRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        List<Ticket> tickets = medicationService.purchaseTickets(
                001, "Fallback name", "ana@example.com", 2);

        assertEquals(2, tickets.size());
        assertEquals("MED-001", tickets.get(0).code());
        assertEquals(1, tickets.get(0).medicationId());
        assertEquals("Paracetamol", tickets.get(0).medicationName());
        assertEquals("ana@example.com", tickets.get(0).customerEmail());
        assertEquals(950, tickets.get(0).totalPrice());
        verify(ticketJpaRepository).saveAll(anyList());
    }

    @Test
    void testPurchaseTicketsRejectsNonPositiveQuantity() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> medicationService.purchaseTickets(001, "Paracetamol", "ana@example.com", 0));

        assertEquals("Quantity must be greater than zero", exception.getMessage());
    }

    @Test
    void testPurchaseTicketsMedicationNotFound() {
        when(medicationJpaRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> medicationService.purchaseTickets(999, "Missing", "ana@example.com", 1));
    }

    @Test
    void testGetMedicationTickets() {
        MedicationEntity medication = medicationEntity(001, "Paracetamol", 950);
        when(ticketJpaRepository.findByMedicationId(001))
                .thenReturn(List.of(ticketEntity(001, medication)));

        List<Ticket> tickets = medicationService.getMedicationTickets(001);

        assertEquals(1, tickets.size());
        assertEquals(001, tickets.get(0).id());
        assertEquals(1, tickets.get(0).medicationId());
        assertEquals("MED-001", tickets.get(0).code());
    }

    private Medication medication(Integer id, String name, int price) {
        return new Medication(id, "MED-001", name, price, null, null,
                "OPEN", "Description", "Ingredient", 3, 3);
    }

    private TicketEntity ticketEntity(Integer id, MedicationEntity medication) {
        return new TicketEntity(id, "TCK-001", medication, "ana@example.com",
                medication.getMedicationName(), medication.getTicketPrice(), "2026-08-27");
    }

    private MedicationEntity medicationEntity(Integer id, String name, int price) {
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
