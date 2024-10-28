package comp4342.group15.gemserver.dao;

import comp4342.group15.gemserver.model.Post;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class PostMapper<Object> implements RowMapper<Object> {
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Post post = new Post();
        post.setPostId(rs.getString("id"));
        post.setUserId(rs.getString("user_id"));
        post.setMessage(rs.getString("message"));
        post.setLocation(rs.getString("location"));
        post.setPostTime(rs.getString("post_time"));
        post.setPicName(rs.getString("pic_name"));
        return (Object) post;
    }
}
