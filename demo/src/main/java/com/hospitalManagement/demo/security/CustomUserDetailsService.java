package com.hospitalManagement.demo.security;

import com.hospitalManagement.demo.admin.AdminRepository;
import com.hospitalManagement.demo.doctors.DoctorRepository;
import com.hospitalManagement.demo.patients.PatientRepository;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AdminRepository adminRepository;

    public CustomUserDetailsService(DoctorRepository doctorRepository,
                                    PatientRepository patientRepository,
                                    AdminRepository adminRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Hash the email to search in DB
        String emailHash;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(email.getBytes(StandardCharsets.UTF_8));
            emailHash = Base64.getEncoder().encodeToString(hashBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash email", e);
        }

        // Check Admin table
        var admin = adminRepository.findByEmailHash(emailHash);
        if (admin.isPresent()) {
            return User.builder()
                    .username(email)
                    .password(admin.get().getPassword())
                    .roles("ADMIN")
                    .build();
        }

        // Check Doctor table
        var doctor = doctorRepository.findByEmailHash(emailHash);
        if (doctor.isPresent()) {
            return User.builder()
                    .username(email)
                    .password(doctor.get().getPassword())
                    .roles("DOCTOR")
                    .build();
        }

        // Check Patient table
        var patient = patientRepository.findByEmailHash(emailHash);
        if (patient.isPresent()) {
            return User.builder()
                    .username(email)
                    .password(patient.get().getPassword())
                    .roles("PATIENT")
                    .build();
        }

        throw new UsernameNotFoundException("User not found: " + email);
    }
}