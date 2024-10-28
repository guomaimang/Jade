package comp4342.group15.gemserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class Response {
    private String status;
    private String message;

    public static Response success(String message) {
        return new Response("Success", message);
    }

    public static Response fail(String message) {
        return new Response("Fail", message);
    }

    public static Response bad(String message) {
        return new Response("Bad", message);
    }
}
