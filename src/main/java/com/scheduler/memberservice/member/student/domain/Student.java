package com.scheduler.memberservice.member.student.domain;

import com.scheduler.memberservice.infra.BaseEntity;
import com.scheduler.memberservice.member.common.RoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.UUID;

import static com.scheduler.memberservice.member.student.dto.StudentRequest.RegisterStudentRequest;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
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

    private String studentName;

    private String password;

    private String studentPhoneNumber;

    private String studentParentPhoneNumber;

    private String studentAddress;

    private String studentDetailedAddress;

    @Enumerated(STRING)
    private RoleType roleType;

    private Boolean approved;

    public static Student create(RegisterStudentRequest registerStudentRequest) {
        Student student = new Student();
        student.studentName = registerStudentRequest.getStudentName();
        student.password = registerStudentRequest.getPassword();
        student.studentPhoneNumber = registerStudentRequest.getStudentPhoneNumber();
        student.studentParentPhoneNumber = registerStudentRequest.getStudentParentPhoneNumber();
        student.studentAddress = registerStudentRequest.getStudentAddress();
        student.studentDetailedAddress = registerStudentRequest.getStudentDetailedAddress();
        student.approved = true;
        return student;
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
