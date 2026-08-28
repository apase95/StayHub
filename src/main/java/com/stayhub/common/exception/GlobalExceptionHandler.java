package com.stayhub.common.exception;

import com.stayhub.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Catch data not found (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ApiResponse.error("ERR_NOT_FOUND", ex.getMessage());
    }

    // Catch business logic errors (400)
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleBusinessException(BusinessException ex) {
        return ApiResponse.error(ex.getErrorCode(), ex.getMessage());
    }

    // Catch invalid state transition errors (409)
    @ExceptionHandler(InvalidStateTransitionException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<Void> handleInvalidStateTransitionException(InvalidStateTransitionException ex) {
        return ApiResponse.error(ex.getErrorCode(), ex.getMessage());
    }

    // Catch URL not found errors (404)
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return ApiResponse.error("ERR_ENDPOINT_NOT_FOUND", "Đường dẫn không tồn tại trên hệ thống.");
    }

    // Catch all remaining system errors (500)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleGlobalException(Exception ex) {
        log.error("Lỗi hệ thống không mong muốn: ", ex);
        return ApiResponse.error("ERR_INTERNAL_SERVER", "Đã xảy ra lỗi hệ thống, vui lòng thử lại sau.");
    }
}