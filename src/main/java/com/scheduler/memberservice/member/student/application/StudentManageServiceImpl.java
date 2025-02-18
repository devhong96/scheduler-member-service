package com.scheduler.memberservice.member.student.application;

import com.scheduler.memberservice.infra.exception.custom.MemberExistException;
import com.scheduler.memberservice.member.student.domain.Student;
import com.scheduler.memberservice.member.student.repository.StudentJpaRepository;
import com.scheduler.memberservice.member.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.scheduler.memberservice.member.student.dto.StudentResponse.StudentInfoResponse;

@Service
@RequiredArgsConstructor
public class StudentManageServiceImpl implements StudentManageService {

    private final StudentRepository studentRepository;
    private final StudentJpaRepository studentJpaRepository;


    @Override
    @Transactional(readOnly = true)
    public Page<StudentInfoResponse> findStudentInfoList(
            String teacherName, String studentName, Pageable pageable
    ) {
        return studentRepository.studentInformationList(teacherName, studentName, pageable);
    }

    @Override
    @Transactional
    public void changeStudentStatus(String studentId) {

        Student student = studentJpaRepository
                .findStudentByStudentId(studentId)
                .orElseThrow(MemberExistException::new);

        Boolean approved = student.getApproved();

        student.updateApproved(!approved);
    }

}
