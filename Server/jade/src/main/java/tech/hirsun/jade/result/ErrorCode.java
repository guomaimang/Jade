package tech.hirsun.jade.result;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorCode {

    private int code;
    private String message;

    // Client's blame: 400
    // Login/User
    public static ErrorCode USER_NOT_EXIST = new ErrorCode(40011, "No match found. Please try again.");
    public static ErrorCode USER_NOT_LOGIN = new ErrorCode(40012, "User not Login. Please login first.");
    public static ErrorCode USER_NO_PERMISSION = new ErrorCode(40013, "No Permission. Please try again.");

    // SSO
    public static ErrorCode SSO_TOKEN_INVALID = new ErrorCode(40021, "Token Invalid. Please try again.");
    public static ErrorCode SSO_NO_PERMISSION = new ErrorCode(40022, " You are now in AD. Please contact support.");

    // Business
    public static ErrorCode REQUEST_ILLEGAL = new ErrorCode(40013, "Request Illegal.");

    // Server's blame: 500
    // General
    public static ErrorCode REFUSE_SERVICE = new ErrorCode(50004, "Access Limit Reached. Please try later.");

    // Other
    public static ErrorCode OTHER_ERROR = new ErrorCode(90000, "No more comment. Please contact support");


    public ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "CodeMessage{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
