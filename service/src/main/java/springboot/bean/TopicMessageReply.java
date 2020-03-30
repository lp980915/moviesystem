package springboot.bean;

import lombok.Data;

@Data
public class TopicMessageReply {
    private String topicid;
    private String topicmessageid;
    private String userid;
    private String username;
    private String userimg;
    private String touserid;
    private String tousername;
    private String topicmessagereplydate;
    private String replycontent;
}
