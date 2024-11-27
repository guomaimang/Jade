package tech.hirsun.jade.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TopicDao {

    // By Annotation
    @Select("select count(*) from topic where id = #{topicId}")
    public Integer count(Integer topicId);
}
