package tech.hirsun.jade.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import tech.hirsun.jade.pojo.PageBean;
import tech.hirsun.jade.pojo.Picture;

import java.net.MalformedURLException;
import java.util.List;

public interface PictureService {
    PageBean getTopicPictures(Integer topicId, Integer pageNum, Integer pageSize);

    Picture getInfo(Integer pictureId);

    Resource getFile(String subPath) throws MalformedURLException;

    Picture postPicture(MultipartFile file, Picture picture, Integer userId) throws Exception;

    Integer deletePicture(Integer pictureId);

    List getUserPictures(int loggedInUserId, Integer pageNum, Integer pageSize);
}
