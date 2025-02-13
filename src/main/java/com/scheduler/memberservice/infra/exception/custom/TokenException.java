package com.scheduler.memberservice.infra.exception.custom;

import com.scheduler.memberservice.infra.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenException extends RuntimeException {
    private final ErrorCode errorCode;
}
