package springboot.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
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
