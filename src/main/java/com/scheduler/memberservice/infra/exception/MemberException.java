package com.scheduler.memberservice.infra.exception;

import com.scheduler.memberservice.infra.exception.custom.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class MemberException {

    @ExceptionHandler(AuthorApproveException.class)
    public ResponseEntity<String> handleAuthorApproveException() {
        return new ResponseEntity<>("wait for approve", FORBIDDEN);
    }

    @ExceptionHandler(InvalidAuthNumException.class)
    public ResponseEntity<String> handleInvalidAuthNumException() {
        return new ResponseEntity<>("invalid AuthNUm", FORBIDDEN);
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<String> handleDuplicateUsernameException() {
        return new ResponseEntity<>("already existed username", FORBIDDEN);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<String> handleDuplicateEmailException() {
        return new ResponseEntity<>("email already exist.", FORBIDDEN);
    }
    @ExceptionHandler(MemberExistException.class)
    public ResponseEntity<String> handleMemberExistException() {
        return new ResponseEntity<>(NOT_FOUND);
    }
}
