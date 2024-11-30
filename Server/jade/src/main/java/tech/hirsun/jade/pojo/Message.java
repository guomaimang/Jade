package tech.hirsun.jade.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String id;
    private Date createTime;
    private Integer userId;
    private Integer type;
    private String content;
    private String nickname;
    private Integer topicId;
}
