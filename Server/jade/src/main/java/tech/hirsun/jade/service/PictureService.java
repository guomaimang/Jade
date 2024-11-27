package tech.hirsun.jade.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import tech.hirsun.jade.pojo.Picture;

import java.net.MalformedURLException;

public interface PictureService {
    Picture getPicturesByTopicId(Integer topicId, Integer pageNum, Integer pageSize);

    public Resource getFile(String path) throws MalformedURLException;

    Picture postPicture(MultipartFile file, Picture picture, Integer userId) throws Exception;

    Integer deletePictureByPictureId(Integer pictureId);
}
