package com.stayhub.common.exception;

import lombok.Getter;

@Getter
public class InvalidStateTransitionException extends RuntimeException {
    private final String errorCode = "ERR_INVALID_STATE";

    public InvalidStateTransitionException(String message) {
        super(message);
    }
}