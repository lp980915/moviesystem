package springboot.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String myuserid;
    private String otheruserid;
    private String username;
    private String messagedate;
    private String message;
    private String userimg;
}
