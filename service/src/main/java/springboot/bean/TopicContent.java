package springboot.bean;

import lombok.Data;

import java.util.List;

@Data
public class TopicContent {
    private List<TopicMessage> topicMessageList;
    private List<TopicMessageReply> topicMessageReplyList;
}
