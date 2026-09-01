package com.katerin.farmacia.domain.model;

public record Ticket(
    Integer id,
    String code,
    Integer medicationId,
    String medicationName,
    Integer totalPrice,
    String customerEmail,
    String purchaseDate

) {
} 