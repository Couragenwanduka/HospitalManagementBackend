package com.hospitalManagement.demo.admin;

public class AdminMapper {
public static AdminDTO toDTO(Admin admin) {
        AdminDTO dto = new AdminDTO();
        dto.setId(admin.getId());
        dto.setFirstName(admin.getFirstName());
        dto.setLastName(admin.getLastName());
        dto.setEmail(admin.getEmail());
        dto.setPhoneNumber(admin.getPhoneNumber());
        dto.setProfilePictureUrl(admin.getProfilePictureUrl());
        // password, iv, emailHash intentionally excluded
        return dto;
    }
}
