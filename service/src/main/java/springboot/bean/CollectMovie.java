package springboot.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class CollectMovie {
    private String movieid;
    private String movieimg;
    private String moviename;
    private String movieintro;
    private String movielike;
    private String scoreperson;
    private String moviescore;
    private String onestar;
    private String twostar;
    private String threestar;
    private String fourstar;
    private String fivestar;
    private String date;
    private String collectdate;
}
