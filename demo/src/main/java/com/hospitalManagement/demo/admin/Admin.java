package com.hospitalManagement.demo.admin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name= "Admin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    @Id
    @GeneratedValue
    @Column(columnDefinition= "uuid", updatable= false, nullable=false)
    private UUID id;

    @Column(nullable= false)
    private String firstName;

    @Column(nullable= false)
    private String lastName;

    @Column(nullable= false)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String iv;

    @JsonIgnore
    @Column(nullable = false, unique = true)
    private String emailHash;

    @JsonIgnore
    @Column(nullable= false)
    private String password;

     @Column(nullable = false)
    private String phoneNumber;

    @Column
    private String profilePictureUrl;


}
