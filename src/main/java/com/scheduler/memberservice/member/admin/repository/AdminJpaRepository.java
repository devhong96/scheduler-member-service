package com.scheduler.memberservice.member.admin.repository;

import com.scheduler.memberservice.member.admin.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminJpaRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findAdminByAdminId(String adminId);
    Optional<Admin> findAdminByEmail(String email);
    Boolean existsAdminByUsername(String username);
    Optional<Admin> findByUsernameIs(String username);
    boolean existsByEmail(String email);
}
