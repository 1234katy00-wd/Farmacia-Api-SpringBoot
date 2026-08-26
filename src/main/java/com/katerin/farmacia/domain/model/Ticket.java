package com.katerin.farmacia.domain.model;

public record Ticket(
    String id,
    String code,
    String medicationId,
    String medicationName,
    Integer totalPrice,
    String customerEmail,
    String purchaseDate

) {
} 