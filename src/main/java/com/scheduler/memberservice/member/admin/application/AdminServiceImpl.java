package com.scheduler.memberservice.member.admin.application;

import com.scheduler.memberservice.client.CourseServiceClient;
import com.scheduler.memberservice.infra.email.dto.AuthEmailService;
import com.scheduler.memberservice.infra.exception.custom.MemberExistException;
import com.scheduler.memberservice.infra.util.MemberUtils;
import com.scheduler.memberservice.member.admin.domain.Admin;
import com.scheduler.memberservice.member.admin.repository.AdminJpaRepository;
import com.scheduler.memberservice.member.student.domain.Student;
import com.scheduler.memberservice.member.student.repository.StudentJpaRepository;
import com.scheduler.memberservice.member.teacher.domain.Teacher;
import com.scheduler.memberservice.member.teacher.repository.TeacherJpaRepository;
import com.scheduler.memberservice.member.teacher.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.scheduler.memberservice.client.dto.FeignCourseResponse.StudentCourseResponse;
import static com.scheduler.memberservice.member.admin.dto.AdminInfoRequest.ChangeTeacherRequest;
import static com.scheduler.memberservice.member.admin.dto.AdminInfoRequest.EmailRequest;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoResponse.TeacherResponse;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final MemberUtils memberUtils;

    private final TeacherRepository teacherRepository;
    private final AdminJpaRepository adminJpaRepository;
    private final StudentJpaRepository studentJpaRepository;
    private final TeacherJpaRepository teacherJpaRepository;

    private final AuthEmailService authEmailService;
    private final CourseServiceClient courseServiceClient;

    @Override
    public List<TeacherResponse> getTeacherList() {
        return teacherRepository.getTeacherList();
    }

    @Override
    public List<TeacherResponse> findTeacherInformation(String username) {
        return teacherRepository.getTeacherInfoByUsername(username);
    }

    @Override
    public void findAdminUsernameByEmail(EmailRequest emailRequest) {

        Admin admin = adminJpaRepository
                .findAdminByEmail(emailRequest.getEmail())
                .orElseThrow(MemberExistException::new);

        String email = admin.getEmail();
        String username = admin.getUsername();

        authEmailService.sendUsername(email, username);
    }

    @Override
    @Transactional
    public void grantAuth(String teacherId) {

        Teacher teacher = teacherJpaRepository
                .findByUsernameIs(teacherId)
                .orElseThrow(MemberExistException::new);

        teacher.updateApprove(true);
    }

    @Override
    @Transactional
    public void revokeAuth(String username) {

        Teacher teacher = teacherJpaRepository
                .findByUsernameIs(username)
                .orElseThrow(MemberExistException::new);

        teacher.updateApprove(false);
    }

    @Override
    @Transactional
    public void changeExistTeacher(ChangeTeacherRequest changeTeacherRequest) {

        String teacherId = changeTeacherRequest.getTeacherId();
        String studentId = changeTeacherRequest.getStudentId();

        Teacher teacher = teacherJpaRepository
                .findTeacherByTeacherId(teacherId)
                .orElseThrow(MemberExistException::new);

        Student student = studentJpaRepository
                .findStudentByStudentId(teacherId)
                .orElseThrow(MemberExistException::new);

        //학생의 수업엔티티와 교사의 수업을 비교
        List<StudentCourseResponse> weekCourseByTeacherId = courseServiceClient.getWeeklyCoursesByTeacherId(teacherId);
        StudentCourseResponse weeklyCoursesByStudentId = courseServiceClient.getWeeklyCoursesByStudentId(studentId);

        classValidator(weekCourseByTeacherId, weeklyCoursesByStudentId);

        student.assignTeacher(teacher.getTeacherId());

        courseServiceClient.reassignStudentCourses(teacherId, studentId);
    }

    private void classValidator(
            List<StudentCourseResponse> weekCourseByTeacherId,
            StudentCourseResponse weeklyCoursesByStudentId
    ) {

        for (StudentCourseResponse studentCourseResponse : weekCourseByTeacherId) {

            Integer mondayValue = studentCourseResponse.getMondayClass();
            Integer tuesdayValue = studentCourseResponse.getTuesdayClass();
            Integer wednesdayValue = studentCourseResponse.getWednesdayClass();
            Integer thursdayValue = studentCourseResponse.getThursdayClass();
            Integer fridayValue = studentCourseResponse.getFridayClass();

            if (mondayValue.equals(weeklyCoursesByStudentId.getMondayClass()))
                throw new IllegalArgumentException("학생의 월요일 수업 중에 겹치는 날이 있습니다.");

            if (tuesdayValue.equals(weeklyCoursesByStudentId.getTuesdayClass()))
                throw new IllegalArgumentException("학생의 화요일 수업 중에 겹치는 날이 있습니다.");

            if (wednesdayValue.equals(weeklyCoursesByStudentId.getWednesdayClass()))
                throw new IllegalArgumentException("학생의 수요일 수업 중에 겹치는 날이 있습니다.");

            if (thursdayValue.equals(weeklyCoursesByStudentId.getThursdayClass()))
                throw new IllegalArgumentException("학생의 목요일 수업 중에 겹치는 날이 있습니다.");

            if (fridayValue.equals(weeklyCoursesByStudentId.getFridayClass()))
                throw new IllegalArgumentException("학생의 금요일 수업 중에 겹치는 날이 있습니다.");
        }
    }

    @Override
    @Transactional
    public void deleteTeacherAccount(String teacherId) {

        Teacher teacher = teacherJpaRepository
                .findByUsernameIs(teacherId)
                .orElseThrow(MemberExistException::new);

        List<StudentCourseResponse> studentClassByTeacherName
                = courseServiceClient.getWeeklyCoursesByTeacherId(teacher.getTeacherId());

        if(!studentClassByTeacherName.isEmpty())
            throw new IllegalStateException("학생 수업 시간이 남아 있습니다.");

        teacherJpaRepository.deleteByUsernameIs(teacherId);
    }

    @Override
    public String findEmail() {
        Admin admin = memberUtils.getAdmin();
        return admin.getEmail();
    }
}