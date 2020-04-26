package springboot.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Topic {
    private String topicid;
    private String movieid;
    private String moviename;
    private String movieimg;
    private String userid;
    private String username;
    private String userimg;
    private String title;
    private String describe;
    private String topicdate;
}
