package tech.hirsun.jade.controller;

import org.springframework.core.io.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.hirsun.jade.pojo.Picture;
import tech.hirsun.jade.result.ErrorCode;
import tech.hirsun.jade.result.Result;
import tech.hirsun.jade.service.PictureService;
import tech.hirsun.jade.utils.JwtUtils;
import tech.hirsun.jade.controller.exception.custom.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import tech.hirsun.jade.utils.StringAndBeanConventer;

import java.net.MalformedURLException;


@Slf4j
@RestController
@RequestMapping("/picture")
public class PictureController {

    @Autowired
    private PictureService pictureService;

    @Value("${pictures.max-size}")
    private String maxSizeMB;

    /**
     * Get picture id list by topic id
     * @param topicId: topic id
     * @param pageNum: the page number, default is 1
     * @param pageSize: the page size, default is 20
     */
    @GetMapping("/list")
    private Result listByTopicId(@RequestParam Integer topicId,
                                           @RequestParam(defaultValue = "1") Integer pageNum,
                                           @RequestParam(defaultValue = "20") Integer pageSize) {
        log.info("Request pictures by topic id: {}, pageNum: {}, pageSize: {}", topicId, pageNum, pageSize);
        return Result.success(pictureService.getTopicPictures(topicId, pageNum, pageSize));
    }


    /**
     * upload picture
     * @param file: file object
     * @param picture: picture object json
     */
    @PostMapping("/upload")
    private Result uploadPicture(@RequestHeader String jwt,
                                 @RequestParam("file") MultipartFile file,
                                 @RequestParam String picture) throws Exception {
        int loggedInUserId = Integer.parseInt(JwtUtils.parseJwt(jwt).get("id").toString());

        log.info("picture: {}", picture);

        if (file.isEmpty()) {
            throw new BadRequestException("File is empty", ErrorCode.UPLOAD_FILE_ERROR);
        }

        long maxSize = parseSize(maxSizeMB);
        if (file.getSize() > maxSize) {
            throw new BadRequestException("File size exceeds the limit", ErrorCode.UPLOAD_FILE_SIZE_ERROR);
        }

        log.info("Request upload picture, file name: {}", file.getOriginalFilename());

        // Validate file type
        String contentType = file.getContentType();
        if (!isSupportedContentType(contentType)) {
            throw new BadRequestException("Unsupported file type", ErrorCode.UPLOAD_FILE_TYPE_ERROR);
        }

        return Result.success(pictureService.postPicture(file, StringAndBeanConventer.stringToBean(picture, Picture.class), loggedInUserId));
    }

    /**
     * Delete picture by picture id
     * @param pictureId: picture id
     */
    @DeleteMapping("/delete")
    private Result deletePicture(@RequestParam Integer pictureId) {
        log.info("Request delete picture by picture id: {}", pictureId);
        return Result.success(pictureService.deletePicture(pictureId));
    }


    /**
     * Get picture file
     * @param file_name: file name, like uuid.jpg
     * @param user_id: user id, refer the user_id of the picture
     * @param resolution: file type, thumbnail or picture
     */
    @GetMapping("/get_file")
    private ResponseEntity<Resource> getPictureFile(@RequestParam String file_name,
                                                    @RequestParam String user_id,
                                                    @RequestParam String resolution) throws MalformedURLException {

        // check file name, only allow alphanumeric, dash, dot
        if (!file_name.matches("^[a-zA-Z0-9-\\.]+$")) {
            throw new BadRequestException("Invalid file name", ErrorCode.REQUEST_ILLEGAL);
        }

        // only support jpg, jpeg, png, heic
        String fileExtension = StringUtils.getFilenameExtension(file_name);
        if (fileExtension == null || !fileExtension.matches("^(jpg|jpeg|png|heic|JPG|JPEG|PNG|HEIC)$")) {
            throw new BadRequestException("Invalid file extension", ErrorCode.REQUEST_ILLEGAL);
        }


        // subpath = type / + user_id + "/" + file_name
        String subPath;
        if (resolution.equals("thumbnail")) {
            // file_name: set file extension to jpg
            file_name = file_name.substring(0, file_name.lastIndexOf(".")) + ".jpg";
            subPath = "thumbnail/" + user_id + "/" + file_name;
        } else if (resolution.equals("picture")) {
            subPath = "picture/" + user_id + "/" + file_name;
        } else {
            throw new BadRequestException("Invalid file type", ErrorCode.REQUEST_ILLEGAL);
        }

        Resource resource = pictureService.getFile(subPath);

        if (resource.exists() && resource.isReadable()) {
            String contentType = fileExtension.equals("jpg") || fileExtension.equals("jpeg") ? "image/jpeg" :
                    fileExtension.equals("png") ? "image/png" :
                            fileExtension.equals("heic") ? "image/heic" : "application/octet-stream";
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            throw new BadRequestException("File not found or not readable", ErrorCode.RESOURCE_NOT_FOUND);
        }
    }

    @GetMapping("/info")
    private Result getPictureInfo(@RequestParam Integer pictureId) {
        log.info("Request picture info, pictureId: {}", pictureId);

        return Result.success(pictureService.getInfo(pictureId));
    }

    @GetMapping("/myself")
    private Result getMyselfPictures(@RequestHeader String jwt,
                                     @RequestParam(defaultValue = "1") Integer pageNum,
                                     @RequestParam(defaultValue = "20") Integer pageSize) {
        int loggedInUserId = Integer.parseInt(JwtUtils.parseJwt(jwt).get("id").toString());
        log.info("Request pictures by user id: {}, pageNum: {}, pageSize: {}", loggedInUserId, pageNum, pageSize);
        return Result.success(pictureService.getUserPictures(loggedInUserId, pageNum, pageSize));
    }


    private long parseSize(String size) {
        size = size.toUpperCase();
        if (size.endsWith("MB")) {
            return Long.parseLong(size.replace("MB", "")) * 1024 * 1024;
        } else if (size.endsWith("KB")) {
            return Long.parseLong(size.replace("KB", "")) * 1024;
        } else if (size.endsWith("B")) {
            return Long.parseLong(size.replace("B", ""));
        } else {
            throw new IllegalArgumentException("Invalid size format");
        }
    }

    private boolean isSupportedContentType(String contentType) {
        return "image/jpeg".equals(contentType) || "image/png".equals(contentType) || "image/heic".equals(contentType);
    }

}
