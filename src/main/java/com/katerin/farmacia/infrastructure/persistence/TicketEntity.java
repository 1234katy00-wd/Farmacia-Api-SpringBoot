package com.katerin.farmacia.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table( name = "tickets")
public class TicketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medication_id", nullable = false)
    private MedicationEntity medication;

    private String customerEmail;
    private String medicationName;
    private Integer totalPrice;
    private String purchaseDate;
    private String code;

    public TicketEntity(){}

    public TicketEntity(Integer id, String code, MedicationEntity medication, String customerEmail, String medicationName,
            Integer totalPrice, String purchaseDate) {
        this.id = id;
        this.code = code;
        this.medication = medication;
        this.customerEmail = customerEmail;
        this.medicationName = medicationName;
        this.totalPrice = totalPrice;
        this.purchaseDate = purchaseDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    

    public MedicationEntity getMedication() {
        return medication;
    }

    public void setMedication(MedicationEntity medication) {
        this.medication = medication;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    


    

}
