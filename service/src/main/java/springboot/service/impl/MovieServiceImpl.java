package springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import springboot.bean.*;
import springboot.dao.*;
import springboot.service.MovieService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Service
@Transactional
public class MovieServiceImpl implements MovieService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private MovieDao movieDao;
    @Autowired
    private SortDao sortDao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private CustomerDao customerDao;

    @Transactional(readOnly = true)
    @Override
    public IPage selectPage(Page<Movie> page, MovieSort movieSort) {
        //如果没有条件，就只做简单的查询
        if(StringUtils.isEmpty(movieSort.getMoviename())){
            IPage ipage=movieDao.selectPage(page,new QueryWrapper<MovieSort>());
            for(MovieSort movieSort1:(List<MovieSort>)ipage.getRecords()){
                movieSort1.setMoviesort(movieDao.getSortByMovieid(movieSort1))
                .setMoviecountry(movieDao.getCountryByMovieid(movieSort1))
                .setActorname(movieDao.getMovieActorByMovieid(movieSort1));
            }
            return ipage;
        }else {
            IPage ipage=movieDao.selectPage(page,new QueryWrapper<MovieSort>().like("moviename", movieSort.getMoviename()));
            for(MovieSort movieSort1:(List<MovieSort>)ipage.getRecords()){
                movieSort1.setMoviesort(movieDao.getSortByMovieid(movieSort1))
                .setMoviecountry(movieDao.getCountryByMovieid(movieSort1))
                .setActorname(movieDao.getMovieActorByMovieid(movieSort1));
            }
            return ipage;
        }
    }

    @Override
    public Object updateMovie(MovieSort movieSort, MultipartFile file,String selectedactorid,HttpServletRequest req) {
        if(null == file) {
            int result=movieDao.updateMovieById(movieSort);
            int deleteResult=0;
            int sortResult=0;
            int deleteResult1=0;
                if(!movieSort.getMoviesort().isEmpty()) {
                    deleteResult=movieDao.deleteMovieSortById(movieSort.getMovieid());
                    sortResult = movieDao.addMovieSortById(movieSort.getMoviesort(),movieSort.getMovieid(),movieSort.getMoviecountry());
                }else{
                    deleteResult=movieDao.deleteMovieSortById(movieSort.getMovieid());
                }
                if(!StringUtils.isEmpty(selectedactorid)){
                    List<String> actoridList = Arrays.asList(selectedactorid.split(","));
                    deleteResult1=movieDao.deleteMovieActor(movieSort.getMovieid());
                    for(String actorid:actoridList){
                        if(!StringUtils.isEmpty(actorid)){
                            movieDao.addMovieActor(actorid,movieSort.getMovieid());
                        }
                    }
                }

            Map<String,Object> map=new HashMap<String, Object>();
            map.put("result",result>0&&sortResult>0);
            return map;
        }


        String realPath = req.getServletContext().getRealPath("/images/movie/");

        File folder = new File(realPath);
        File copyFile=new File("service/src/main/resources/static/images/movie/");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String oldName = file.getOriginalFilename();
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
        File imgTomcatPath = new File(folder,newName);
        File localPathFile=new File(copyFile.getAbsolutePath()+File.separator+ newName);
        System.out.println("真实路径"+localPathFile.toPath());
        try {
            file.transferTo(imgTomcatPath);
            Files.copy(imgTomcatPath.toPath(), localPathFile.toPath());
        }catch (IOException e1) {

            e1.printStackTrace();
        }
        String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/images/movie/"+ newName;
        System.out.println("url是"+url);
        movieSort.setMovieimg(url);
        int result=movieDao.updateById(movieSort);
        int deleteResult=0;
        int sortResult=0;
        int deleteResult1=0;
        if(!movieSort.getMoviesort().isEmpty()) {
             deleteResult=movieDao.deleteMovieSortById(movieSort.getMovieid());
             sortResult = movieDao.addMovieSortById(movieSort.getMoviesort(),movieSort.getMovieid(),movieSort.getMoviecountry());
        }else{
            deleteResult=movieDao.deleteMovieSortById(movieSort.getMovieid());
        }
        if(!StringUtils.isEmpty(selectedactorid)){
            List<String> actornameList = Arrays.asList(selectedactorid.split(","));
            System.out.println(actornameList);
            deleteResult1=movieDao.deleteMovieActor(movieSort.getMovieid());
            for(String actorname:actornameList){
                if(!StringUtils.isEmpty(actornameList)){
                    String actorid=movieDao.getActorIdByActorName(actorname);
                    movieDao.addMovieActor(actorid,movieSort.getMovieid());
                }
            }
        }
        Map<String,Object> map=new HashMap<String, Object>();
       map.put("result",result>0&&sortResult>0);
       map.put("url",url);
        return map;
    }

    @Override
    public Object deleteMovie(Movie movie) {
        Map<String,Object> map=new HashMap<String, Object>();
        System.out.println("userid是"+movie.getMovieid());
        int result=movieDao.deleteById(movie.getMovieid());
        map.put("result",result>0);
        return map;
    }

    @Override
    public Object getSortList() {
        List<String> sortList=movieDao.getSortList();
        return sortList;
    }

    @Override
    public Object getCountryList() {
        return movieDao.getCountryList();
    }

    @Override
    public Object addMovie(MultipartFile file, Movie movie, String moviesort,String moviecountry,String selectedactorid,HttpServletRequest req) {
        if(null == file) {
            return "notFile";
        }

        String realPath = req.getServletContext().getRealPath("/images/movie/");

        File folder = new File(realPath);
        File copyFile=new File("service/src/main/resources/static/images/movie/");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String oldName = file.getOriginalFilename();
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
        File imgTomcatPath = new File(folder,newName);
        File localPathFile=new File(copyFile.getAbsolutePath()+File.separator+ newName);
        System.out.println("真实路径"+localPathFile.toPath());
        try {
            file.transferTo(imgTomcatPath);
            Files.copy(imgTomcatPath.toPath(), localPathFile.toPath());
        }catch (IOException e1) {
            e1.printStackTrace();
        }
        String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/images/movie/"+ newName;
        System.out.println("url是"+url);
        movie.setMovieimg(url);
        Integer result=movieDao.addMovie(movie);
        System.out.println(movie.toString());
        List<String> moviesortList = Arrays.asList(moviesort.split(","));
        List<String> moviecountryList = Arrays.asList(moviecountry.split(","));
        List<String> actoridList = Arrays.asList(selectedactorid.split(","));
       for(String sort:moviesortList) {
           for(String country:moviecountryList) {
               movieDao.addMovieSort(sort,country,movie);
           }
        }
       for(String actorid:actoridList){
           if(!StringUtils.isEmpty(actorid)){
               movieDao.addMovieActor(actorid,movie.getMovieid());
           }
       }
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("result",result>0);
        map.put("url",url);
        return map;
    }

    @Transactional(readOnly = true)
    @Override
    public Object getSortAndMovie(Page page) {
     IPage ipage=sortDao.selectPage(page,new QueryWrapper());
        for(SortAndMovie SAM:(List<SortAndMovie>)ipage.getRecords()){
            SAM.setMovieNum(movieDao.getMovieNumBySort(SAM))
            .setMovieList(movieDao.getMovieListBySort(SAM));
        }
        return ipage;
    }

    @Override
    public Object addSort(String moviesort) {
        int result=movieDao.addSort(moviesort);
        return result>0;
    }

    @Override
    public Object updateSort(String moviesort, String oldMovieSort) {
        int result=movieDao.updateSort(moviesort,oldMovieSort);
        return result>0;
    }

    @Override
    public Object deleteSort(String moviesort) {
        movieDao.deleteSortDependency(moviesort);
        int result=movieDao.deleteSort(moviesort);
        return result>0;
    }

    @Transactional(readOnly = true)
    @Override
    public Object getCommentList(Page page, Comment comment) {
     if(!StringUtils.isEmpty(comment.getComment())){
        IPage iPage=commentDao.selectPage(page,new QueryWrapper<Comment>().like("comment",comment.getComment()));
         for (Comment comment1:(List<Comment>)iPage.getRecords()){
             comment1.setUsername(userDao.getUsernameById(comment1.getUserid()))
             .setMoviename(movieDao.getMovienameById(comment1.getMovieid()));
         }
         return iPage;
     }
     else {
         IPage iPage=commentDao.selectPage(page,new QueryWrapper<Comment>());
         for (Comment comment1:(List<Comment>)iPage.getRecords()){
             comment1.setUsername(userDao.getUsernameById(comment1.getUserid()))
             .setMoviename(movieDao.getMovienameById(comment1.getMovieid()));
         }
         return iPage;
     }

    }

    @Override
    public Object deleteComment(Comment comment) {
        int result=movieDao.deleteComment(comment);
        int result1=0;
        if(comment.getScore()==5){
            result1=customerDao.removeFiveStar(comment);
        }if(comment.getScore()==4){
            result1=customerDao.removeFourStar(comment);
        }if(comment.getScore()==3){
            result1=customerDao.removeThreeStar(comment);
        }if(comment.getScore()==2){
            result1=customerDao.removeTwoStar(comment);
        }if(comment.getScore()==1){
            result1=customerDao.removeOneStar(comment);
        }
        return result>0&&result1>0;
    }


}
