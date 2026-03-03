package com.hospitalManagement.demo.admin;

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
import java.util.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
public class Admincontoller {
    private final  AdminService adminService;

    public Admincontoller(AdminService adminService){
        this.adminService = adminService;
    }

    @PostMapping
    public ResponseEntity<AdminDTO> createAdmin(@RequestBody @Valid AdminDTO adminDTO) {
        AdminDTO createdAdmin = adminService.createAdmin(adminDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAdmin);
    }


    @GetMapping("/{id}")
    public ResponseEntity<AdminDTO> getAdminById(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.getAdminById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminDTO> updateAdmin(
            @PathVariable UUID id,
            @RequestBody @Valid AdminDTO adminDTO) {
        return ResponseEntity.ok(adminService.updateAdmin(id, adminDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable UUID id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }


    
    

}
