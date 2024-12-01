package tech.hirsun.jade.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tech.hirsun.jade.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {

    // By Annotation
    @Select("select * from user where id = #{id}")
    public User getUserById(@Param("id")int id);

    @Select("select * from user where email = #{email}")
    public User getUserByEmail(@Param("email")String email);

    @Insert("insert into user (nickname, email, password) values (#{nickname}, #{email}, #{password})")
    public Integer insert(User user);
}
