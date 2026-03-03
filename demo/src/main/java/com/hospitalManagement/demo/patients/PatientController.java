package com.hospitalManagement.demo.patients;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.hospitalManagement.demo.utils.EmailSender;


@RestController
@RequestMapping("/api/patients")
public class PatientController {
    private final PatientService service;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private PatientRepository repository;

    public PatientController(PatientService service) {
        this.service = service;
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<Void> deleteAllPatients() {
        service.deleteAllPatients();
        return ResponseEntity.noContent().build();  
    }

    @GetMapping
    public ResponseEntity<Page<Patient>> getAllPatients(Pageable pageable) {
       Page<Patient> patients = service.getAllPatients(pageable);
       return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable UUID id) {
        Patient patient = service.getPatientById(id);
        return ResponseEntity.ok(patient);
    }

    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody PatientDTO patientDTO) {
        String plainPassword = patientDTO.getPassword();
        String plainEmail = patientDTO.getEmail();
        String emailHash = service.hashEmail(plainEmail);

      
         if (repository.existsByEmailHash(emailHash)) {
        throw new IllegalArgumentException("Email already exists");
    }
      Patient createdPatient = service.createPatient(patientDTO);

        // Create a friendly message including their password
        String message = "Dear " + createdPatient.getFirstName() + " " + createdPatient.getLastName() + ",\n\n"
                + "Welcome to our hospital! Your account has been created successfully.\n"
                + "Here are your login details:\n"
                + "Email: " + plainEmail+ "\n"
                + "Password: " + plainPassword + "\n\n"
                + "Please keep this information safe.\n"
                + "Best regards,\n"
                + "Hospital Team";

        emailSender.sendEmail(plainEmail, "Welcome to Our Hospital", message);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable UUID id, @RequestBody Patient patient) {
        try {
            Patient updatedPatient = service.updatePatient(id, patient);
            return ResponseEntity.ok(updatedPatient);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        service.deletePatient(id);
        return ResponseEntity.noContent().build();  
    }
}
