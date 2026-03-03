package com.hospitalManagement.demo.patients;


import java.util.Base64;
import java.util.UUID;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hospitalManagement.demo.utils.EncryptService;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



@Service
public class PatientService {
    private final PatientRepository repository;
    private  final EncryptService encryptService;
   

    public PatientService(PatientRepository repository, EncryptService encryptService) throws Exception {
        this.repository = repository;
        this.encryptService = encryptService;
    }
    
    public Page<Patient> getAllPatients(Pageable pageable) {
        Page<Patient> patients = repository.findAll(pageable);
        patients.forEach(this::decryptSensitiveFields);
        return patients;
    }

    public  Patient getPatientById(UUID id){
        Patient patient = repository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
        decryptSensitiveFields(patient);
        return patient;
    }

    public Patient createPatient(PatientDTO patientDTO) {
        Patient patient = mapDtoEntity(patientDTO);

        String emailHash = hashEmail(patient.getEmail());
        patient.setEmailHash(emailHash);
        encryptSensitiveFields(patient);
        return repository.save(patient);
    }

    public String hashEmail(String email) {
    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(email.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashBytes); // store as string
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException("Error hashing email", e);
    }
}

    public Patient updatePatient(UUID id, Patient updatedPatient) throws Exception {
        Patient existingPatient = repository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));

        existingPatient.setFirstName(updatedPatient.getFirstName());
        existingPatient.setLastName(updatedPatient.getLastName());

        existingPatient.setEmail(updatedPatient.getEmail());
        existingPatient.setPhoneNumber(updatedPatient.getPhoneNumber());
        existingPatient.setAddress(updatedPatient.getAddress());
        existingPatient.setDateOfBirth(updatedPatient.getDateOfBirth());
        existingPatient.setGender(updatedPatient.getGender());
        existingPatient.setEmergencyContactName(updatedPatient.getEmergencyContactName());
        existingPatient.setEmergencyContactPhone(updatedPatient.getEmergencyContactPhone());
        existingPatient.setEmergencyContactRelationship(updatedPatient.getEmergencyContactRelationship());

        encryptSensitiveFields(existingPatient);

        return repository.save(existingPatient);
    }


    public void deletePatient(UUID id) {
        repository.deleteById(id);
    }

    public void  deleteAllPatients() {
        repository.deleteAll();
    }
    
    private  Patient mapDtoEntity(PatientDTO dto) {
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
        throw new IllegalArgumentException("Password is required");
    }
        Patient patient = new Patient();
        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setEmail(dto.getEmail());
        patient.setPhoneNumber(dto.getPhoneNumber());
        patient.setAddress(dto.getAddress());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setGender(dto.getGender());
        patient.setEmergencyContactName(dto.getEmergencyContactName());
        patient.setEmergencyContactPhone(dto.getEmergencyContactPhone());
        patient.setEmergencyContactRelationship(dto.getEmergencyContactRelationship());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        patient.setPassword(encoder.encode(dto.getPassword()));
        return patient;
    }

     private void encryptSensitiveFields(Patient patient) {
    try {
        byte[] ivBytes = encryptService.generateIV();
        String ivBase64 = Base64.getEncoder().encodeToString(ivBytes);
        patient.setIv(ivBase64);

        patient.setEmail(encryptService.encrypt(patient.getEmail(), ivBytes));
        patient.setPhoneNumber(encryptService.encrypt(patient.getPhoneNumber(), ivBytes));
        patient.setAddress(encryptService.encrypt(patient.getAddress(), ivBytes));
        patient.setEmergencyContactName(encryptService.encrypt(patient.getEmergencyContactName(), ivBytes));
        patient.setEmergencyContactPhone(encryptService.encrypt(patient.getEmergencyContactPhone(), ivBytes));
        patient.setEmergencyContactRelationship(
                encryptService.encrypt(patient.getEmergencyContactRelationship(), ivBytes));

    } catch (Exception e) {
        throw new RuntimeException("Error encrypting patient data", e);
    }
}

 private void decryptSensitiveFields(Patient patient) {
    try {
        byte[] ivBytes = Base64.getDecoder().decode(patient.getIv());

        patient.setEmail(encryptService.decrypt(patient.getEmail(), ivBytes));
        patient.setPhoneNumber(encryptService.decrypt(patient.getPhoneNumber(), ivBytes));
        patient.setAddress(encryptService.decrypt(patient.getAddress(), ivBytes));
        patient.setEmergencyContactName(encryptService.decrypt(patient.getEmergencyContactName(), ivBytes));
        patient.setEmergencyContactPhone(encryptService.decrypt(patient.getEmergencyContactPhone(), ivBytes));
        patient.setEmergencyContactRelationship(
                encryptService.decrypt(patient.getEmergencyContactRelationship(), ivBytes));

    } catch (Exception e) {
        throw new RuntimeException("Error decrypting patient data", e);
    }
}

}
