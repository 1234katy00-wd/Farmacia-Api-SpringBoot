package com.katerin.farmacia.application.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.katerin.farmacia.domain.exception.ResourceNotFoundException;
import com.katerin.farmacia.domain.model.Medication;
import com.katerin.farmacia.domain.model.Ticket;
import com.katerin.farmacia.infrastructure.persistence.MedicationEntity;
import com.katerin.farmacia.infrastructure.persistence.TicketEntity;
import com.katerin.farmacia.infrastructure.persistence.repository.MedicationJpaRepository;
import com.katerin.farmacia.infrastructure.persistence.repository.TicketJpaRepository;

@Service
public class MedicationServiceImpl implements MedicationService {

    private final MedicationJpaRepository medicationJpaRepository;
    private final TicketJpaRepository ticketJpaRepository;

    public MedicationServiceImpl(
        MedicationJpaRepository medicationJpaRepository,
        TicketJpaRepository ticketJpaRepository){
        
        this.medicationJpaRepository = medicationJpaRepository;
        this.ticketJpaRepository = ticketJpaRepository;
        } 

    @Override
    @Transactional(readOnly = true)
    public List<Medication> getAllMedications() {
        return medicationJpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Medication getMedicationById(Integer id){
        MedicationEntity entity = medicationJpaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Medication with id" + id + "not found"));
        return toDomain(entity);
    }

    @Override
    @Transactional
    public Medication createMedication(Medication medication) {
        if (medication == null) {
            throw new IllegalArgumentException("Medication must not be null");
        }

        int totalTickets = medication.totalTickets() != null ? medication.totalTickets() : 0;
        int availableTickets = medication.availableTickets() != null ? medication.availableTickets() : totalTickets;
        String status = medication.status() != null ? medication.status() : "OPEN";

        MedicationEntity entity = new MedicationEntity(
            medication.id(),
            medication.code(),
            medication.creationDate(),
            medication.dueDate(),
            medication.medicationName(),
            status,
            medication.ticketPrice() != null ? medication.ticketPrice() : 0,
            medication.description(),
            medication.activeIngredient(),
            medication.ticketPrice(),
            totalTickets,
            availableTickets);
        MedicationEntity saved = medicationJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    @Transactional
    public Medication updateMedication(Integer id, Medication medication) {
        MedicationEntity entity = medicationJpaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Medication with id" + id + "not found"));

        if(medication.medicationName() != null) entity.setMedicationName(medication.medicationName());
        if(medication.code() !=null) entity.setCode(medication.code());
        if(medication.activeIngredient() !=null) entity.setActiveIngredient(medication.activeIngredient());
        if(medication.status() !=null) entity.setStatus(medication.status());
        if(medication.availableTickets() !=null) entity.setAvailableTickets(medication.availableTickets());
        if(medication.dueDate() != null) entity.setDueDate(medication.dueDate());
        if(medication.creationDate() !=null) entity.setCreationDate(medication.creationDate());
        if(medication.ticketPrice() !=null) entity.setTicketPrice(medication.ticketPrice());
        if(medication.totalTickets() !=null)  entity.setTotalTickets(medication.totalTickets());


        MedicationEntity upMedication= medicationJpaRepository
        .save(entity);
        return toDomain(upMedication);
    }

    @Override
    @Transactional
    public void deleteMedication(Integer id) {
        if (!medicationJpaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Medication with id '" + id + "' not found");
        }
        List<TicketEntity> tickets = ticketJpaRepository.findByMedicationId(id);
        ticketJpaRepository.deleteAll(tickets);
        medicationJpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<Ticket> purchaseTickets(
            Integer medicationId,
            String medicationName,
            String customerEmail,
            int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        MedicationEntity medication = medicationJpaRepository.findById(medicationId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Medication with id '" + medicationId + "' not found"));
        String name = medication.getMedicationName();
        if (name == null) {
            name = medicationName;
        }

        int ticketPrice = medication.getTicketPrice() == null
                ? 0
                : medication.getTicketPrice();

        List<TicketEntity> tickets = new ArrayList<>(quantity);
        for (int index = 0; index < quantity; index++) {
            TicketEntity ticket = new TicketEntity(
                    null,
                    medication.getCode(),
                    medication,
                    customerEmail,
                    name,
                    ticketPrice,
                    LocalDate.now().toString());
            medication.addTicket(ticket);
            tickets.add(ticket);
        }

        return ticketJpaRepository.saveAll(tickets)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> getMedicationTickets(Integer medicationId) {
        return ticketJpaRepository.findByMedicationId(medicationId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private Medication toDomain(MedicationEntity entity) {
        return new Medication(
                entity.getId(),
                entity.getCode(),
                entity.getMedicationName(),
                entity.getTicketPrice(),
                entity.getCreationDate(),
                entity.getDueDate(),
                entity.getStatus(),
                entity.getDescription(),
                entity.getActiveIngredient(),
                entity.getTotalTickets(),
                entity.getAvailableTickets());
    }

    private Ticket toDomain(TicketEntity entity) {
        return new Ticket(
                entity.getId(),
                entity.getCode(),
                entity.getMedication().getId(),
                entity.getMedicationName(),
                entity.getTotalPrice(),
                entity.getCustomerEmail(),
                entity.getPurchaseDate());
    }

    

}
