package tech.hirsun.jade.result;

import lombok.Getter;

import java.util.Date;

@Getter
public class Result<T> {

    private int code;    // Response code, 0 for success; others for failure
    private String msg;  // Response message
    private T data;      // Response data
    private final Date dateStamp = new Date(); // Response date
    private String details; // Response details

    private Result(T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
        this.details = null;
    }

    // For success result
    public static <T> Result<T> success(T data) {
        return new Result<>(data);
    }
    public static <T> Result<T> success() {
        return new Result<>(null);
    }

    // For error result
    public static Result error(ErrorCode errorCode, String details) {

        Result errResult = new Result<>(errorCode);
        errResult.code = errorCode.getCode();
        errResult.msg = errorCode.getMessage();
        errResult.data = null;
        errResult.details = details;
        return errResult;
    }



}

