package com.hospitalManagement.demo.doctors;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DoctorNotFoundException extends RuntimeException {
    public DoctorNotFoundException(UUID id){
        super("Doctor not found" + id);

    }

}
