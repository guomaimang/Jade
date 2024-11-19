package tech.hirsun.jade.controller.exception.handler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import tech.hirsun.jade.controller.exception.custom.BadRequestException;
import tech.hirsun.jade.controller.exception.custom.ResourceNotFoundException;
import tech.hirsun.jade.result.ErrorCode;
import tech.hirsun.jade.result.Result;

@ControllerAdvice
public class HTTPExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(HTTPExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        Result errorResult = Result.error(ex.getErrorCode(), request.getDescription(false));
        log.error("HTTP Controller Error - handleResourceNotFoundException is caught: {}", ex.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException ex, WebRequest request) {
        Result errorResult = Result.error(ex.getErrorCode(), request.getDescription(false));
        log.error("HTTP Controller Error - handleBadRequestException is caught: {}", ex.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        Result errorResult = Result.error(ErrorCode.OTHER_ERROR, request.getDescription(false));
        log.error("HTTP Controller Error - handleGlobalException is caught: {}", ex.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 其他异常处理方法
}