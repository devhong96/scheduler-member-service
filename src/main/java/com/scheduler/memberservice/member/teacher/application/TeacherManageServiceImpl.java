package com.scheduler.memberservice.member.teacher.application;

import com.scheduler.memberservice.client.CourseServiceClient;
import com.scheduler.memberservice.infra.exception.custom.DuplicateCourseException;
import com.scheduler.memberservice.infra.exception.custom.MemberExistException;
import com.scheduler.memberservice.member.student.domain.Student;
import com.scheduler.memberservice.member.student.repository.StudentJpaRepository;
import com.scheduler.memberservice.member.teacher.domain.Teacher;
import com.scheduler.memberservice.member.teacher.repository.TeacherJpaRepository;
import com.scheduler.memberservice.member.teacher.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoRequest.ChangeTeacherRequest;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoResponse.TeacherResponse;

@Service
@RequiredArgsConstructor
public class TeacherManageServiceImpl implements TeacherManageService {

    private final TeacherRepository teacherRepository;
    private final StudentJpaRepository studentJpaRepository;
    private final TeacherJpaRepository teacherJpaRepository;

    private final CourseServiceClient courseServiceClient;

    @Override
    public List<TeacherResponse> getTeacherList() {
        return teacherRepository.getTeacherList();
    }

    @Override
    public TeacherResponse findTeacherInformation(String username) {
        return teacherRepository.getTeacherInfoByUsername(username);
    }

    @Override
    @Transactional
    public void changeTeacherStatus(String teacherId) {
        Teacher teacher = teacherJpaRepository
                .findByTeacherId(teacherId)
                .orElseThrow(MemberExistException::new);

        Boolean result = courseServiceClient
                .existWeeklyCoursesByTeacherId(teacher.getTeacherId());

        if (!result) {
            throw new IllegalStateException("학생 수업 시간이 남아 있습니다.");
        }

        Boolean approved = teacher.getApproved();

        teacher.updateApprove(!approved);
    }

    @Override
    @Transactional
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
        Boolean result = courseServiceClient.validateStudentCoursesAndReassign(
                actualTeacherId,
                student.getStudentId()
        );

        if (result) {
            student.assignTeacher(actualTeacherId);
        } else {
            throw new DuplicateCourseException();
        }
    }
}
