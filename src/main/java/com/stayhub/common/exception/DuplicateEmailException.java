package com.stayhub.common.exception;

public class DuplicateEmailException extends BusinessException {

    public DuplicateEmailException() {
        super("ERR_EMAIL_EXISTS", "An account with this email already exists.");
    }
}
