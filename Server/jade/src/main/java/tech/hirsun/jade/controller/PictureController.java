package tech.hirsun.jade.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("/get_list_by_topic_id")
    private Result getListByTopicId(@RequestParam Integer topicId,
                                           @RequestParam(defaultValue = "1") Integer pageNum,
                                           @RequestParam(defaultValue = "20") Integer pageSize) {
        log.info("Request pictures by topic id: {}, pageNum: {}, pageSize: {}", topicId, pageNum, pageSize);
        return Result.success(pictureService.getPicturesByTopicId(topicId, pageNum, pageSize));
    }

    /**
     * Get picture by picture id
     * @param pictureId: picture id
     */
    @GetMapping("/get_file_by_picture_id")
    private Result getFileByPictureId(@RequestParam Integer pictureId) {
        log.info("Request picture by picture id: {}", pictureId);
        return Result.success(pictureService.getFileByPictureId(pictureId));
    }

    /**
     * Get picture thumbnail by picture id
     */
    @GetMapping("/get_thumbnail_by_picture_id")
    private Result getThumbnailByPictureId(@RequestParam Integer pictureId) {
        log.info("Request picture thumbnail by picture id: {}", pictureId);
        return Result.success(pictureService.getThumbnailByPictureId(pictureId));
    }

    /**
     * upload picture
     * @param file: picture object
     */
    @PostMapping("/upload")
    private Result uploadPicture(@RequestHeader String jwt, @RequestBody MultipartFile file, @RequestBody Picture picture) throws Exception {
        int loggedInUserId = Integer.parseInt(JwtUtils.parseJwt(jwt).get("id").toString());

        if (file.isEmpty()) {
            throw new BadRequestException("File is empty", ErrorCode.UPLOAD_FILE_ERROR);
        }

        long maxSize = parseSize(maxSizeMB);
        if (file.getSize() > maxSize) {
            throw new BadRequestException("File size exceeds the limit", ErrorCode.UPLOAD_FILE_SIZE_ERROR);
        }

        // Validate file type
        String contentType = file.getContentType();
        if (!isSupportedContentType(contentType)) {
            throw new BadRequestException("Unsupported file type", ErrorCode.UPLOAD_FILE_TYPE_ERROR);
        }

        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (fileExtension == null || !fileExtension.matches("^(jpg|jpeg|png|heic)$")) {
            throw new BadRequestException("Invalid file extension", ErrorCode.UPLOAD_FILE_TYPE_ERROR);
        }

        return Result.success(pictureService.postPicture(file, picture, loggedInUserId));
    }

    /**
     * Delete picture by picture id
     * @param pictureId: picture id
     */
    @DeleteMapping("/delete_by_picture_id")
    private Result deleteByPictureId(@RequestParam Integer pictureId) {
        log.info("Request delete picture by picture id: {}", pictureId);
        return Result.success(pictureService.deletePictureByPictureId(pictureId));
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
