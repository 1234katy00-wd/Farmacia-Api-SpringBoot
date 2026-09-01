package com.katerin.farmacia.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class DomainModelTest {

    @Test
    void testMedicationRecor(){
        Medication medication = new Medication(
            001,
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
        assertEquals(001, medication.id());
        assertEquals("MED-001", medication.code());
        assertEquals("Paracetamol", medication.medicationName());
        assertEquals(950, medication.ticketPrice());
        assertEquals(LocalDate.of(2026, 1, 1), medication.creationDate());
        assertEquals(LocalDate.of(2028, 1, 1), medication.dueDate());
        assertEquals("OPEN", medication.status());
        assertEquals("Description", medication.description());
        assertEquals("Ingredient", medication.activeIngredient());
        assertEquals(3, medication.totalTickets());
        assertEquals(3, medication.availableTickets());
        }

        @Test
        void testTicketRecord() {
        Ticket ticket = new Ticket(
            001,
            "TCK-001",
            001,
            "Paracetamol",
            950,
            "ana@example.com",
            "2026-08-27");

        assertNotNull(ticket);
        assertEquals(001, ticket.id());
        assertEquals("TCK-001", ticket.code());
        assertEquals(001, ticket.medicationId());
        assertEquals("Paracetamol", ticket.medicationName());
        assertEquals(950, ticket.totalPrice());
        assertEquals("ana@example.com", ticket.customerEmail());
        assertEquals("2026-08-27", ticket.purchaseDate());
        }

        @Test
        void recordsWithSameValuesShouldBeEqual() {
        Medication first = new Medication(
            001, "MED-001", "Paracetamol", 950,
            LocalDate.of(2026, 1, 1), LocalDate.of(2028, 1, 1),
            "OPEN", "Description", "Ingredient", 3, 3);
        Medication second = new Medication(
            001, "MED-001", "Paracetamol", 950,
            LocalDate.of(2026, 1, 1), LocalDate.of(2028, 1, 1),
            "OPEN", "Description", "Ingredient", 3, 3);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        }

        @Test
        void recordsWithDifferentValuesShouldNotBeEqual() {
            Medication first = new Medication(
                1, "MED-001", "Paracetamol", 950,
                LocalDate.of(2026, 1, 1), LocalDate.of(2028, 1, 1),
                "OPEN", "Description", "Ingredient", 3, 3);
            Medication second = new Medication(
                2, "MED-002", "Ibuprofeno", 1200,
                LocalDate.of(2026, 2, 1), LocalDate.of(2028, 2, 1),
                "CLOSED", "Other", "OtherIngredient", 5, 5);

            assertNotEquals(first, second);
        }

    
}
