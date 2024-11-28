package tech.hirsun.jade.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tech.hirsun.jade.pojo.Picture;

import java.util.List;


@Mapper
public interface PictureDao {

    // By Annotation
    @Select("select * from picture where id = #{id}")
    Picture query(Integer id);


    // By XML
    int insert(Picture picture);

    List<Picture> list(@Param("topicId") Integer topicId,
                       @Param("userId") Integer userId,
                       @Param("start") Integer start,
                       @Param("pageSize") Integer pageSize
                       );

    int count(@Param("topicId") Integer topicId,
              @Param("userId") Integer userId);

}
