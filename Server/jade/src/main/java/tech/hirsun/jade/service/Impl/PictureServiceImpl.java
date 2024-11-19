package tech.hirsun.jade.service.Impl;

import org.springframework.stereotype.Service;
import tech.hirsun.jade.pojo.Picture;
import tech.hirsun.jade.service.PictureService;

@Service
public class PictureServiceImpl implements PictureService {
    @Override
    public Picture getPicturesByTopicId(Integer topicId, Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public Object getFileByPictureId(Integer pictureId) {
        return null;
    }

    @Override
    public Object getThumbnailByPictureId(Integer pictureId) {
        return null;
    }

    @Override
    public Integer postPicture(Picture picture) {
        return 0;
    }

    @Override
    public Integer deletePictureByPictureId(Integer pictureId) {
        return 0;
    }
}
