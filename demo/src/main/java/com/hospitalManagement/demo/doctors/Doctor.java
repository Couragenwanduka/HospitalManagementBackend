package com.hospitalManagement.demo.doctors;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Period;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalTime;


@Entity
@Table(name="Doctor")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false)
    private String licenseNumber;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private Integer graduationYear;

    @Column(nullable = false)
    private String qualifications;

    @Column(nullable = false)
    private LocalDate joiningDate;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)   
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String gender;
    
    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column
    private String profilePictureUrl;

    @Column
    private Boolean isAvailable;

    @Column(unique = true)
    private String emailHash;

    @Column(unique = true)
    private String licenseNumberHash;

    @JsonIgnore
    @Column(nullable = false)
    private String iv;
 
    public int getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public int getYearsOfExperience() {
        return LocalDate.now().getYear() - graduationYear;
    }

    public boolean isCurrentlyAvailable() {
        return isAvailable != null && isAvailable;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    

  

}
