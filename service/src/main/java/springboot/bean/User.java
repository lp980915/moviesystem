package springboot.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String  userid;
    private String rowid;
    private String username;
    private String password;
    private String realname;
    private String number;
    private String email;
    private String userimg;
    private String autograph;
}
