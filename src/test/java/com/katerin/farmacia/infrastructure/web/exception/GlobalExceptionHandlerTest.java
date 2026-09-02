package com.katerin.farmacia.infrastructure.web.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.katerin.farmacia.infrastructure.web.dto.ApiResponse;
import com.katerin.farmacia.infrastructure.web.dto.ErrorResponse;

public class GlobalExceptionHandlerTest {
    
    @Test
    void testHandleValidationExceptions() throws NoSuchMethodException {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        BeanPropertyBindingResult bindinResult = new BeanPropertyBindingResult(new Object(), "target");
        bindinResult.addError(new FieldError("target", "name", "no puede estar vacío"));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindinResult);

        ResponseEntity<ApiResponse> responseEntity = handler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(400, responseEntity.getBody().status());
        assertEquals("name:no puede estar vacío", responseEntity.getBody().message());
    }

    @Test
    void testHandleTypeMismatchExceptions() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException(
            "missing",
            Integer.class,
            "id",
            null,
            new NumberFormatException("For input string: \"missing\"")
        );

        ResponseEntity<ErrorResponse> responseEntity = handler.handleTypeMismatch(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(400, responseEntity.getBody().code());
        assertEquals("Invalid path parameter: id", responseEntity.getBody().message());
    }
}
