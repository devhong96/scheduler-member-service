package com.scheduler.memberservice.member.student.domain;

import com.scheduler.memberservice.infra.BaseEntity;
import com.scheduler.memberservice.member.common.RoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static com.scheduler.memberservice.member.common.RoleType.STUDENT;
import static com.scheduler.memberservice.member.student.dto.StudentRequest.*;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.lang.Boolean.FALSE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = PROTECTED)
public class Student extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String studentId;

    @Column(nullable = false)
    private String teacherId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String studentName;

    public void updateStudentName(String studentName) {
        this.studentName = studentName;
    }

    @Column(nullable = false)
    private String email;

    @Embedded
    private Address address;

    @Enumerated(STRING)
    private RoleType roleType;

    private Boolean approved;

    public static Student create(
            RegisterStudentRequest registerStudentRequest,
            String teacherId,
            PasswordEncoder passwordEncoder
    ) {
        Student student = new Student();
        student.teacherId = teacherId;
        student.studentId = registerStudentRequest.getStudentId();
        student.username = registerStudentRequest.getUsername();
        student.password = passwordEncoder.encode(registerStudentRequest.getPassword());
        student.studentName = registerStudentRequest.getStudentName();
        student.email = registerStudentRequest.getStudentEmail();
        student.address = Address.create(registerStudentRequest);
        student.roleType = STUDENT;
        student.approved = FALSE;
        return student;
    }

    public void modifyStudentInfo(
            ModifyStudentInfoRequest modifyStudentRequest
    ) {
        this.email = modifyStudentRequest.getStudentEmail();
        this.address = new Address(
                modifyStudentRequest.getStudentPhoneNumber(),
                modifyStudentRequest.getStudentParentPhoneNumber(),
                modifyStudentRequest.getStudentAddress(),
                modifyStudentRequest.getStudentDetailedAddress()
        );
    }

    public void changePassword(
            ModifyStudentPasswordRequest modifyStudentPasswordRequest,
            PasswordEncoder passwordEncoder
    ) {
        this.password = passwordEncoder.encode(modifyStudentPasswordRequest.getNewPassword());
    }

    public void assignTeacher(String teacherId) {
        this.teacherId = teacherId;
    }

    @PrePersist
    public void generateUuid() {
        if (this.studentId == null) {
            this.studentId = UUID.randomUUID().toString();
        }
    }

    public void updateApproved(Boolean approved) {
        this.approved = approved;
    }
}
