package com.hospitalManagement.demo.admin;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, UUID> {
  boolean existsByEmailHash(String emailHash);
  Optional<Admin > findByEmailHash(String emailHash);
}
