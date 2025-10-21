package com.scheduler.memberservice.infra.exception.custom;

public class InvalidAuthNumException extends RuntimeException {
    // 기본 생성자
    public InvalidAuthNumException() {
        super("invalid AuthNum Exception");
    }

    // 메시지를 받는 생성자
    public InvalidAuthNumException(String message) {
        super(message);
    }

    // 메시지와 원인(cause)를 받는 생성자
    public InvalidAuthNumException(String message, Throwable cause) {
        super(message, cause);
    }

    // 원인(cause)만 받는 생성자
    public InvalidAuthNumException(Throwable cause) {
        super(cause);
    }
}
