package com.scheduler.memberservice.member.teacher.application;

import com.scheduler.memberservice.infra.email.dto.HtmlEmailService;
import com.scheduler.memberservice.infra.exception.custom.MemberExistException;
import com.scheduler.memberservice.infra.util.MemberUtils;
import com.scheduler.memberservice.member.teacher.domain.Teacher;
import com.scheduler.memberservice.member.teacher.repository.TeacherJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.scheduler.memberservice.infra.email.dto.FindInfoRequest.*;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoRequest.EditEmailRequest;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoRequest.PwdEditRequest;

@Service
@RequiredArgsConstructor
public class TeacherCertServiceImpl implements TeacherCertService {

    private final TeacherJpaRepository teacherJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final HtmlEmailService htmlEmailService;
    private final MemberUtils memberUtils;

    @Override
    public void findUsernameByEmail(FindIdRequest findIdRequest) {

        Teacher teacher = teacherJpaRepository
                .findByEmailIs(findIdRequest.getEmail())
                .orElseThrow(MemberExistException::new);

        htmlEmailService.sendUsername(teacher.getUsername(), findIdRequest.getEmail());
    }

    @Override
    @Transactional
    public void initializePassword(PwdEditRequest pwdEditRequest) {

        Teacher teacher = teacherJpaRepository
                .findByUsernameIs(pwdEditRequest.getUsername())
                .orElseThrow(MemberExistException::new);

        teacher.updatePassword(passwordEncoder, pwdEditRequest);
    }

    @Override
    public void sendPasswordResetEmail(FindPasswordRequest findPasswordRequest) {

        String username = findPasswordRequest.getUsername();
        String email = findPasswordRequest.getEmail();

        Boolean exist = teacherJpaRepository
                .existsTeacherByUsernameAndEmail(username, email);

        if(!exist) {
            throw new MemberExistException();
        }

        htmlEmailService.sendAuthNum(email, username);
    }

    @Override
    public void verifyAuthCode(AuthCodeRequest authCodeRequest) {

    }

    @Override
    public void changeUserEmail(EditEmailRequest editEmailRequest) {

        String teacherId = memberUtils.getTeacherId();

        Teacher teacher = teacherJpaRepository
                .findByTeacherId(teacherId)
                .orElseThrow(MemberExistException::new);

        teacher.updateEmail(editEmailRequest);
    }
}
