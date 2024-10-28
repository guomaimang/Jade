package comp4342.group15.gemserver.dao;

import comp4342.group15.gemserver.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class UserMapper<Object> implements RowMapper<Object> {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setUserId(String.valueOf(rs.getInt("id")));
        user.setUserName(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setIdentifier(rs.getString("identifier"));
        user.setEnable(rs.getString("enable"));
        return (Object) user;
    }
}