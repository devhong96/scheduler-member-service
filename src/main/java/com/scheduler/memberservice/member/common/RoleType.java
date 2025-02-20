package com.scheduler.memberservice.member.common;

import lombok.Getter;

@Getter
public enum RoleType {

    ADMIN("관리자"),
    TEACHER("선생님"),
    STUDENT("학생");

    private final String description;

    RoleType(String description) {
        this.description = description;
    }
}
