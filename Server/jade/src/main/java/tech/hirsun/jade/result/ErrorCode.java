package tech.hirsun.jade.result;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorCode {

    /**
     * Error code and message 是反馈给前端的信息
     * 后端日志不会反馈这些信息。后端日志将写入抛出的异常的 message
     */
    private int code;
    private String message;

    // Client's blame: 400
    // Login/User
    public static ErrorCode USER_NOT_EXIST = new ErrorCode(40011, "No match found. Please try again.");
    public static ErrorCode USER_NOT_LOGIN = new ErrorCode(40012, "User not Login. Please login first.");
    public static ErrorCode USER_NO_PERMISSION = new ErrorCode(40013, "No Permission. Please try again.");

    // General
    public static ErrorCode RESOURCE_NOT_FOUND = new ErrorCode(40014, "Resource Not Found.");

    // Upload
    public static ErrorCode UPLOAD_FILE_ERROR = new ErrorCode(40015, "File is not uploaded successfully. Please try again.");
    public static ErrorCode UPLOAD_FILE_TYPE_ERROR = new ErrorCode(40016, "File type is not supported. Please try again.");
    public static ErrorCode UPLOAD_FILE_SIZE_ERROR = new ErrorCode(40017, "File size is too large. Please try again.");

    // SSO
    public static ErrorCode SSO_TOKEN_INVALID = new ErrorCode(40021, "Token Invalid. Please try again.");
    public static ErrorCode SSO_NO_PERMISSION = new ErrorCode(40022, " You are now in AD. Please contact support.");

    // Business
    public static ErrorCode REQUEST_ILLEGAL = new ErrorCode(40013, "Request/Message Illegal.");

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
