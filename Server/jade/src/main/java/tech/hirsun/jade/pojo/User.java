package tech.hirsun.jade.pojo;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String nickname;
    private String email;
    private String password;
}
