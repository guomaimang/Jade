package tech.hirsun.jade.controller.exception.handler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import tech.hirsun.jade.controller.exception.custom.BadRequestException;
import tech.hirsun.jade.controller.exception.custom.ResourceNotFoundException;
import tech.hirsun.jade.result.ErrorCode;
import tech.hirsun.jade.result.Result;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class HTTPExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(HTTPExceptionHandler.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public Result handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        log.error("HTTP Controller Error - handleResourceNotFoundException is caught: {}", ex.getMessage());
        return Result.error(ex.getErrorCode(), request.getDescription(false));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public Result handleBadRequestException(BadRequestException ex, WebRequest request) {
        log.error("HTTP Controller Error - handleBadRequestException is caught: {}", ex.getMessage());
        return Result.error(ex.getErrorCode(), request.getDescription(false));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return Result.error(ErrorCode.REQUEST_ILLEGAL, errors.toString());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result handleGlobalException(Exception ex, WebRequest request) {
        log.error("HTTP Controller Error - handleGlobalException is caught: {}", ex.getMessage());
        return Result.error(ErrorCode.OTHER_ERROR, request.getDescription(false));
    }

    // 其他异常处理方法
}