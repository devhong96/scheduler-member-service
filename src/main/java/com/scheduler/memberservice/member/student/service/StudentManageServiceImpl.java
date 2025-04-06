package com.scheduler.memberservice.member.student.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.scheduler.memberservice.client.CourseServiceClient;
import com.scheduler.memberservice.infra.exception.custom.DuplicateCourseException;
import com.scheduler.memberservice.infra.exception.custom.MemberExistException;
import com.scheduler.memberservice.member.messaging.outbox.OutBoxEventJpaRepository;
import com.scheduler.memberservice.member.messaging.outbox.OutboxEvent;
import com.scheduler.memberservice.member.student.domain.Student;
import com.scheduler.memberservice.member.student.repository.StudentJpaRepository;
import com.scheduler.memberservice.member.student.repository.StudentRepository;
import com.scheduler.memberservice.member.teacher.domain.Teacher;
import com.scheduler.memberservice.member.teacher.repository.TeacherJpaRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.scheduler.memberservice.client.dto.FeignMemberRequest.CourseReassignmentResponse;
import static com.scheduler.memberservice.infra.config.messaging.RabbitConfig.EXCHANGE_NAME;
import static com.scheduler.memberservice.infra.config.messaging.RabbitConfig.ROUTING_KEY;
import static com.scheduler.memberservice.member.student.dto.StudentRequest.ChangeStudentName;
import static com.scheduler.memberservice.member.student.dto.StudentRequest.ChangeTeacherRequest;
import static com.scheduler.memberservice.member.student.dto.StudentResponse.StudentInfoResponse;
import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentManageServiceImpl implements StudentManageService {

    private final static String redisKey = "student : ";

    private final StudentRepository studentRepository;
    private final StudentJpaRepository studentJpaRepository;
    private final TeacherJpaRepository teacherJpaRepository;

    private final OutBoxEventJpaRepository outBoxEventJpaRepository;
    private final CourseServiceClient courseServiceClient;
    private final RedissonClient redissonClient;
    private final RabbitTemplate rabbitTemplate;

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
        CourseReassignmentResponse courseReassignmentResponse = courseServiceClient
                .validateStudentCoursesAndReassign(
                        actualTeacherId,
                        student.getStudentId()
                );

        if (courseReassignmentResponse.getExists()) {
            student.assignTeacher(actualTeacherId);
        } else {
            throw new DuplicateCourseException();
        }
    }

    protected void fallback(ChangeTeacherRequest ChangeTeacherRequest, Throwable e) {
        log.warn("Exception: {}", e.getMessage());
        throw new MemberExistException();
    }

    @Override
    @Transactional
    public void changeStudentName(ChangeStudentName changeStudentName) {

        RLock lock = redissonClient
                .getLock(redisKey + changeStudentName.getStudentId());

        try {
            boolean available = lock.tryLock(10, 1, SECONDS);

            if (available) {
                try {
                    Student student = studentJpaRepository
                            .findStudentByStudentId(changeStudentName.getStudentId())
                            .orElseThrow(() -> new RuntimeException("Student not found"));

                    student.updateStudentName(changeStudentName.getStudentName());

                    OutboxEvent save = outBoxEventJpaRepository.save(OutboxEvent.create(changeStudentName));
                    processOutboxEventAsync(save);

                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            } else {
                throw new RuntimeException("Timeout: Unable to acquire lock");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    //일단 이벤트 발행시. 시도 아웃 박스에서는 실패 로직만
    @Async
    public void processOutboxEventAsync(OutboxEvent event) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, event.getPayload());
            event.updateProcessed(true);
            outBoxEventJpaRepository.save(event);
        } catch (Exception e) {
            log.error("Failed to send to RabbitMQ: {}", event.getId(), e);
        }
    }
}
