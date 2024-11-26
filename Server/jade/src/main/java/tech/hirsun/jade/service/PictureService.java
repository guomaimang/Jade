package tech.hirsun.jade.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tech.hirsun.jade.pojo.Picture;

import java.io.File;

public interface PictureService {
    Picture getPicturesByTopicId(Integer topicId, Integer pageNum, Integer pageSize);

    Object getFileByPictureId(Integer pictureId);

    Object getThumbnailByPictureId(Integer pictureId);

    Picture postPicture(MultipartFile file, Picture picture, Integer userId) throws Exception;

    Integer deletePictureByPictureId(Integer pictureId);
}
