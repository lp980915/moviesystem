package springboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.multipart.MultipartFile;
import springboot.bean.Comment;
import springboot.bean.Movie;
import springboot.bean.MovieSort;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface MovieService {
    IPage selectPage(Page<Movie> page, MovieSort movieSort);

    Object updateMovie(MovieSort movieSort, MultipartFile file,String selectedactorid,HttpServletRequest req);

    Object deleteMovie(Movie movie);

    Object getSortList();

    Object getCountryList();

    Object addMovie(MultipartFile file, Movie movie, String moviesort,String moviecountry,String selectedactorid, HttpServletRequest req);


    Object getSortAndMovie(Page page);

    Object addSort(String moviesort);

    Object updateSort(String moviesort, String oldMovieSort);

    Object deleteSort(String moviesort);

    Object getCommentList(Page page, Comment comment);

    Object deleteComment(Comment comment);

}
