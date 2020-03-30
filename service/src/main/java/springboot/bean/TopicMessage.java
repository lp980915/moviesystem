package springboot.bean;

import lombok.Data;

@Data
public class TopicMessage {
    private String topicmessageid;
    private String topicid;
    private String userid;
    private String username;
    private String userimg;
    private String topicmessagedate;
    private String content;
}
