package com.hospitalManagement.demo.doctors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import com.hospitalManagement.demo.utils.EmailSender;


@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
    private final DoctorService service ;

    @Autowired
    private final EmailSender emailSender;

    public DoctorController(DoctorService service, EmailSender emailSender){
        this.service = service;
        this.emailSender = emailSender;
    }

    @GetMapping
    public ResponseEntity<Page<Doctor>>getAllDoctor(Pageable pageable){
       Page<Doctor> doctors = service.getAllDoctors(pageable);
       return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable UUID id){
        Doctor doctor = service.getDoctorById(id);
        return ResponseEntity.ok(doctor);
    }

   @PostMapping
    public ResponseEntity<Doctor> createDoctor(@RequestBody @Valid DoctorDTO doctorDTO) {
        String plainEmail = doctorDTO.getEmail();
        String plainPassword = doctorDTO.getPassword();

        Doctor createdDoctor = service.createDoctor(doctorDTO);

        String message = "Dear Dr. " + createdDoctor.getFirstName() + " " + createdDoctor.getLastName() + ",\n\n"
                + "Welcome to the Hospital Management System. We are pleased to inform you that your "
                + "account has been successfully created. Below are your login credentials:\n\n"
                + "------------------------------------------------------------\n"
                + "  Email Address : " + plainEmail + "\n"
                + "  Password : " + plainPassword + "\n"
                + "------------------------------------------------------------\n\n"
                + "For security purposes, you are strongly advised to change your password "
                + "immediately upon your first login. Please do not share your credentials "
                + "with anyone.\n\n"
                + "If you did not request this account or believe this email was sent in error, "
                + "please contact our IT support team immediately.\n\n"
                + "Should you have any questions or require assistance, do not hesitate to reach out "
                + "to our support team.\n\n"
                + "Warm regards,\n"
                + "Hospital Management System\n"
                + "IT Support & Administration\n"
                + "support@hospital.com\n"
                + "Tel: +1 (800) 123-4567";

        emailSender.sendEmail(plainEmail, "Welcome to the Hospital Management System — Account Created", message);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdDoctor);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable UUID id, @RequestBody DoctorDTO doctor){
        try{
            Doctor updatedDoctor = service.updateDoctor(id, doctor);
            return  ResponseEntity.ok(updatedDoctor);

        }catch(Exception e){
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping()
    public ResponseEntity<Doctor> deleteDoctor(@PathVariable UUID id){
        service.deleteDoctor(id);
        return ResponseEntity.noContent().build(); 
    }


}
