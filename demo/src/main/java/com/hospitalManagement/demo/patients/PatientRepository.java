package com.hospitalManagement.demo.patients;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, UUID> {
    // Custom query methods can be added here if needed
    boolean existsByEmailHash(String emailHash);
    Optional<Patient> findByEmailHash(String emailhash);
}
