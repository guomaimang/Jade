package comp4342.group15.gemserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class User {
    private String userId;
    private String userName;
    private String password;
    private String identifier;
    private String enable;
}

