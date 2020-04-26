package springboot.bean;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class MovieInfo {
    private Movie movie;
    private IPage<Comment> commentList;
    private String moviecountry;
    private List<String> moviesort;
    private List<Actor> movieactor;
    private Boolean existCollect;
    private List<Topic> topicList;
}
