package com.scheduler.memberservice.client.service;

import com.scheduler.memberservice.infra.security.jwt.component.JwtUtils;
import com.scheduler.memberservice.member.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.scheduler.memberservice.client.dto.FeignMemberResponse.StudentResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeignOrderServiceImpl implements FeignOrderService {

    private final JwtUtils jwtUtils;
    private final StudentRepository studentRepository;

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getStudentInfo(String accessToken) {

        accessToken = accessToken.replace("Bearer ", "").trim();
        String username = jwtUtils.getAuthentication(accessToken).getName();

        return studentRepository.getStudentInfo(username);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse findStudentByUsername(String username) {
        return studentRepository.getStudentInfo(username);
    }
}
