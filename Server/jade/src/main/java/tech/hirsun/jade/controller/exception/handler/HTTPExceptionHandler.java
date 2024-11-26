package tech.hirsun.jade.controller.exception.handler;


import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
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
        log.info("HTTP Controller Error - handleResourceNotFoundException is caught: {}", ex.getMessage());
        return Result.error(ex.getErrorCode(), request.getDescription(false));
    }


    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity handleNoHandlerFoundException(NoHandlerFoundException ex, WebRequest request) {
        log.info("HTTP Controller Error - handleNoHandlerFoundException is caught: {}", ex.getMessage());
        Result result = Result.error(ErrorCode.RESOURCE_NOT_FOUND, request.getDescription(false));
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public Result handleBadRequestException(BadRequestException ex, WebRequest request) {
        log.info("HTTP Controller Error - handleBadRequestException is caught: {}", ex.getMessage());
        return Result.error(ex.getErrorCode(), request.getDescription(false));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result methodArgumentNotValidExceptionExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.info("HTTP Controller Error - methodArgumentNotValidExceptionExceptions is caught: {}", ex.getMessage());
        return Result.error(ErrorCode.REQUEST_ILLEGAL, errors.toString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Result constraintViolationExceptionExceptions(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        });
        log.info("HTTP Controller Error - constraintViolationExceptionExceptions is caught: {}", ex.getMessage());
        return Result.error(ErrorCode.REQUEST_ILLEGAL, errors.toString());
    }

//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(Exception.class)
//    public Result handleGlobalException(Exception ex, WebRequest request) {
//        log.error("HTTP Controller Error - handleGlobalException is caught: {}", ex.getMessage());
//        return Result.error(ErrorCode.OTHER_ERROR, request.getDescription(false));
//    }

    // 其他异常处理方法
}