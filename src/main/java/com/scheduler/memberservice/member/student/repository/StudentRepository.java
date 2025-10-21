package com.scheduler.memberservice.member.student.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.scheduler.memberservice.client.dto.FeignMemberResponse.StudentResponse;
import static com.scheduler.memberservice.member.student.domain.QStudent.student;
import static com.scheduler.memberservice.member.student.dto.StudentResponse.StudentInfoResponse;
import static com.scheduler.memberservice.member.teacher.domain.QTeacher.teacher;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class StudentRepository {

    private final JPAQueryFactory queryFactory;

    public Page<StudentInfoResponse> studentInformationList(
            String teacherName, String studentName, Pageable pageable
    ) {
        List<StudentInfoResponse> contents =
                queryFactory
                        .select(Projections.fields(StudentInfoResponse.class,
                                student.studentId,
                                student.studentName,
                                student.studentAddress,
                                student.studentDetailedAddress,
                                student.studentPhoneNumber,
                                student.studentParentPhoneNumber,
                                teacher.teacherName,
                                student.createdAt))
                        .from(student)
                        .join(teacher)
                        .on(student.teacherId.eq(teacher.teacherId))
                        .where(
                                studentNameEq(studentName),
                                teacherNameEq(teacherName)
                        )
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        JPAQuery<Long> counts = queryFactory
                .select(student.count())
                .from(student)
                .where(
                        studentNameEq(studentName),
                        teacherNameEq(teacherName)
                );

        return PageableExecutionUtils.getPage(contents, pageable, counts::fetchOne);
    }

    public StudentResponse getStudentInfo(String username) {

        return queryFactory.select(Projections.fields(StudentResponse.class,
                        student.studentId,
                        student.username))
                .from(student)
                .where(studentUsernameEq(username))
                .fetchOne();
    }

    private BooleanExpression studentUsernameEq(String username) {
        return hasText(username) ? student.username.eq(username) : null;
    }

    private BooleanExpression studentNameEq(String studentName) {
        return hasText(studentName) ? student.studentName.eq(studentName) : null;
    }

    private BooleanExpression teacherNameEq(String teacherName) {
        return hasText(teacherName) ? teacher.teacherName.eq(teacherName) : null;
    }
}
