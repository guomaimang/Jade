package tech.hirsun.jade.service.Impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tech.hirsun.jade.controller.exception.custom.BadRequestException;
import tech.hirsun.jade.dao.PictureDao;
import tech.hirsun.jade.dao.TopicDao;
import tech.hirsun.jade.pojo.PageBean;
import tech.hirsun.jade.pojo.Picture;
import tech.hirsun.jade.result.ErrorCode;
import tech.hirsun.jade.service.PictureService;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import tech.hirsun.jade.utils.ThumbnailUtil;
import tech.hirsun.jade.utils.PictureMetaUtil;


@Service
@Slf4j
public class PictureServiceImpl implements PictureService {

    @Value("${pictures.path}")
    private String picturesPath;

    @Value("${tencent.map.service.key}")
    private String apiKey;

    @Autowired
    private TopicDao topicDao;

    @Autowired
    private PictureDao pictureDao;


    @Override
    public PageBean getTopicPictures(Integer topicId, Integer pageNum, Integer pageSize) {
        int count = pictureDao.count(topicId, null);

        int start = (pageNum - 1) * pageSize;
        List<Picture> pictures = pictureDao.list(topicId, null, start, pageSize);

        return new PageBean(count, pictures, Math.floorDiv(count, pageSize) + 1, pageNum);
    }

    @Override
    public Picture getInfo(Integer pictureId) {
        return pictureDao.query(pictureId);
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
        if (picture.getCoordinateX() != null && picture.getCoordinateY() != null){

            if (Double.parseDouble(picture.getCoordinateY()) > 90 || Double.parseDouble(picture.getCoordinateY()) < -90
                    || Double.parseDouble(picture.getCoordinateX()) > 180 || Double.parseDouble(picture.getCoordinateX()) < -180){
                throw new BadRequestException("Coordinate is invalid", ErrorCode.UPLOAD_FILE_ERROR);
            }else {
                picture.setLocation(convertCoordinateToAddress(picture.getCoordinateY(), picture.getCoordinateX()));
            }
        }



        if (picture.getTitle().length() > 30){
            throw new BadRequestException("Title is too long", ErrorCode.UPLOAD_FILE_ERROR);
        }
        if (picture.getDescription().length() > 200){
            throw new BadRequestException("Description is too long", ErrorCode.UPLOAD_FILE_ERROR);
        }
        picture.setCreateTime(new Date());
        picture.setExifSize(null);
        picture.setExifTime(null);
        picture.setExifLatitude(null);
        picture.setExifLongitude(null);
        picture.setExifLocation(null);

        // Generate UUID file name
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + ".jpg";

        // For Picture: Create directories if they do not exist
        Path userDir = Paths.get(picturesPath, "picture" , userId.toString());
        if (!Files.exists(userDir)) {
            Files.createDirectories(userDir);
        }

        // For Picture: Save the file
        Path filePath = userDir.resolve(fileName);
        Files.write(filePath, file.getBytes());

        // For Picture: read EXIF
        HashMap<String, String> exifMap = PictureMetaUtil.printImageTags(filePath.toFile());
        picture.setExifSize(exifMap.get("Image Height") + " x " + exifMap.get("Image Width"));
        picture.setExifTime(exifMap.get("Date/Time Original"));
        picture.setExifLatitude(exifMap.get("GPS Latitude"));
        picture.setExifLongitude(exifMap.get("GPS Longitude"));

        if (exifMap.get("Make") != null && exifMap.get("Model") != null){
            picture.setExifDevice(exifMap.get("Make") + " " + exifMap.get("Model"));
        }

        if (exifMap.get("GPS Latitude") != null && exifMap.get("GPS Longitude") != null){
            picture.setExifLocation(convertCoordinateToAddress(
                    exifMap.get("GPS Latitude"),
                    exifMap.get("GPS Longitude")));
        }

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
    public Integer deletePicture(Integer pictureId) {
        return 0;
    }

    @Override
    public PageBean getUserPictures(int loggedInUserId, Integer pageNum, Integer pageSize) {
        List<Picture> pictures = pictureDao.list(null, loggedInUserId, (pageNum - 1) * pageSize, pageSize);
        int count = pictureDao.count(null, loggedInUserId);
        return new PageBean(count, pictures, Math.floorDiv(count, pageSize) + 1, pageNum);
    }

    public String convertCoordinateToAddress(String latitude, String longitude) {
        if (apiKey == null) {
            log.error("API key is not available");
            return null;
        }

        OkHttpClient client = new OkHttpClient();
        String url = String.format("https://apis.map.qq.com/ws/geocoder/v1/?location=%s,%s&key=%s", latitude, longitude, apiKey);
        log.info("Request URL: {}", url);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = client.newCall(request);
        try (Response response = call.execute()) {
            if (!response.isSuccessful()) {
                log.error("Unexpected code: {}", response);
                return null;
            }

            String rspBody = response.body().string();
            log.info("Response body: {}", rspBody);
            JSONObject jsonObject = JSON.parseObject(rspBody);
            JSONObject result = jsonObject.getJSONObject("result");
            if (result != null) {
                JSONObject formattedAddresses = result.getJSONObject("formatted_addresses");
                if (formattedAddresses != null) {
                    String standardAddress = formattedAddresses.getString("standard_address");
                    if (standardAddress != null) {
                        return standardAddress;
                    }
                }
                return result.getString("address");
            }
            return null;
        } catch (Exception e) {
            log.error("Error in convertCoordinateToAddress", e);
            return null;
        }
    }

}
