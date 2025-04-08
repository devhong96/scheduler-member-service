package com.scheduler.memberservice.infra.exception.custom;

public class PasswordMismatchException extends RuntimeException {

    // 기본 생성자
    public PasswordMismatchException() {
        super("비밀번호 불일치 에러");
    }

    // 메시지를 받는 생성자
    public PasswordMismatchException(String message) {
        super(message);
    }

    // 메시지와 원인(cause)를 받는 생성자
    public PasswordMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    // 원인(cause)만 받는 생성자
    public PasswordMismatchException(Throwable cause) {
        super(cause);
    }
}