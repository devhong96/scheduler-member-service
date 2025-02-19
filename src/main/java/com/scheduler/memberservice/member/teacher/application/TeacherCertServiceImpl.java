package com.scheduler.memberservice.member.teacher.application;

import com.scheduler.memberservice.infra.email.application.AuthEmailService;
import com.scheduler.memberservice.infra.exception.custom.DuplicateEmailException;
import com.scheduler.memberservice.infra.exception.custom.DuplicateUsernameException;
import com.scheduler.memberservice.infra.exception.custom.MemberExistException;
import com.scheduler.memberservice.infra.exception.custom.PasswordMismatchException;
import com.scheduler.memberservice.infra.util.MemberUtils;
import com.scheduler.memberservice.member.teacher.domain.Teacher;
import com.scheduler.memberservice.member.teacher.repository.TeacherJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.scheduler.memberservice.infra.email.dto.FindInfoRequest.FindPasswordRequest;
import static com.scheduler.memberservice.infra.email.dto.FindInfoRequest.FindUsernameRequest;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoRequest.*;

@Service
@RequiredArgsConstructor
public class TeacherCertServiceImpl implements TeacherCertService {

    private final TeacherJpaRepository teacherJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthEmailService authEmailService;
    private final MemberUtils memberUtils;

    @Override
    @Transactional
    public void joinTeacher(JoinTeacherRequest joinTeacherRequest) {

        boolean existsByUsername = teacherJpaRepository.existsByUsername(joinTeacherRequest.getUsername());

        if (existsByUsername) {
            throw new DuplicateUsernameException();
        }

        boolean existsByEmail = teacherJpaRepository.existsByEmail(joinTeacherRequest.getEmail());

        if (existsByEmail) {
            throw new DuplicateEmailException();
        }

        Teacher teacher = Teacher.create(joinTeacherRequest, passwordEncoder);
        teacherJpaRepository.save(teacher);
    }

    @Override
    public void findUsernameByEmail(FindUsernameRequest findUsernameRequest) {

        Teacher teacher = teacherJpaRepository
                .findByEmailIs(findUsernameRequest.getEmail())
                .orElseThrow(MemberExistException::new);

        authEmailService.sendUsername(teacher.getUsername(), findUsernameRequest.getEmail());
    }

    @Override
    @Transactional
    public void sendPasswordResetEmail(FindPasswordRequest findPasswordRequest) {

        String username = findPasswordRequest.getUsername();
        String email = findPasswordRequest.getEmail();

        Boolean exist = teacherJpaRepository
                .existsTeacherByUsernameAndEmail(username, email);

        if(!exist) {
            throw new MemberExistException();
        }

        authEmailService.sendAuthNum(email, username);
    }

    @Override
    @Transactional
    public void initializePassword(PwdEditRequest pwdEditRequest) {

        String username = memberUtils.getTeacher().getUsername();

        if(!Objects.equals(
                pwdEditRequest.getCheckPassword(),
                pwdEditRequest.getNewPassword()))

            throw new PasswordMismatchException();

        Teacher teacher = teacherJpaRepository
                .findByUsernameIs(username)
                .orElseThrow(MemberExistException::new);

        teacher.updatePassword(passwordEncoder, pwdEditRequest);
    }

    @Override
    @Transactional
    public void changeUserEmail(EditEmailRequest editEmailRequest) {

        String teacherId = memberUtils.getTeacherId();

        Teacher teacher = teacherJpaRepository
                .findByTeacherId(teacherId)
                .orElseThrow(MemberExistException::new);

        teacher.updateEmail(editEmailRequest);
    }
}
