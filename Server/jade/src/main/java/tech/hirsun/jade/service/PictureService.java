package tech.hirsun.jade.service;

import org.springframework.stereotype.Service;
import tech.hirsun.jade.pojo.Picture;

public interface PictureService {
    Picture getPicturesByTopicId(Integer topicId, Integer pageNum, Integer pageSize);

    Object getFileByPictureId(Integer pictureId);

    Object getThumbnailByPictureId(Integer pictureId);

    Integer postPicture(Picture picture);

    Integer deletePictureByPictureId(Integer pictureId);
}
