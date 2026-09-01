package com.katerin.farmacia.infrastructure.persistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "medications")
public class MedicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String code;
    private LocalDate creationDate;
    private LocalDate dueDate;
    private String medicationName;
    private String status;
    private int price;
    private String description;
    private String activeIngredient;
    private Integer ticketPrice;
    private Integer totalTickets;
    private Integer availableTickets;

    @OneToMany(mappedBy = "medication")
    private List<TicketEntity> tickets = new ArrayList<>();


    
    public MedicationEntity() {
    }



    public MedicationEntity(Integer id,String code, LocalDate creationDate, LocalDate dueDate, String medicationName, String status,
            int price, String description, String activeIngredient, Integer ticketPrice, Integer totalTickets, Integer availableTickets) {
        this.id = id;
        this.code= code;
        this.creationDate = creationDate;
        this.dueDate = dueDate;
        this.medicationName = medicationName;
        this.status = status;
        this.price = price;
        this.description = description;
        this.activeIngredient = activeIngredient;
        this.ticketPrice = ticketPrice;
        this.totalTickets= totalTickets;
        this.availableTickets= availableTickets;
    }

    public void addTicket(TicketEntity ticket) {
        if (ticket == null) {
            return;
        }
        tickets.add(ticket);
        ticket.setMedication(this);
    }

    public void removeTicket(TicketEntity ticket) {
        if (ticket == null) {
            return;
        }
        tickets.remove(ticket);
        ticket.setMedication(null);
    }

    public List<TicketEntity> getTickets() {
        return tickets;
    }

    public Integer getId() {
        return id;
    }



    public void setId(Integer id) {
        this.id = id;
    }



    public LocalDate getCreationDate() {
        return creationDate;
    }



    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }



    public LocalDate getDueDate() {
        return dueDate;
    }



    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }



    public String getMedicationName() {
        return medicationName;
    }



    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }



    public String getStatus() {
        return status;
    }



    public void setStatus(String status) {
        this.status = status;
    }



    public int getPrice() {
        return price;
    }



    public void setPrice(int price) {
        this.price = price;
    }



    public String getDescription() {
        return description;
    }



    public void setDescription(String description) {
        this.description = description;
    }



    public String getActiveIngredient() {
        return activeIngredient;
    }



    public void setActiveIngredient(String activeIngredient) {
        this.activeIngredient = activeIngredient;
    }



    public Integer getTicketPrice() {
        return ticketPrice;
    }



    public void setTicketPrice(Integer ticketPrice) {
        this.ticketPrice = ticketPrice;
    }



    public String getCode() {
        return code;
    }



    public void setCode(String code) {
        this.code = code;
    }



    public Integer getTotalTickets() {
        return totalTickets;
    }



    public void setTotalTickets(Integer totalTickets) {
        this.totalTickets = totalTickets;
    }



    public Integer getAvailableTickets() {
        return availableTickets;
    }



    public void setAvailableTickets(Integer availableTickets) {
        this.availableTickets = availableTickets;
    }
    
    

    

}
