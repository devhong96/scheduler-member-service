package com.scheduler.memberservice.member.teacher.application;

import com.scheduler.memberservice.client.CourseServiceClient;
import com.scheduler.memberservice.infra.exception.custom.MemberExistException;
import com.scheduler.memberservice.member.teacher.domain.Teacher;
import com.scheduler.memberservice.member.teacher.repository.TeacherJpaRepository;
import com.scheduler.memberservice.member.teacher.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.scheduler.memberservice.client.dto.FeignMemberRequest.CourseExistenceResponse;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoResponse.TeacherResponse;

@Service
@RequiredArgsConstructor
public class TeacherManageServiceImpl implements TeacherManageService {

    private final TeacherRepository teacherRepository;
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
    public void changeTeacherStatus(String username) {

        Teacher teacher = teacherJpaRepository
                .findTeacherByUsernameIs(username)
                .orElseThrow(MemberExistException::new);

        CourseExistenceResponse result = courseServiceClient
                .existWeeklyCoursesByTeacherId(teacher.getTeacherId());

        if (result.getExists()) {
            throw new IllegalStateException("학생 수업 시간이 남아 있습니다.");
        }

        Boolean approved = teacher.getApproved();
        teacher.updateApprove(!approved);
    }
}
