package tech.hirsun.jade.result;

import lombok.Getter;

import java.util.Date;

@Getter
public class Result<T> {

    private int code;    // Response code, 0 for success; others for failure
    private String message;  // Response message
    private T data;      // Response data
    private final Date timestamp = new Date(); // Response date
    private String error; // error details
    private String path; // request uri prompt

    private Result(T data) {
        this.code = 0;
        this.message = "success";
        this.data = data;
        this.error = null;
    }

    // For success result
    public static <T> Result<T> success(T data) {
        return new Result<>(data);
    }
    public static <T> Result<T> success() {
        return new Result<>(null);
    }

    // For error result
    public static Result error(ErrorCode errorCode, String path) {

        Result errResult = new Result<>(errorCode);
        errResult.code = errorCode.getCode();
        errResult.message = "error";
        errResult.data = null;
        errResult.error = errorCode.getMessage();
        errResult.path = path;
        return errResult;
    }



}

