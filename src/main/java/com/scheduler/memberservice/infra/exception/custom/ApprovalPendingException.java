package com.scheduler.memberservice.infra.exception.custom;

import org.springframework.security.core.AuthenticationException;

public class ApprovalPendingException extends AuthenticationException {
    public ApprovalPendingException(String msg) {
        super(msg);
    }
}