package com.katerin.farmacia.infrastructure.web.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.katerin.farmacia.infrastructure.web.dto.ApiResponse;

public class GlobalExceptionHandlerTest {
    
    @Test
    void testHandleValidationExceptions() throws NoSuchMethodException {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        BeanPropertyBindingResult bindinResult = new BeanPropertyBindingResult(new Object(), "target");
        bindinResult.addError(new FieldError("target", "name", "no puede estar vacío"));

        MethodParameter parameter = new MethodParameter(
            this.getClass().getDeclaredMethod("estHandleValidationExceptions"), -1);

            MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindinResult);
            
            ResponseEntity<ApiResponse> responseEntity = handler.handleValidationExceptions(exception);

            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
            assertNotNull(responseEntity.getBody());

    }
}
