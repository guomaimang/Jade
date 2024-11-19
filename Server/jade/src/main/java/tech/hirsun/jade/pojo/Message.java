package tech.hirsun.jade.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Integer id;
    private Date createTime;
    private Integer userId;
    private Integer type;
    private String content;
}
