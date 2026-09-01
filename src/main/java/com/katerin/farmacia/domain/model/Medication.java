package com.katerin.farmacia.domain.model;

import java.time.LocalDate;

public record Medication (
    Integer id,
    String code,
    String medicationName,
    Integer ticketPrice,
    LocalDate creationDate,
    LocalDate dueDate,
    String status,
    String description,
    String activeIngredient,
    Integer totalTickets,
    Integer availableTickets

){

    
}