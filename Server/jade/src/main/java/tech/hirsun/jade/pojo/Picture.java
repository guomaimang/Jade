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
    private Integer viewCount;

    private String uri;
    private String thumbnail_url;

    private String location;
    private String coordinate_x;
    private String coordinate_y;

    private String title;
    private Date createTime;
    private String description;
}
