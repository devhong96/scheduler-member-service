package com.scheduler.memberservice.member.student.application;

import com.scheduler.memberservice.infra.exception.custom.MemberExistException;
import com.scheduler.memberservice.member.student.domain.Student;
import com.scheduler.memberservice.member.student.repository.StudentJpaRepository;
import com.scheduler.memberservice.member.student.repository.StudentRepository;
import com.scheduler.memberservice.member.teacher.domain.Teacher;
import com.scheduler.memberservice.member.teacher.repository.TeacherJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.scheduler.memberservice.member.student.dto.StudentRequest.ModifyStudentRequest;
import static com.scheduler.memberservice.member.student.dto.StudentRequest.RegisterStudentRequest;
import static com.scheduler.memberservice.member.student.dto.StudentResponse.StudentInfoResponse;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final TeacherJpaRepository teacherJpaRepository;
    private final StudentJpaRepository studentJpaRepository;
    private final StudentRepository studentRepository;

    @Override
    @Transactional
    public void registerStudentInformation(RegisterStudentRequest registerStudentRequest) {

        boolean exists = studentJpaRepository
                .existsByStudentNameIs(registerStudentRequest.getStudentName());

        if (exists) {
            throw new MemberExistException();
        }

        Student student = Student.create(registerStudentRequest);

        Teacher teacher = teacherJpaRepository
                .findByUsernameIs(registerStudentRequest.getTeacherUsername())
                .orElseThrow(MemberExistException::new);

        student.setTeacher(teacher);
        studentJpaRepository.save(student);
    }

    @Override
    @Transactional
    public void modifyStudentVerification(ModifyStudentRequest registerStudentRequest) {

        String studentId = registerStudentRequest.getStudentId();
        Boolean isApproved = registerStudentRequest.getIsApproved();

        Student student = studentJpaRepository.findStudentByStudentId(studentId)
                .orElseThrow(MemberExistException::new);

        student.updateApproved(isApproved);
    }

    @Override
    @Transactional
    public Page<StudentInfoResponse> findStudentInfoList(
            String teacherName, String studentName, Pageable pageable
    ) {
        return studentRepository.studentInformationList(teacherName, studentName, pageable);
    }
}
