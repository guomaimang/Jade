package tech.hirsun.jade.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.hirsun.jade.pojo.Picture;
import tech.hirsun.jade.result.Result;
import tech.hirsun.jade.service.PictureService;

@Slf4j
@RestController
@RequestMapping("/picture")
public class PictureController {

    @Autowired
    private PictureService pictureService;

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
     * @param picture: picture object
     */
    @PostMapping("/upload")
    private Result postPicture(@RequestBody Picture picture) {
        log.info("Request post picture: {}", picture);
        return Result.success(pictureService.postPicture(picture));
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


}
