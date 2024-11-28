package tech.hirsun.jade.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Picture {
    private Integer id;
    private Integer userId;
    private Integer topicId;
    private String fileName;

    private Integer viewCount;
    private String location;
    private String coordinateX;
    private String coordinateY;

    private String title;
    private Date createTime;
    private String description;
}
