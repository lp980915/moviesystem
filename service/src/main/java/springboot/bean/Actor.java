package springboot.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Actor {
    private String actorid;
    private String actorname;
    private String actorimg;
    private String actordetails;
    private String actorreward;
    private String position;
}
