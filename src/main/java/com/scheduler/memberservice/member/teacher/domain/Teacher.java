package com.scheduler.memberservice.member.teacher.domain;

import com.scheduler.memberservice.member.common.RoleType;
import com.scheduler.memberservice.member.student.domain.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
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
public class Teacher {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String teacherId;

    private String username;

    private String teacherName;

    private String password;

    private String email;

    @Enumerated(STRING)
    private RoleType roleType;

    private Boolean approved;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Student> studentList = new ArrayList<>();

    public void updatePassword(PasswordEncoder passwordEncoder, PwdEditRequest pwdEditRequest) {
        this.password = passwordEncoder.encode(pwdEditRequest.getPassword());
    }

    public void updateEmail(EditEmailRequest editEmailRequest) {
        this.email = editEmailRequest.getEmail();
    }

    @PrePersist
    public void generateUuid() {
        if (this.teacherId == null) {
            this.teacherId = UUID.randomUUID().toString();
        }
    }

    public void updateApprove(boolean approved) {
        this.approved = approved;
    }

    public static Teacher create(JoinTeacherRequest joinTeacherRequest, PasswordEncoder passwordEncoder) {
        Teacher teacher = new Teacher();
        teacher.username = joinTeacherRequest.getUsername();
        teacher.password = passwordEncoder.encode(joinTeacherRequest.getPassword());
        teacher.teacherName = joinTeacherRequest.getTeacherName();
        teacher.email = joinTeacherRequest.getEmail();
        teacher.roleType = TEACHER;
        teacher.approved = false;
        return teacher;
    }
}