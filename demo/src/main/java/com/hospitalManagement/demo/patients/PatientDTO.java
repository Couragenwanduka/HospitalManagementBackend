package com.hospitalManagement.demo.patients;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientDTO {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;
    
    @Email
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    private Patient.Gender gender;

    @NotBlank(message = "Emergency contact name is required")
    private String emergencyContactName;    

    @NotBlank(message = "Emergency contact phone is required")
    private String emergencyContactPhone;

    @NotBlank(message = "Emergency contact relationship is required")
    private String emergencyContactRelationship;

}
