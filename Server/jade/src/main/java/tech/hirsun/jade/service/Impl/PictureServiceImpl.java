package tech.hirsun.jade.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tech.hirsun.jade.controller.exception.custom.BadRequestException;
import tech.hirsun.jade.dao.PictureDao;
import tech.hirsun.jade.pojo.Picture;
import tech.hirsun.jade.result.ErrorCode;
import tech.hirsun.jade.service.PictureService;
import tech.hirsun.jade.service.TopicService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;


@Service
public class PictureServiceImpl implements PictureService {

    @Value("${pictures.path}")
    private String picturesPath;

    @Autowired
    private TopicService topicService;

    @Autowired
    private PictureDao pictureDao;


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
    public Picture postPicture(MultipartFile file, Picture picture, Integer userId) throws Exception {

        // Save the picture info
        picture.setId(null);
        picture.setUserId(userId);
        if(picture.getTopicId() == null || topicService.checkExist(picture.getTopicId())) {
            throw new BadRequestException("Topic does not exist", ErrorCode.RESOURCE_NOT_FOUND);
        }
        picture.setViewCount(0);

        picture.setUri(null);
        picture.setThumbnailUrl(null);

        picture.setLocation(null);
        // check if the coordinate is valid
        if (Double.parseDouble(picture.getCoordinateX()) > 90 || Double.parseDouble(picture.getCoordinateX()) < -90
                || Double.parseDouble(picture.getCoordinateY()) > 180 || Double.parseDouble(picture.getCoordinateY()) < -180){
            throw new BadRequestException("Coordinate is invalid", ErrorCode.UPLOAD_FILE_ERROR);
        }

        if (picture.getTitle().length() > 20){
            throw new BadRequestException("Title is too long", ErrorCode.UPLOAD_FILE_ERROR);
        }
        picture.setCreateTime(new Date());
        if (picture.getDescription().length() > 200){
            throw new BadRequestException("Description is too long", ErrorCode.UPLOAD_FILE_ERROR);
        }

        // Create directories if they do not exist
        Path userDir = Paths.get(picturesPath, userId.toString());
        if (!Files.exists(userDir)) {
            Files.createDirectories(userDir);
        }

        // Generate UUID file name
        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + "." + fileExtension;

        // Save the file
        Path filePath = userDir.resolve(fileName);
        Files.write(filePath, file.getBytes());

        // Save the picture info
        picture.setUri(filePath.toString());
        picture.setId(pictureDao.insert(picture));
        return picture;
    }

    @Override
    public Integer deletePictureByPictureId(Integer pictureId) {
        return 0;
    }
}
