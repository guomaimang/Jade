package tech.hirsun.jade.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import tech.hirsun.jade.pojo.Topic;

import java.util.List;

@Mapper
public interface TopicDao {

    // By Annotation
    @Select("select count(*) from topic where id = #{topicId}")
    Integer count(Integer topicId);

    @Select("select * from topic")
    List<Topic> list();
}
