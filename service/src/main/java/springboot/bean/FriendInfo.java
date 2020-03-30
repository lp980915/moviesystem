package springboot.bean;

import lombok.Data;

import java.util.List;

@Data
public class FriendInfo {
    private List<User> myFans;
    private List<User> myLikely;
    private List<User> myFriend;
}
