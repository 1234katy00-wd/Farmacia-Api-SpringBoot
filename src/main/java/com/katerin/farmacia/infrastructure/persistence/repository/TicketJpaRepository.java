package com.katerin.farmacia.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.katerin.farmacia.infrastructure.persistence.TicketEntity;

@Repository
public interface TicketJpaRepository extends JpaRepository<TicketEntity, String>{
    List<TicketEntity> findByMedicationId(String medicationId);
    
}
