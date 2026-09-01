package com.katerin.farmacia.domain.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class DomainExceptionTest {

    @Test
    public void shouldCreateResourceNotFoundException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Medication not found");

        assertEquals("Medication not found", exception.getMessage());
        assertInstanceOf(RuntimeException.class, exception);

        assertThrows(ResourceNotFoundException.class,
            () -> { throw new ResourceNotFoundException("Medication not found"); });
    }

    @Test
    public void shouldCreateOutOfStockException() {
        OutOfStockException exception = new OutOfStockException("Not enough stock");

        assertEquals("Not enough stock", exception.getMessage());
        assertInstanceOf(RuntimeException.class, exception);
        assertThrows(OutOfStockException.class,
            () -> { throw new OutOfStockException("Not enough stock"); });
    }

    @Test
    public void shouldCreatePrescriptionNotProvidedException() {
        PrescriptionNotProvidedException exception =
                new PrescriptionNotProvidedException("Prescription required");

        assertEquals("Prescription required", exception.getMessage());
        assertInstanceOf(RuntimeException.class, exception);

        assertThrows(PrescriptionNotProvidedException.class,
            () -> { throw new PrescriptionNotProvidedException("Prescription required"); });
    }
}
