package com.scheduler.memberservice.member.student.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static com.scheduler.memberservice.member.student.dto.StudentRequest.*;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class Address {

    @Column(nullable = false)
    private String studentPhoneNumber;

    @Column(nullable = false)
    private String studentParentPhoneNumber;

    @Column(nullable = false)
    private String studentAddress;

    @Column(nullable = false)
    private String studentDetailedAddress;

    public Address(String studentPhoneNumber, String studentParentPhoneNumber, String studentAddress, String studentDetailedAddress) {
        this.studentPhoneNumber = studentPhoneNumber;
        this.studentParentPhoneNumber = studentParentPhoneNumber;
        this.studentAddress = studentAddress;
        this.studentDetailedAddress = studentDetailedAddress;
    }

    protected static Address create(RegisterStudentRequest request) {
        Address address = new Address();
        address.studentPhoneNumber = request.getStudentPhoneNumber();
        address.studentParentPhoneNumber = request.getStudentParentPhoneNumber();
        address.studentAddress = request.getStudentAddress();
        address.studentDetailedAddress = request.getStudentDetailedAddress();
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(studentPhoneNumber, address.studentPhoneNumber) && Objects.equals(studentParentPhoneNumber, address.studentParentPhoneNumber) && Objects.equals(studentAddress, address.studentAddress) && Objects.equals(studentDetailedAddress, address.studentDetailedAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentPhoneNumber, studentParentPhoneNumber, studentAddress, studentDetailedAddress);
    }
}
