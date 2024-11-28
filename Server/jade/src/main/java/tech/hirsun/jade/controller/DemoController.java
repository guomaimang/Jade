package tech.hirsun.jade.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.hirsun.jade.pojo.Picture;
import tech.hirsun.jade.utils.StringAndBeanConventer;

@Slf4j
@RestController
@RequestMapping("/demo")
public class DemoController {

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file,
                                                   @RequestParam String picture) {

        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }

        Picture pc = StringAndBeanConventer.stringToBean(picture, Picture.class);

        // 处理文件上传逻辑
        log.info("picture: {}", pc);
        return ResponseEntity.ok("File uploaded successfully");
    }
}
