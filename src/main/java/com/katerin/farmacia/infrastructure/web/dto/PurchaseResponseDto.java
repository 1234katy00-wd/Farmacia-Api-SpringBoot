package com.katerin.farmacia.infrastructure.web.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de compra exitosa de medicamentos.")
public record PurchaseResponseDto(

    @Schema(description = "Mensaje descriptivo")
    String message,

    @Schema(description = "Código del medicamento", example = "MED-001")
    Integer medicationId,

    @Schema(description = "Nombre del medicamento", example = "Paracetamol")
    String medicationName,

    @Schema(description = "Total pagado", example = "950")
    Integer price,

    @Schema(description = "Cantidad de medicamentos compradas", example = "2")
    int purchasedCount,

    @Schema(description = "Lista de tickets generados")
    List<TicketResponseDto> tickets


) {
} 
