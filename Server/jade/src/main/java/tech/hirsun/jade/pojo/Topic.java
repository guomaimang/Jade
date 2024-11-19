package tech.hirsun.jade.pojo;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Topic {
    private Integer id;
    private String tag;
    private Date createTime;
}
