package com.hospitalManagement.demo.admin;

import java.util.Base64;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hospitalManagement.demo.utils.EncryptService;

@Service
public class AdminService {

    private final AdminRepository repository;
    private final EncryptService encryptService;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository repository, EncryptService encryptService, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.encryptService = encryptService;
        this.passwordEncoder = passwordEncoder;
    }

    public AdminDTO createAdmin(AdminDTO adminDTO) {
        if (adminDTO.getPassword() == null || adminDTO.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        // Hash email for duplicate check
        String emailHash;
        try {
            emailHash = encryptService.hash(adminDTO.getEmail());
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash admin data", e);
        }

        if (repository.existsByEmailHash(emailHash)) {
            throw new IllegalArgumentException("An admin with this email already exists");
        }

        Admin admin = mapDtoToEntity(adminDTO);
        admin.setEmailHash(emailHash);
        admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
        encryptSensitiveFields(admin);

        Admin saved = repository.save(admin);
        decryptSensitiveFields(saved);
        return AdminMapper.toDTO(saved);
    }

    public AdminDTO getAdminById(UUID id) {
        Admin admin = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        decryptSensitiveFields(admin);
        return AdminMapper.toDTO(admin);
    }

    public AdminDTO updateAdmin(UUID id, AdminDTO adminDTO) {
        Admin existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        decryptSensitiveFields(existing);

        // Check email uniqueness only if it changed
        if (!existing.getEmail().equals(adminDTO.getEmail())) {
            try {
                String newEmailHash = encryptService.hash(adminDTO.getEmail());
                if (repository.existsByEmailHash(newEmailHash)) {
                    throw new IllegalArgumentException("An admin with this email already exists");
                }
                existing.setEmailHash(newEmailHash);
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException("Failed to hash admin data", e);
            }
        }

        existing.setFirstName(adminDTO.getFirstName());
        existing.setLastName(adminDTO.getLastName());
        existing.setEmail(adminDTO.getEmail());
        existing.setPhoneNumber(adminDTO.getPhoneNumber());
        existing.setProfilePictureUrl(adminDTO.getProfilePictureUrl());

        if (adminDTO.getPassword() != null && !adminDTO.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
        }

        encryptSensitiveFields(existing);
        Admin saved = repository.save(existing);
        decryptSensitiveFields(saved);
        return AdminMapper.toDTO(saved);
    }

    public void deleteAdmin(UUID id) {
        Admin admin = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        repository.delete(admin);
    }

    private Admin mapDtoToEntity(AdminDTO adminDTO) {
        Admin admin = new Admin();
        admin.setFirstName(adminDTO.getFirstName());
        admin.setLastName(adminDTO.getLastName());
        admin.setEmail(adminDTO.getEmail());
        admin.setPhoneNumber(adminDTO.getPhoneNumber());
        admin.setProfilePictureUrl(adminDTO.getProfilePictureUrl());
        return admin;
    }

    private void encryptSensitiveFields(Admin admin) {
        try {
            byte[] ivBytes = encryptService.generateIV();
            admin.setIv(Base64.getEncoder().encodeToString(ivBytes));
            admin.setEmail(encryptService.encrypt(admin.getEmail(), ivBytes));
            admin.setPhoneNumber(encryptService.encrypt(admin.getPhoneNumber(), ivBytes));
            if (admin.getProfilePictureUrl() != null) {
                admin.setProfilePictureUrl(encryptService.encrypt(admin.getProfilePictureUrl(), ivBytes));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting admin data", e);
        }
    }

    private void decryptSensitiveFields(Admin admin) {
        try {
            if (admin.getIv() == null || admin.getIv().isBlank()) return;
            byte[] ivBytes = Base64.getDecoder().decode(admin.getIv());
            admin.setEmail(encryptService.decrypt(admin.getEmail(), ivBytes));
            admin.setPhoneNumber(encryptService.decrypt(admin.getPhoneNumber(), ivBytes));
            if (admin.getProfilePictureUrl() != null) {
                admin.setProfilePictureUrl(encryptService.decrypt(admin.getProfilePictureUrl(), ivBytes));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting admin data", e);
        }
    }
}