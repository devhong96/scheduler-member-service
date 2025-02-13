package com.scheduler.memberservice.member.admin.application;

import com.scheduler.memberservice.infra.exception.custom.MemberExistException;
import com.scheduler.memberservice.infra.util.MemberUtils;
import com.scheduler.memberservice.member.admin.domain.Admin;
import com.scheduler.memberservice.member.admin.repository.AdminJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.scheduler.memberservice.infra.email.dto.FindInfoRequest.FindPasswordRequest;
import static com.scheduler.memberservice.member.admin.dto.AdminInfoRequest.EditEmailRequest;
import static com.scheduler.memberservice.member.admin.dto.AdminInfoRequest.PwdEditRequest;

@Service
@RequiredArgsConstructor
public class AdminCertServiceImpl implements AdminCertService {

    private final MemberUtils memberUtils;
    private final PasswordEncoder passwordEncoder;
    private final AdminJpaRepository adminJpaRepository;

    @Override
    public boolean emailConfirmation(FindPasswordRequest findPasswordDTO) {
        return adminJpaRepository.existsByEmail(findPasswordDTO.getEmail());
    }

    @Override
    @Transactional
    public void initializePassword(PwdEditRequest PwdEditRequest) {

        String adminId = memberUtils.getAdminId();
        String password = PwdEditRequest.getPassword();

        Admin admin = adminJpaRepository
                .findAdminByAdminId(adminId)
                .orElseThrow(MemberExistException::new);

        admin.updatePassword(passwordEncoder, password);
    }

    @Override
    @Transactional
    public void updateEmail(EditEmailRequest editEmailRequest) {

        String adminId = memberUtils.getAdminId();

        Admin admin = adminJpaRepository
                .findByUsernameIs(adminId)
                .orElseThrow(MemberExistException::new);

        admin.updateEmail(editEmailRequest);
    }
}
