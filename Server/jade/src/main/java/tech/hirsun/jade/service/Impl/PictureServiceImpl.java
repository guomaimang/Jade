package tech.hirsun.jade.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tech.hirsun.jade.controller.exception.custom.BadRequestException;
import tech.hirsun.jade.dao.PictureDao;
import tech.hirsun.jade.dao.TopicDao;
import tech.hirsun.jade.pojo.Picture;
import tech.hirsun.jade.result.ErrorCode;
import tech.hirsun.jade.service.PictureService;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;
import tech.hirsun.jade.utils.ThumbnailUtil;


@Service
public class PictureServiceImpl implements PictureService {

    private static final Logger log = LoggerFactory.getLogger(PictureServiceImpl.class);
    @Value("${pictures.path}")
    private String picturesPath;

    @Autowired
    private TopicDao topicDao;

    @Autowired
    private PictureDao pictureDao;


    @Override
    public Picture getPicturesByTopicId(Integer topicId, Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public Resource getFile(String subPath) throws MalformedURLException {
        Path filePath = Paths.get(picturesPath).resolve(subPath).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new BadRequestException("File not found", ErrorCode.RESOURCE_NOT_FOUND);
        }
    }

    @Override
    public Picture postPicture(MultipartFile file, Picture picture, Integer userId) throws Exception {

        // Save the picture info
        picture.setId(null);
        picture.setUserId(userId);
        if(picture.getTopicId() == null || topicDao.count(picture.getTopicId()) < 0) {
            throw new BadRequestException("Topic does not exist", ErrorCode.RESOURCE_NOT_FOUND);
        }
        picture.setViewCount(0);

        picture.setLocation(null);
        // check if the coordinate is valid
        if (picture.getCoordinateX() == null || picture.getCoordinateY() == null){
        }
        else if (Double.parseDouble(picture.getCoordinateX()) > 90 || Double.parseDouble(picture.getCoordinateX()) < -90
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

        // Generate UUID file name
        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + "." + fileExtension;

        // For Picture: Create directories if they do not exist
        Path userDir = Paths.get(picturesPath, "picture" , userId.toString());
        if (!Files.exists(userDir)) {
            Files.createDirectories(userDir);
        }

        // For Picture: Save the file
        Path filePath = userDir.resolve(fileName);
        Files.write(filePath, file.getBytes());

        // For Thumbnail: Create directories if they do not exist
        Path thumbnailUserDir = Paths.get(picturesPath, "thumbnail" , userId.toString());
        if (!Files.exists(thumbnailUserDir)) {
            Files.createDirectories(thumbnailUserDir);
        }

        // For Thumbnail: Generate thumbnail
        String thumbnailFileName = uuid + ".jpg";
        Path thumbnailFilePath = thumbnailUserDir.resolve(thumbnailFileName);
        ThumbnailUtil.generateThumbnail(filePath.toString(), thumbnailFilePath.toString(), 300, 200);

        // For Picture: Save the picture info
        picture.setFileName(fileName);
        picture.setId(pictureDao.insert(picture));

        return picture;
    }

    @Override
    public Integer deletePictureByPictureId(Integer pictureId) {
        return 0;
    }


}
