package springboot.bean;

import lombok.Data;

import java.util.List;

@Data
public class ActorInfo {
    private Actor actor;
    private List<Movie> movieList;
}
