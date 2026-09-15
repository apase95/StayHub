package com.stayhub.storage;

import com.stayhub.common.exception.BusinessException;

public class StorageException extends BusinessException {
    public StorageException(String message) {
        super("ERR_STORAGE", message);
    }

    public StorageException(String message, Throwable cause) {
        super("ERR_STORAGE", message);
        initCause(cause);
    }
}
