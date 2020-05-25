package springboot.controller;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springboot.bean.Comment;
import springboot.bean.Movie;
import springboot.bean.MovieSort;
import springboot.service.MovieService;
import springboot.token.annotation.UserLoginToken;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/movie")
public class MovieController extends ApiController{
    @Autowired
    private MovieService movieService;

    @UserLoginToken
    @GetMapping("/getMovieList")
    public R getMovieList(Page page, MovieSort movieSort){
        System.out.println(movieSort+"+"+page);
        return success(movieService.selectPage(page,movieSort));
    }

    @UserLoginToken
    @PostMapping("/updateMovie")
    public R updateMovie(MovieSort movieSort, MultipartFile file,String selectedactorid,HttpServletRequest req){
        System.out.println("接收到了movieSort:"+movieSort+"file:"+file);

        return success(movieService.updateMovie(movieSort,file,selectedactorid,req));
    }

    @UserLoginToken
    @GetMapping("/deleteMovie")
    public R deleteMovie(Movie movie){
        return success(movieService.deleteMovie(movie));
    }

    @UserLoginToken
    @GetMapping("/getSortList")
    public R getSortList(){
        return success(movieService.getSortList());
    }

    @UserLoginToken
    @GetMapping("/getCountryList")
    public R getCountryList(){return success(movieService.getCountryList());}

    @UserLoginToken
    @PostMapping("/addMovie")
    public R addMovie(MultipartFile file,Movie movie,String moviesort,String moviecountry,String selectedactorid,HttpServletRequest req){
        return success(movieService.addMovie(file,movie,moviesort,moviecountry,selectedactorid,req));
    }

    @UserLoginToken
    @GetMapping("/getSortAndMovie")
    public R getSortAndMovie(Page page){
        return success(movieService.getSortAndMovie(page));
    }

    @UserLoginToken
    @GetMapping("/addSort")
    public R addSort(String moviesort){
        return success(movieService.addSort(moviesort));
    }

    @UserLoginToken
    @GetMapping("/updateSort")
    public R updateSort(String moviesort,String oldMovieSort){ return success(movieService.updateSort(moviesort,oldMovieSort)); }

    @UserLoginToken
    @GetMapping("/deleteSort")
    public R deleteSort(String moviesort){
        return success(movieService.deleteSort(moviesort));
    }

    @UserLoginToken
    @GetMapping("/getCommentList")
    public R getCommentList(Page page, Comment comment){ return success(movieService.getCommentList(page,comment));}

    @UserLoginToken
    @GetMapping("/deleteComment")
    public R deleteComment(Comment comment){return success(movieService.deleteComment(comment));}
}
