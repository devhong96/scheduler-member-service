package com.scheduler.memberservice.client.dto;

import com.scheduler.memberservice.member.common.RoleType;
import lombok.Getter;
import lombok.Setter;

public class FeignMemberResponse {

    @Getter
    @Setter
    public static class StudentInfo {

        private String studentId;
        private String studentName;
        private String teacherId;

        public StudentInfo(String studentId, String studentName, String teacherId) {
            this.studentId = studentId;
            this.studentName = studentName;
            this.teacherId = teacherId;
        }
    }

    @Getter
    @Setter
    public static class TeacherInfo {

        private String teacherId;
    }

    @Getter
    @Setter
    public static class MemberInfo {

        private RoleType roleType;
        private String memberId;

        public MemberInfo(RoleType roleType, String memberId) {
            this.roleType = roleType;
            this.memberId = memberId;
        }
    }
}