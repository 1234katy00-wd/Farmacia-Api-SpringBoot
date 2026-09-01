package com.katerin.farmacia.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.katerin.farmacia.infrastructure.persistence.MedicationEntity;

public interface MedicationJpaRepository extends JpaRepository<MedicationEntity, Integer>{

}