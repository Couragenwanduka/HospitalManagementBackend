package com.hospitalManagement.demo.doctors;

import org.springframework.stereotype.Service;
import com.hospitalManagement.demo.utils.EncryptService;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Base64;
import java.util.UUID;

@Service
public class DoctorService {

    private final DoctorRepository repository;
    private final EncryptService encryptService;

    public DoctorService(DoctorRepository repository, EncryptService encryptService) {
        this.repository = repository;
        this.encryptService = encryptService;
    }

    public Page<Doctor> getAllDoctors(Pageable pageable) {
        Page<Doctor> doctors = repository.findAll(pageable);
       doctors.forEach(this::decryptSensitiveFields);
        return doctors;
    }

    public Doctor getDoctorById(UUID id) {
        Doctor doctor =  repository.findById(id).orElseThrow(() -> new DoctorNotFoundException(id));
        decryptSensitiveFields(doctor);
        return doctor;
    }
    

    public Doctor createDoctor(DoctorDTO doctorDTO) {
        String emailHash;
        String licenseHash;
        try {
            emailHash = encryptService.hash(doctorDTO.getEmail());
            licenseHash = encryptService.hash(doctorDTO.getLicenseNumber());
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash doctor data", e);
        }

        if (repository.existsByEmailHash(emailHash)) {
            throw new IllegalArgumentException("A doctor with this email already exists");
        }
        if (repository.existsByLicenseNumberHash(licenseHash)) {
            throw new IllegalArgumentException("A doctor with this license number already exists");
        }
       

        Doctor doctor = mapDtoToEntity(doctorDTO);
        try {
            encryptSensitiveFields(doctor);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt doctor data", e);
        }

        return repository.save(doctor);
    }

   private Doctor mapDtoToEntity(DoctorDTO doctorDTO) {
    if(doctorDTO.getPassword() == null || doctorDTO.getPassword().isBlank()){
        throw new IllegalArgumentException("Password is required");
    }
        Doctor doctor = new Doctor();
        doctor.setFirstName(doctorDTO.getFirstName());
        doctor.setLastName(doctorDTO.getLastName());
        doctor.setEmail(doctorDTO.getEmail());
        doctor.setPhoneNumber(doctorDTO.getPhoneNumber());
        doctor.setAddress(doctorDTO.getAddress());
        doctor.setGender(doctorDTO.getGender());
        doctor.setDateOfBirth(doctorDTO.getDateOfBirth());
        doctor.setSpecialization(doctorDTO.getSpecialization());
        doctor.setLicenseNumber(doctorDTO.getLicenseNumber());
        doctor.setDepartment(doctorDTO.getDepartment());
        doctor.setGraduationYear(doctorDTO.getGraduationYear().getYear());
        doctor.setQualifications(doctorDTO.getQualifications());
        doctor.setJoiningDate(doctorDTO.getJoiningDate());
        doctor.setStartTime(doctorDTO.getStartTime());
        doctor.setEndTime(doctorDTO.getEndTime());
        doctor.setProfilePictureUrl(doctorDTO.getProfilePictureUrl());
        doctor.setIsAvailable(doctorDTO.getIsAvailable());
         BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        doctor.setPassword(encoder.encode(doctorDTO.getPassword()));
        return doctor;
    }

    public Doctor updateDoctor(UUID id, DoctorDTO doctorDTO) {
        String newEmailHash;
        try {
            newEmailHash = encryptService.hash(doctorDTO.getEmail());
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash doctor email", e);
        }
        Doctor existingDoctor = repository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));
        try {
            decryptSensitiveFields(existingDoctor);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt doctor data", e);
        }
        if (!existingDoctor.getEmailHash().equals(newEmailHash) &&
        repository.existsByEmailHash(newEmailHash)) {
    throw new IllegalArgumentException("A doctor with this email already exists");
}
        existingDoctor.setFirstName(doctorDTO.getFirstName());
        existingDoctor.setLastName(doctorDTO.getLastName());
        existingDoctor.setEmail(doctorDTO.getEmail());
        existingDoctor.setPhoneNumber(doctorDTO.getPhoneNumber());
        existingDoctor.setAddress(doctorDTO.getAddress());
        existingDoctor.setGender(doctorDTO.getGender());
        existingDoctor.setDateOfBirth(doctorDTO.getDateOfBirth());
        existingDoctor.setSpecialization(doctorDTO.getSpecialization());
        existingDoctor.setLicenseNumber(doctorDTO.getLicenseNumber());
        existingDoctor.setDepartment(doctorDTO.getDepartment());
        existingDoctor.setGraduationYear(doctorDTO.getGraduationYear().getYear());
        existingDoctor.setQualifications(doctorDTO.getQualifications());
        existingDoctor.setJoiningDate(doctorDTO.getJoiningDate());
        existingDoctor.setStartTime(doctorDTO.getStartTime());
        existingDoctor.setEndTime(doctorDTO.getEndTime());
        existingDoctor.setProfilePictureUrl(doctorDTO.getProfilePictureUrl());
        existingDoctor.setIsAvailable(doctorDTO.getIsAvailable());

        // Only update password if a new one is provided
        if (doctorDTO.getPassword() != null && !doctorDTO.getPassword().isBlank()) {
             BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            existingDoctor.setPassword(encoder.encode(doctorDTO.getPassword()));
        }

        try {
            encryptSensitiveFields(existingDoctor);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt doctor data", e);
        }

        return repository.save(existingDoctor);
    }

    public void deleteDoctor(UUID id) {
        Doctor existingDoctor = repository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));
        repository.delete(existingDoctor);
    }

    private void encryptSensitiveFields(Doctor doctor) {
        try{
       byte[] ivBytes = encryptService.generateIV();
        String ivBase64 = Base64.getEncoder().encodeToString(ivBytes);
        doctor.setIv(ivBase64);

        doctor.setLicenseNumber(encryptService.encrypt(doctor.getLicenseNumber(), ivBytes));
        doctor.setAddress(encryptService.encrypt(doctor.getAddress(), ivBytes));
        doctor.setEmail(encryptService.encrypt(doctor.getEmail(), ivBytes));
        doctor.setPhoneNumber(encryptService.encrypt(doctor.getPhoneNumber(), ivBytes));
         if (doctor.getProfilePictureUrl() != null) {
                doctor.setProfilePictureUrl(encryptService.encrypt(doctor.getProfilePictureUrl(), ivBytes));
            }
        }catch(Exception e){
             throw new RuntimeException("Error encrypting doctors data", e);
        }
    }

    private void decryptSensitiveFields(Doctor doctor) {
         try{
             byte[] ivBytes = Base64.getDecoder().decode(doctor.getIv());

           doctor.setLicenseNumber(encryptService.decrypt(doctor.getLicenseNumber(), ivBytes));
        doctor.setAddress(encryptService.decrypt(doctor.getAddress(), ivBytes));
        doctor.setEmail(encryptService.decrypt(doctor.getEmail(), ivBytes));
        doctor.setPhoneNumber(encryptService.decrypt(doctor.getPhoneNumber(), ivBytes));
        if (doctor.getProfilePictureUrl() != null) {
                doctor.setProfilePictureUrl(encryptService.decrypt(doctor.getProfilePictureUrl(), ivBytes));
            }
         }catch(Exception e){
              throw new RuntimeException("Error decryptingdoctors data", e);
         }
    }


}
