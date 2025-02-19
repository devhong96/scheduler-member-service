package com.scheduler.memberservice.member.teacher.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.types.dsl.Expressions.constant;
import static com.scheduler.memberservice.member.common.RoleType.TEACHER;
import static com.scheduler.memberservice.member.teacher.domain.QTeacher.teacher;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoResponse.TeacherResponse;

@Repository
@RequiredArgsConstructor
public class TeacherRepository {

    public final JPAQueryFactory queryFactory;

    public List<TeacherResponse> getTeacherList(){
        return queryFactory
                .select(Projections.fields(TeacherResponse.class,
                        teacher.id,
                        teacher.username,
                        teacher.teacherName,
                        teacher.email,
                        ExpressionUtils.as(
                                constant(TEACHER.getDescription()), "role"
                        ),
                        teacher.approved))
                .from(teacher)
                .fetch();
    }

    public TeacherResponse getTeacherInfoByUsername(String username){
        return queryFactory
                .select(Projections.fields(TeacherResponse.class,
                        teacher.id,
                        teacher.username,
                        teacher.teacherName,
                        teacher.email,
                        ExpressionUtils.as(
                                constant(TEACHER.getDescription()), "role"
                        ),
                        teacher.approved))
                .from(teacher)
                .where(teacher.username.eq(username))
                .fetchOne();
    }
}
