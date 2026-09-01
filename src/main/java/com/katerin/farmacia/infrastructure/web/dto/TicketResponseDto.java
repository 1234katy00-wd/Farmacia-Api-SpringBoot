package com.katerin.farmacia.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de un ticket emitido")
public record TicketResponseDto(
    @Schema(description = "ID auto-incremental del ticket", example = "001")
    Integer id,

    
    @Schema(description = "Código único del ticket", example = "TCK-4A8F9C12")
    String code,

    @Schema(description = "Código único del concierto", example = "001")
    Integer medicationId,

    @Schema(description = "El nombre del medicamento", example = "Paracetamol")
    String medicationName,

    @Schema(description = "Correo del cliente", example = "alice@example.com")
    String customerEmail,

    @Schema(description = "Precio total pagado", example = "45000")
    Integer totalPrice,

    @Schema(description = "Fecha de compra (ISO)", example = "2026-08-24T12:00:00")
    String purchaseDate

) {
} 