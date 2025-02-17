package com.scheduler.memberservice.infra.exception;

import com.scheduler.memberservice.infra.exception.custom.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateExceptionHandle(IllegalStateException e) {
        return new ResponseEntity<>(e.getMessage(), FORBIDDEN);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException e) {
        return new ResponseEntity<>(e.getMessage(), FORBIDDEN);
    }

    @ExceptionHandler(DuplicateCourseException.class)
    public ResponseEntity<String> handleNullPointerException() {
        return new ResponseEntity<>(FORBIDDEN);
    }

    /*
       찾고자 하는 객체, 파일 또는 자료가 없을 때
    */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementExceptionHandle(NoSuchElementException e) {
        return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
    }


    @ExceptionHandler(MemberLoginException.class)
    public ResponseEntity<String> handleAuthorLoginException(MemberLoginException e) {
        return new ResponseEntity<>(e.getMessage(), FORBIDDEN);
    }

    /*
       잘못된 접근
    */
    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<String> handleIllegalAccessException(IllegalAccessException e) {
        return new ResponseEntity<>(e.getMessage(), UNAUTHORIZED);
    }

    /*
       파라미터 오류
    */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidExceptionHandle(
            MethodArgumentNotValidException e) {
        return new ResponseEntity<>(
                e.getBindingResult().getFieldErrors().get(0).getDefaultMessage(), BAD_REQUEST);
    }

    /*
       로그인 오류
    */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<String> handleDisabledException(DisabledException e) {
        return new ResponseEntity<>(e.getMessage(), FORBIDDEN);
    }

    @ExceptionHandler(ApprovalPendingException.class)
    public ResponseEntity<String> handleApprovalPendingException(ApprovalPendingException e) {
        return new ResponseEntity<>(e.getMessage(), FORBIDDEN);
    }

    /*
       인증번호 확인
    */
    @ExceptionHandler(InvalidAuthNumException.class)
    public ResponseEntity<String> handleInvalidAuthNumException() {
        return new ResponseEntity<>("invalid AuthNUm", FORBIDDEN);
    }

    @ExceptionHandler(MemberExistException.class)
    public ResponseEntity<String> handleMemberException() {
        return new ResponseEntity<>("no such member", FORBIDDEN);
    }


    @ExceptionHandler(AuthorityException.class)
    public ResponseEntity<String> handleAuthorityException() {
        return new ResponseEntity<>("do not have permission.", FORBIDDEN);
    }

    /*
       토큰 인증 오류
    */
    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ErrorDto> handleTokenException(TokenException e) {
        return new ResponseEntity<>(
                new ErrorDto(e.getErrorCode().getStatus(), e.getErrorCode().getMessage()),
                valueOf(e.getErrorCode().getStatus()));
    }
}
