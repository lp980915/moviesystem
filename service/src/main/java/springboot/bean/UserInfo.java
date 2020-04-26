package springboot.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private User user;
    private List<Comment> commentList;
    private List<CollectMovie> collectMovieList;
    private List<Message> messageList;
    private FriendInfo friendInfo;
    private List<Topic> topicList;
}
