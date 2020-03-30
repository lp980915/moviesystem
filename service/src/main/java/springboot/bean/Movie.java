package springboot.bean;

import lombok.Data;

import java.sql.Date;

@Data
public class Movie {
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
}
