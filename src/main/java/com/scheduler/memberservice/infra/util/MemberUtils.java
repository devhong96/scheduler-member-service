package com.scheduler.memberservice.infra.util;

import com.scheduler.memberservice.infra.exception.custom.MemberLoginException;
import com.scheduler.memberservice.member.admin.domain.Admin;
import com.scheduler.memberservice.member.admin.repository.AdminJpaRepository;
import com.scheduler.memberservice.member.teacher.domain.Teacher;
import com.scheduler.memberservice.member.teacher.repository.TeacherJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberUtils {

    private final AdminJpaRepository adminJpaRepository;
    private final TeacherJpaRepository teacherJpaRepository;

    public String getAdminId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = Optional.ofNullable(authentication)
                .orElseThrow(() -> new MemberLoginException("작가 로그인 에러"))
                .getName();

        return adminJpaRepository.findByUsernameIs(username)
                .orElseThrow(() -> new MemberLoginException("Cannot find author with username: " + username))
                .getAdminId();
    }

    public Admin getAdmin() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = Optional.ofNullable(authentication)
                .orElseThrow(() -> new MemberLoginException("작가 로그인 에러"))
                .getName();

        return adminJpaRepository.findByUsernameIs(username)
                .orElseThrow(() -> new MemberLoginException("Cannot find author with username: " + username));
    }

    public String getTeacherId() {

        String username = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .orElseThrow(() -> new MemberLoginException("회원가입 후, 이용해 주세요."))
                .getName();

        Teacher reader = teacherJpaRepository
                .findByUsernameIs(username)
                .orElseThrow(() -> new MemberLoginException("회원가입 후, 이용해 주세요."));

        return reader.getTeacherId();
    }

    public Teacher getTeacher() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = Optional.ofNullable(authentication)
                .orElseThrow(() -> new MemberLoginException("회원가입 후, 이용해 주세요."))
                .getName();

        return teacherJpaRepository
                .findByUsernameIs(username)
                .orElseThrow(() -> new MemberLoginException("독자 로그인 에러"));

    }
}
