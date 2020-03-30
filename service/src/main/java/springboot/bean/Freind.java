package springboot.bean;

import lombok.Data;

import java.util.List;

@Data
public class Freind {
    private List<String> myFans;
    private List<String> myLikely;
    private List<String> myFriend;
}
