package springboot.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
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
