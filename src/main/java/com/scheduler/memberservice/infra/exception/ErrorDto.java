package com.scheduler.memberservice.infra.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ErrorDto {
    private final int status;
    private final String message;
}
