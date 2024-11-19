package tech.hirsun.jade.result;

import lombok.Getter;
import lombok.Setter;

public class ErrorMessage {

    @Getter
    @Setter
    private int code;
    @Getter
    @Setter
    private String message;

    // Server Message, like 5000x
    public static ErrorMessage SERVER_ERROR = new ErrorMessage(50001, "Server Error");
    public static ErrorMessage RECAPTCHA_ERROR = new ErrorMessage(50002, "Recaptcha Error. Please click it.");
    public static ErrorMessage REQUEST_ILLEGAL = new ErrorMessage(50003, "Request Illegal.");
    public static ErrorMessage REFUSE_SERVICE = new ErrorMessage(50004, "Access Limit Reached.");

    // Login/User Message, like 5001x
    public static ErrorMessage USER_NOT_EXIST = new ErrorMessage(50011, "No match found. Please try again.");
    public static ErrorMessage USER_NOT_LOGIN = new ErrorMessage(50012, "User not Login. Please login first.");

    public static ErrorMessage USER_NO_PERMISSION = new ErrorMessage(50013, "No Permission. Please try again.");


    //constructor
    private ErrorMessage() {
    }

    public ErrorMessage(int code, String message) {
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
