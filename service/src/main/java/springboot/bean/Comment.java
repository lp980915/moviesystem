package springboot.bean;

import lombok.Data;

@Data
public class Comment {
    private String commentid;
    private String comment;
    private String movieid;
    private String userid;
    private int likevalue;
    private String date;
    private int score;
    private String moviename;
    private String username;
    private String userimg;
    private String movieimg;
}
