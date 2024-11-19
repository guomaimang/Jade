package tech.hirsun.jade.controller.exception.custom;

import lombok.Getter;
import tech.hirsun.jade.result.ErrorCode;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public ResourceNotFoundException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}