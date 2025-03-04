package com.scheduler.memberservice.member.student.application;

import com.scheduler.memberservice.client.CourseServiceClient;
import com.scheduler.memberservice.infra.exception.custom.DuplicateCourseException;
import com.scheduler.memberservice.infra.exception.custom.MemberExistException;
import com.scheduler.memberservice.member.student.domain.Student;
import com.scheduler.memberservice.member.student.repository.StudentJpaRepository;
import com.scheduler.memberservice.member.student.repository.StudentRepository;
import com.scheduler.memberservice.member.teacher.domain.Teacher;
import com.scheduler.memberservice.member.teacher.repository.TeacherJpaRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.scheduler.memberservice.client.dto.FeignMemberRequest.CourseReassignmentResponse;
import static com.scheduler.memberservice.infra.config.messaging.RabbitConfig.EXCHANGE_NAME;
import static com.scheduler.memberservice.infra.config.messaging.RabbitConfig.ROUTING_KEY;
import static com.scheduler.memberservice.member.student.dto.StudentRequest.ChangeStudentName;
import static com.scheduler.memberservice.member.student.dto.StudentRequest.ChangeTeacherRequest;
import static com.scheduler.memberservice.member.student.dto.StudentResponse.StudentInfoResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentManageServiceImpl implements StudentManageService {

    private final StudentRepository studentRepository;
    private final StudentJpaRepository studentJpaRepository;
    private final RabbitTemplate rabbitTemplate;
    private final TeacherJpaRepository teacherJpaRepository;
    private final CourseServiceClient courseServiceClient;

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

    @Override
    @Transactional
    @CircuitBreaker(name = "studentService", fallbackMethod = "fallback")
    public void changeExistTeacher(ChangeTeacherRequest changeTeacherRequest) {

        String teacherId = changeTeacherRequest.getTeacherId();
        String studentId = changeTeacherRequest.getStudentId();

        Teacher teacher = teacherJpaRepository
                .findTeacherByTeacherId(teacherId)
                .orElseThrow(MemberExistException::new);

        String actualTeacherId = teacher.getTeacherId();

        Student student = studentJpaRepository
                .findStudentByStudentId(studentId)
                .orElseThrow(MemberExistException::new);

        //학생의 수업엔티티와 교사의 수업을 비교 후, 재할당
        CourseReassignmentResponse courseReassignmentResponse = courseServiceClient.validateStudentCoursesAndReassign(
                actualTeacherId,
                student.getStudentId()
        );

        if (courseReassignmentResponse.getExists()) {
            student.assignTeacher(actualTeacherId);
        } else {
            throw new DuplicateCourseException();
        }
    }

    protected void fallback(String username, Throwable e) {
        log.warn("Fallback triggered for username: {}, Exception: {}", username, e.getMessage());
        throw new MemberExistException();
    }

    @Override
    @Transactional
    public void changeStudentName(
            ChangeStudentName changeStudentName
    ) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, changeStudentName);
    }
}
