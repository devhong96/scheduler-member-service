package com.scheduler.memberservice.member.teacher.domain;

import com.scheduler.memberservice.infra.BaseEntity;
import com.scheduler.memberservice.member.common.RoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static com.scheduler.memberservice.member.common.RoleType.TEACHER;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoRequest.*;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = PROTECTED)
public class Teacher extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String teacherId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String teacherName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Enumerated(STRING)
    private RoleType roleType;

    @Column(nullable = false)
    private Boolean approved;

    public void updatePassword(PasswordEncoder passwordEncoder, PwdEditRequest pwdEditRequest) {
        this.password = passwordEncoder.encode(pwdEditRequest.getNewPassword());
    }

    public void updateEmail(EditEmailRequest editEmailRequest) {
        this.email = editEmailRequest.getEmail();
    }

    public void updateTeacherName(String newTeacherName) {
        this.teacherName = newTeacherName;
    }

    public void updateApprove(boolean approved) {
        this.approved = approved;
    }

    public static Teacher create(JoinTeacherRequest joinTeacherRequest, PasswordEncoder passwordEncoder) {
        Teacher teacher = new Teacher();
        teacher.teacherId = joinTeacherRequest.getTeacherId();
        teacher.username = joinTeacherRequest.getUsername();
        teacher.password = passwordEncoder.encode(joinTeacherRequest.getPassword());
        teacher.teacherName = joinTeacherRequest.getTeacherName();
        teacher.email = joinTeacherRequest.getEmail();
        teacher.roleType = TEACHER;
        teacher.approved = false;
        return teacher;
    }

    @PrePersist
    public void generateUuid() {
        if (this.teacherId == null) {
            this.teacherId = UUID.randomUUID().toString();
        }
    }
}