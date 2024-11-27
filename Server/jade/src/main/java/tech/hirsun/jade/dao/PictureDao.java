package tech.hirsun.jade.dao;


import org.apache.ibatis.annotations.Mapper;
import tech.hirsun.jade.pojo.Picture;


@Mapper
public interface PictureDao {

    // By XML
    int insert(Picture picture);
}
