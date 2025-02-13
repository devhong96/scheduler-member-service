package com.scheduler.memberservice.infra.config;

import com.scheduler.memberservice.member.admin.domain.Admin;
import com.scheduler.memberservice.member.admin.repository.AdminJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminConfig implements ApplicationRunner {

    private final PasswordEncoder passwordEncoder;
    private final AdminJpaRepository adminJpaRepository;

    @Override
    public void run(ApplicationArguments args) {

        final String admin = "admin";
        final String password = "password";
        final String email = "email";

        if (!adminJpaRepository.existsAdminByUsername(admin)) {
            adminJpaRepository.save(Admin.create(admin, password, email, passwordEncoder));
        }
    }
}
