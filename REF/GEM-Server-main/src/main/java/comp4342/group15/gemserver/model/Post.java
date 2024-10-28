package comp4342.group15.gemserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Post {
    private String postId;
    private String userId;
    private String message;
    private String location;
    private String postTime;
    private String picName;
}
