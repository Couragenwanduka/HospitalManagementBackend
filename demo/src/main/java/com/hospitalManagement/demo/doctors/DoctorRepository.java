package com.hospitalManagement.demo.doctors;


import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
   boolean existsByEmailHash(String emailHash);
   boolean existsByLicenseNumberHash(String licenseNumberHash);  
   Optional<Doctor> findByEmailHash(String emailhash);
}
