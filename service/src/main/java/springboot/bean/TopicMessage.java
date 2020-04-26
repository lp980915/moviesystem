package springboot.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class TopicMessage {
    private String topicmessageid;
    private String topicid;
    private String userid;
    private String username;
    private String userimg;
    private String topicmessagedate;
    private String content;
}
