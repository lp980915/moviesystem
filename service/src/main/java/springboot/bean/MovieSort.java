package springboot.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class MovieSort {
    private String movieid;
    private String movieimg;
    private String moviename;
    private String movieintro;
    private int movielike;
    private String scoreperson;
    private String moviescore;
    private String onestar;
    private String twostar;
    private String threestar;
    private String fourstar;
    private String fivestar;
    private String date;
    private List<String> moviesort;
    private String moviecountry;
    private List<String> actorname;
}
