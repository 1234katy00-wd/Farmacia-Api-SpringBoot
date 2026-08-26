package com.katerin.farmacia.infrastructure.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PurchaseRequestDto( 
    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    String customerEmail,

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    Integer quantity,

    @NotBlank(message = "Customer name is required")
    String medicationName
) {
}
