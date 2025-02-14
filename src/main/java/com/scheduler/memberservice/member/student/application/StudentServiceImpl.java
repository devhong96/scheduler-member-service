package com.scheduler.memberservice.member.student.application;

import com.scheduler.memberservice.infra.exception.custom.MemberExistException;
import com.scheduler.memberservice.infra.util.MemberUtils;
import com.scheduler.memberservice.member.student.domain.Student;
import com.scheduler.memberservice.member.student.repository.StudentJpaRepository;
import com.scheduler.memberservice.member.student.repository.StudentRepository;
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

    private final MemberUtils memberUtils;
    private final StudentJpaRepository studentJpaRepository;
    private final StudentRepository studentRepository;

    @Override
    @Transactional
    public void registerStudentInformation(RegisterStudentRequest registerStudentRequest) {

        Student student = Student.create(registerStudentRequest);

        String teacherId = memberUtils.getTeacherId();
        student.assignTeacher(teacherId);

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
    @Transactional(readOnly = true)
    public Page<StudentInfoResponse> findStudentInfoList(
            String teacherName, String studentName, Pageable pageable
    ) {

        return studentRepository.studentInformationList(teacherName, studentName, pageable);
    }
}
