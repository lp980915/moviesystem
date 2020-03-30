package springboot.bean;

import lombok.Data;

import java.util.List;

@Data
public class SortAndMovie {
    private String moviesort;
    private String movieNum;
    private List<String> movieList;
}
