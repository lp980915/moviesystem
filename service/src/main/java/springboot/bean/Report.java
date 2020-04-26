package springboot.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    private String movieid;
    private String movieimg;
    private String moviename;
    private String movieintro;
    private String movielike;
    private String commentNum;
    private String collectNum;
    private String moviescore;
}
