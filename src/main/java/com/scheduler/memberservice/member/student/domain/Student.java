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

import static com.scheduler.memberservice.member.student.dto.StudentRequest.*;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.lang.Boolean.*;
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

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String studentPhoneNumber;

    @Column(nullable = false)
    private String studentParentPhoneNumber;

    @Column(nullable = false)
    private String studentAddress;

    @Column(nullable = false)
    private String studentDetailedAddress;

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
        student.username = registerStudentRequest.getUsername();
        student.password = passwordEncoder.encode(registerStudentRequest.getPassword());
        student.studentName = registerStudentRequest.getStudentName();
        student.email = registerStudentRequest.getStudentEmail();
        student.studentPhoneNumber = registerStudentRequest.getStudentPhoneNumber();
        student.studentParentPhoneNumber = registerStudentRequest.getStudentParentPhoneNumber();
        student.studentAddress = registerStudentRequest.getStudentAddress();
        student.studentDetailedAddress = registerStudentRequest.getStudentDetailedAddress();
        student.approved = TRUE;
        return student;
    }

    public void modifyStudentInfo(
            ModifyStudentInfoRequest modifyStudentRequest
    ) {
        this.email = modifyStudentRequest.getStudentEmail();
        this.studentPhoneNumber = modifyStudentRequest.getStudentPhoneNumber();
        this.studentParentPhoneNumber = modifyStudentRequest.getStudentParentPhoneNumber();
        this.studentAddress = modifyStudentRequest.getStudentAddress();
        this.studentDetailedAddress = modifyStudentRequest.getStudentDetailedAddress();
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
