package comp4342.group15.gemserver.dao;

import comp4342.group15.gemserver.model.Post;
import comp4342.group15.gemserver.model.User;
import lombok.SneakyThrows;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class MySqlRepository {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private UserMapper<Object> userMapper;
    @Resource
    private PostMapper<Object> postMapper;

    public User retrieveUserById(String userId) {
        String statement = "SELECT * FROM userinfo WHERE id = ?";
        try {
            return (User) jdbcTemplate.queryForObject(statement, userMapper, userId);
        } catch (DataAccessException dataAccessException) {
            return null;
        }
    }

    @SneakyThrows
    public User retrieveUserByName(String name) {
        String statement = "SELECT * FROM userinfo WHERE username = ?";
        try {
            return (User) jdbcTemplate.queryForObject(statement, userMapper, name);
        } catch (DataAccessException dataAccessException) {
            return null;
        }
    }

    public void insertUser(User user) {
        String statement = String.format(
                """
                        INSERT INTO userinfo(username, password, enable)
                        VALUES('%s', '%s', %s)""", user.getUserName(), user.getPassword(), user.getEnable());
        jdbcTemplate.execute(statement);
    }

    public User updateUser(User user) {
        String identifier = String.valueOf(new Date().hashCode());
        String statement = String.format(
                """
                        UPDATE userinfo
                        SET identifier = %s
                        WHERE username = '%s'""", identifier, user.getUserName());
        try {
            jdbcTemplate.execute(statement);
            return retrieveUserByName(user.getUserName());
        } catch (DataAccessException dataAccessException) {
            return null;
        }
    }

    public Post retrievePostById(String id) {
        String statement = "SELECT * FROM post WHERE id = ?";
        try {
            return (Post) jdbcTemplate.queryForObject(statement, postMapper, id);
        } catch (DataAccessException dataAccessException) {
            return null;
        }
    }

    public void insertPost(Post post) {
        String statement = String.format(
                """
                        INSERT INTO post(user_id, message, location, post_time, pic_name)
                        VALUES(%s, '%s', '%s', NOW(), '%s')""",
                post.getUserId(), post.getMessage(), post.getLocation(), post.getPicName());
        jdbcTemplate.execute(statement);
    }

    public List<Map<String, Object>> retrieveTrend() {
        String statement = "SELECT * FROM post ORDER BY post_time DESC LIMIT 15";
        try {
            List<Map<String, Object>> postList = jdbcTemplate.queryForList(statement);
            for (var map : postList) {
                String userId = String.valueOf(map.get("user_id"));
                map.put("username", retrieveUserById(userId).getUserName());
                String postTime = String.valueOf(map.get("post_time"));
                map.put("post_time", postTime.replace('T', ' '));
            }
            return postList;
        } catch (DataAccessException dataAccessException) {
            return new ArrayList<>();
        }
    }

    public void select1() {
        jdbcTemplate.execute("SELECT 1 FROM DUAL");
    }
}
