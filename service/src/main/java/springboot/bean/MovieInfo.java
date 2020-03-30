package springboot.bean;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

@Data
public class MovieInfo {
    private Movie movie;
    private IPage<Comment> commentList;
    private String moviecountry;
    private List<String> moviesort;
    private List<Actor> movieactor;
    private Boolean existCollect;
    private List<Topic> topicList;
}
