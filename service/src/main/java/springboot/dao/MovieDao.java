package springboot.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import springboot.bean.*;

import java.io.Serializable;
import java.util.List;

@Repository
public interface MovieDao extends BaseMapper {

    IPage selectPage(@Param("page")IPage page,@Param(Constants.WRAPPER) Wrapper queryWrapper);

    @Override
    int updateById(Object entity);

    @Override
    int deleteById(Serializable id);

    List<String> getSortList();

 

    int addMovieSort(@Param("sort") String sort,@Param("country") String country, @Param("movie") Movie movie);

    Integer addMovie(Movie movie);

    List<String> getSortByMovieid(MovieSort movieSort1);

    int addMovieSortById(@Param("list") List<String> movieSort,@Param("movieid") String movieid,@Param("moviecountry") String moviecountry);

    int deleteMovieSortById(String movieid);

    List<Report> getTopMovie();

    String getCollectNumByMovieid(Report report);

    String getCommentNumByMovieid(Report report);

    List<SortAndMovie> getSortListToMovie();

    String getMovieNumBySort(SortAndMovie sam);

    List<String> getMovieListBySort(SortAndMovie sam);

    int addSort(String moviesort);

    int updateSort(@Param("moviesort") String moviesort, @Param("oldMovieSort") String oldMovieSort);

    void deleteSortDependency(String moviesort);

    int deleteSort(String moviesort);

    String getMovienameById(String movieid);

    int deleteComment(Comment comment);

    String getMoviescoreByMovieid(Report report);

    List<String> getCountryList();

    String getCountryByMovieid(MovieSort movieSort1);

    List<Movie> getMovieList(@Param("moviesort")String moviesort,@Param("moviecountry")String moviecountry);

    void addMovieActor(@Param("actorid") String actorid, @Param("movieid") String movieid);

    List<String> getMovieActorByMovieid(MovieSort movieSort1);

    int deleteMovieActor(String movieid);

    int updateMovieById(MovieSort movieSort);

    String getActorIdByActorName(String actorname);
}
