package com.stayhub.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
@ControllerAdvice(annotations = Controller.class)
public class MvcExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFoundException(ResourceNotFoundException exception,
                                                        HttpServletRequest request) {
        return errorView(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(InvalidStateTransitionException.class)
    public ModelAndView handleInvalidStateTransitionException(InvalidStateTransitionException exception,
                                                              HttpServletRequest request) {
        return errorView(HttpStatus.CONFLICT, exception.getMessage(), request);
    }

    @ExceptionHandler(BusinessException.class)
    public ModelAndView handleBusinessException(BusinessException exception, HttpServletRequest request) {
        return errorView(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception,
                                                             HttpServletRequest request) {
        return errorView(HttpStatus.PAYLOAD_TOO_LARGE,
                "The upload exceeds the configured size limit.", request);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleUnexpectedException(Exception exception, HttpServletRequest request) {
        log.error("Unexpected MVC error", exception);
        return errorView(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.", request);
    }

    private ModelAndView errorView(HttpStatus status, String message, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.setStatus(status);
        modelAndView.addObject("status", status.value());
        modelAndView.addObject("error", status.getReasonPhrase());
        modelAndView.addObject("message", message);
        modelAndView.addObject("path", request.getRequestURI());
        return modelAndView;
    }
}
