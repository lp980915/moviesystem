package springboot.bean;

import lombok.Data;

import java.util.List;

@Data
public class UserInfo {
    private User user;
    private List<Comment> commentList;
    private List<CollectMovie> collectMovieList;
    private List<Message> messageList;
    private FriendInfo friendInfo;
    private List<Topic> topicList;
}
