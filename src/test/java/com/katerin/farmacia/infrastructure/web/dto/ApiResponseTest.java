package com.katerin.farmacia.infrastructure.web.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class ApiResponseTest {

    @Test
    void testOkWithMessageAndName() {
        ApiResponse response = ApiResponse.ok("Éxito", "Katerin");
        assertEquals(200, response.status());
        assertEquals("Éxito", response.message());
        assertEquals("Katerin", response.name());
        assertNotNull(response.timestamp());
    }

    @Test
    void testOkWithMessageOnly() {
        ApiResponse response = ApiResponse.ok("Operación realizada");
        assertEquals(200, response.status());
        assertEquals("Operación realizada", response.message());
        assertNull(response.name());
        assertNotNull(response.timestamp());
    }

    @Test
    void testError() {
        ApiResponse response = ApiResponse.error(400,"Error en la petición" );
        assertEquals(400, response.status());
        assertEquals("Error en la petición", response.message());
        assertNull(response.name());
        assertNotNull(response.timestamp());
    }

    @Test
    void shouldReturnNotFoundErrorResponse() {
        ApiResponse response = ApiResponse.error(404, "Recurso no encontrado");

        assertEquals(404, response.status());
        assertEquals("Recurso no encontrado", response.message());
        assertNull(response.name());
        assertNotNull(response.timestamp());
    }
}
