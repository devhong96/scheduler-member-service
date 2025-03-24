package com.scheduler.memberservice.infra.exception.custom;

public class MemberExistException extends RuntimeException {

    // 기본 생성자
    public MemberExistException() {
        super("회원 에러");
    }

    // 메시지를 받는 생성자
    public MemberExistException(String message) {
        super(message);
    }

    // 메시지와 원인(cause)를 받는 생성자
    public MemberExistException(String message, Throwable cause) {
        super(message, cause);
    }

    // 원인(cause)만 받는 생성자
    public MemberExistException(Throwable cause) {
        super(cause);
    }
}