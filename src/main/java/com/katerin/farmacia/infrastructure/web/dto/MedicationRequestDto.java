package com.katerin.farmacia.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record MedicationRequestDto(
    @Schema(description = "Código único del medicamento", example = "MED-001")
    @NotBlank(message = "medication code is required")
    String code,
    
    @Schema(description = "ID auto-incremental del ticket", example = "1")
    String id,

    @Schema(description = "El nombre del medicamento", example = "Paracetamol")
    String medicationName,

    @Schema(description = "Precio total pagado", example = "45000")
    Integer totalPrice,

    @Schema(description = "Fecha de compra (ISO)", example = "2026-08-24T12:00:00")
    String purchaseDate,

    @Schema(description = "Estado del medicamento", example = "OPEN")
    String status,

    @Schema(description = "Medicamentos disponible", example = "50")
    int availableMedication
) {
}
