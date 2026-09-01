package com.katerin.farmacia.infrastructure.web.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.katerin.farmacia.application.service.MedicationService;
import com.katerin.farmacia.domain.model.Medication;
import com.katerin.farmacia.domain.model.Ticket;
import com.katerin.farmacia.infrastructure.web.dto.MedicationResponseDto;
import com.katerin.farmacia.infrastructure.web.dto.MedicationRequestDto;
import com.katerin.farmacia.infrastructure.web.dto.PurchaseRequestDto;
import com.katerin.farmacia.infrastructure.web.dto.TicketResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/medications")
@Tag(name = "Medication", description = "Operaciones relacionadas con medicamentos y ventas")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController (MedicationService medicationService){
        this.medicationService = medicationService;
    }

    @Operation(summary = "Obtener todos los medicamentos", description = "Devuelve una lista de todos los medicamentos registrados ")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de medicamento obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public List<MedicationResponseDto> getAllMedication() {
        return medicationService.getAllMedications()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @Operation(summary = "Obtener medicamento por id", description = "Devuelve un medicamento por su Id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = " medicamento obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "medicamento no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        
    })
    @GetMapping("/{id}")
    public ResponseEntity<MedicationResponseDto> getMedicationById(@PathVariable(name ="id")Integer id){
        Medication medication = medicationService.getMedicationById(id);
        return ResponseEntity.ok(toResponse(medication));
    }

    @Operation(summary = "Crear medicamento", description = "Crea un nuevo medicamento")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Medicamento creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Medicamento no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<MedicationResponseDto> createMedication (@Valid @RequestBody MedicationRequestDto request) {
        Medication medicationToCreate = new Medication(
            null,
            request.code(),
            request.medicationName(),
            request.totalPrice(),
            null,
            null,
            request.status() != null ? request.status() : "OPEN",
            null,
            null,
            null,
            request.availableMedication()
        );
        Medication created = medicationService.createMedication(medicationToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @Operation(summary = "Actualizar medicamento", description = "actualizar un medicamento existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Medicamento actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Medicamento no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MedicationResponseDto> updateMedication(@PathVariable(name= "id")Integer id, @Valid @RequestBody MedicationRequestDto request){
        Medication medicationUpdate = new Medication(
            id,
            request.code(),
            request.medicationName(),
            request.totalPrice(),
            null,
            null,
            request.status(),
            null,
            null,
            null,
            request.availableMedication()
        );
        Medication update = medicationService.updateMedication(id, medicationUpdate);
        return ResponseEntity.ok(toResponse(update));
    }

    @Operation(summary = "Eliminar medicamento", description = "Eliminar un medicamento existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Medicamento eliminar exitosamente"),
        @ApiResponse(responseCode = "404", description = "Medicamento no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable(name="id")Integer id){
        medicationService.deleteMedication(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "boleta del medicamento", description = "Realiza la compra de medicamento con validación de estado y stock disponible")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "medicamento comprados exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o medicamento agotado"),
        @ApiResponse(responseCode = "404", description = "Medicamento no encontrado"),
        @ApiResponse(responseCode = "422", description = "Stock de medicamento insuficiente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping ("/{id}/purchase")
    public ResponseEntity<List<TicketResponseDto>> purchaseTickets(@PathVariable(name="id")Integer id, @Valid @RequestBody PurchaseRequestDto request){
        List<Ticket> purchased = medicationService.purchaseTickets(
            id, 
            request.medicationName(),
            request.customerEmail(),
            request.quantity()
        );
        List<TicketResponseDto> ticketDtos = purchased.stream()
            .map(this::toTicketResponse)
            .toList();
        return ResponseEntity.ok(ticketDtos);
        
    }
    
    @Operation(summary = "Obtener tickets emitidos de un medicamento", description = "Devuelve la lista de todos los tickets emitidos para el medicamento")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tickets obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "MEdicamento no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}/tickets")
    public ResponseEntity<List<TicketResponseDto>> getMedicationTickets(@PathVariable(name = "id")Integer id){
        List<TicketResponseDto> tickets = medicationService.getMedicationTickets(id).stream()
                .map(this::toTicketResponse)
                .toList();
        return ResponseEntity.ok(tickets);
    }

    private MedicationResponseDto toResponse(Medication medication) {
        return new MedicationResponseDto(
                medication.id(),
                medication.code(),
                medication.id(),
                medication.medicationName(),
                null,
                medication.ticketPrice(),
                null,
                medication.status(),
                medication.availableTickets() != null
                        ? medication.availableTickets()
                        : 0
        );
    }

    private TicketResponseDto toTicketResponse(Ticket ticket) {
        return new TicketResponseDto(
                ticket.id(),
            null,
            ticket.medicationId(),
            ticket.medicationName(),
                ticket.customerEmail(),
            ticket.totalPrice(),
                ticket.purchaseDate()
        );
        }
}
