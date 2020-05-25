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
import springboot.dao.CustomerDao;
import springboot.dao.MovieDao;
import springboot.dao.UserDao;
import springboot.service.CustomerService;
import springboot.service.UserService;
import springboot.token.TokenService;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MovieDao movieDao;

    @Override
    public Object login(User user, HttpServletResponse response) {
        User loginUser= userService.getLoginUser(user);
        if(loginUser==null){
            return "登陆失败，账号或密码有误!";
        }
        else {
            Map<String,Object> map = new HashMap<String, Object>();
            String token=tokenService.getToken(loginUser);
            map.put("token", token);
            map.put("user",loginUser);
            Cookie cookie=new Cookie("token",token);
            cookie.setPath("/");
            response.addCookie(cookie);
            return map;
        }
    }

    @Override
    public Object findPassword(User user) {
        int result= customerDao.findPassword(user);
        return result>0;
    }

    @Transactional(readOnly = true)
    @Override
    public Object getFriend(String userid) {
        Freind freind=new Freind();
        freind.setMyFans(customerDao.getFans(userid))
        .setMyLikely(customerDao.getLikely(userid));
        List<String> friendList=new ArrayList<>();
        for (String fans:freind.getMyFans()){
            for (String likely:freind.getMyLikely()){
                if(likely.equals(fans)){
                    friendList.add(fans);
                }
            }
        }
        freind.setMyFriend(friendList);
        return freind;
    }

    @Override
    public Object uploadUserImg(MultipartFile file, HttpServletRequest req,User user) {
        if(null == file) {
            return "notFile";
        }
        String realPath = req.getServletContext().getRealPath("/images/userimg/");

        File folder = new File(realPath);
        File copyFile=new File("service/src/main/resources/static/images/userimg/");
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
        String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/images/userimg/"+ newName;
        System.out.println("url是"+url);
        user.setUserimg(url);
        int result=customerDao.uploadUserImg(user);
        User newUser=userDao.getUser(user);
        Map<String,Object> map=new HashMap<>();
        map.put("result",result>0);
        map.put("newUser",newUser);
        return map;
    }

    @Transactional(readOnly = true)
    @Override
    public Object getMovieInfoById(String movieid, String userid, Page page) {
        MovieInfo movieInfo=new MovieInfo(customerDao.getMovieById(movieid),
                customerDao.selectPageComment(page,movieid),
                customerDao.getMovieCountryByMovieId(movieid),
                customerDao.getMoviesortByMovieId(movieid),
                customerDao.getActorListByMovieId(movieid),
                customerDao.existCollect(userid,movieid)>0,
                customerDao.getTopicListByMovieId(movieid));
                return movieInfo;
    }

    @Override
    public Object collectMovie(String userid, String movieid) {
       int result=customerDao.addMovieLike(movieid);
       int result1=customerDao.collectMovie(userid,movieid);
        return result>0&&result1>0;
    }

    @Override
    public Object removeCollect(String userid, String movieid) {
        int result=customerDao.removeMovieLike(movieid);
        int result1=customerDao.removeCollect(userid,movieid);
        return result>0&&result1>0;
    }

    @Override
    public Object addCommentLike(String commentid) {
        int result=customerDao.addCommentLike(commentid);
        return result>0;
    }

    @Override
    public Object addComment(Comment comment) {
        int result=0;
        if(comment.getScore()==1) {
            result = customerDao.addOneStar(comment);
        }else  if(comment.getScore()==2) {
             result = customerDao.addTwoStar(comment);
        }else   if(comment.getScore()==3) {
             result = customerDao.addThreeStar(comment);
        }else  if(comment.getScore()==4) {
             result = customerDao.addFourStar(comment);
        } else {
              result = customerDao.addFiveStar(comment);
        }
        int result1=customerDao.addComment(comment);
        return result>0&&result1>0;
    }

    @Override
    public Object getTopComment() {
        return customerDao.getTopComment();
    }

    @Transactional(readOnly = true)
    @Override
    public Object search(String moviename, String actorname) {
        Map<String,Object> map=new HashMap<>();
            List<MovieSort> movieList=customerDao.searchMovie(actorname);
            for (MovieSort ms:movieList){
                ms.setMoviesort(customerDao.getMoviesortByMovieId(ms.getMovieid()));
            }
            List<Actor> actorList=customerDao.searchActor(moviename);
            map.put("movieList",movieList);
            map.put("actorList",actorList);
            return map;
    }

    @Transactional(readOnly = true)
    @Override
    public Object getActorInfo(String actorid) {
        ActorInfo actorInfo=new ActorInfo();
        actorInfo.setActor(customerDao.getActorById(actorid))
        .setMovieList(customerDao.getMovieListByActorId(actorid));
        return actorInfo;
    }

    @Transactional(readOnly = true)
    @Override
    public Object getMovieOrder(String order,Page page) {
        IPage<MovieSort> ipage = null;
        if ("热度优先".equals(order)) {
            ipage=customerDao.getTopHotMovie(page);
        }else  if ("评价优先".equals(order)) {
              ipage=customerDao.getTopScoreMovie(page);
        }else  if ("时间优先".equals(order)) {
              ipage=customerDao.getTopDateMovie(page);
        }
        for(MovieSort movieSort:ipage.getRecords()){
            movieSort.setMoviesort(customerDao.getMoviesortByMovieId(movieSort.getMovieid()))
            .setMoviecountry(customerDao.getMovieCountryByMovieId(movieSort.getMovieid()));
        }
        return ipage;
    }

    @Transactional(readOnly = true)
    @Override
    public Object getAllMovieAndCountryAndSort(String moviesort,String moviecountry) {
        List<String> sortList=movieDao.getSortList();
        List<String> countryList=movieDao.getCountryList();
        List<Movie> movieList=movieDao.getMovieList(moviesort,moviecountry);
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("sortList",sortList);
        map.put("countryList",countryList);
        map.put("movieList",movieList);
        return map;
    }

    @Transactional(readOnly = true)
    @Override
    public Object getUserInfo(String userid) {
        UserInfo userInfo=new UserInfo();
        User user=new User();
        user.setUserid(userid);
        userInfo.setUser(userDao.getUser(user))
        .setCollectMovieList(customerDao.getCollectMovieListById(userid))
        .setCommentList(customerDao.getCommentListByUserId(userid));
        FriendInfo friendInfo=new FriendInfo();
        friendInfo.setMyFans(customerDao.getFansInfo(userid))
        .setMyLikely(customerDao.getLikelyInfo(userid));
        List<User> friendList=new ArrayList<>();
        for (User fans:friendInfo.getMyFans()){
            for (User likely:friendInfo.getMyLikely()){
                if(likely.getUsername().equals(fans.getUsername())){
                    friendList.add(likely);
                }
            }
        }
        Set<User> set = new HashSet<>(friendList);
        friendList.clear();
        friendList.addAll(set);    //使用hashset为list去重
        friendInfo.setMyFriend(friendList);
        userInfo.setFriendInfo(friendInfo)
        .setMessageList(customerDao.getMessageListById(userid))
        .setTopicList(customerDao.getTopicListByUserId(userid));
        return userInfo;
    }

    @Override
    public Object addMessage(Message message) {
        int result=customerDao.addMessage(message);
        return result>0;
    }

    @Override
    public Object addLikely(String userid, String likelyUserid) {
        int result=customerDao.addLikely(userid,likelyUserid);
        return result>0;
    }

    @Override
    public Object removeLikely(String userid, String likelyUserid) {
        int result=customerDao.removeLikely(userid,likelyUserid);
        return result>0;
    }

    @Override
    public Object getTopicList(Page page) {
        return customerDao.selectPageTopic(page);
    }

    @Transactional(readOnly = true)
    @Override
    public Object getTopicContent(String topicid) {
        TopicContent topicContent=new TopicContent();
        topicContent.setTopicMessageList(customerDao.getTopMessageListByTopicId(topicid));
        List<TopicMessageReply> topicMessageReplyList=customerDao.getTopicMessageReplyListByTopicId(topicid);
        for(TopicMessageReply topicMessageReply:topicMessageReplyList){
            topicMessageReply.setTousername(customerDao.getToUserNameByToUserId(topicMessageReply.getTouserid()));
        }
        topicContent.setTopicMessageReplyList(topicMessageReplyList);
        return topicContent;
    }

    @Override
    public Object addTopicMessageReply(TopicMessageReply topicMessageReply) {
        int result=customerDao.addTopicMessageReply(topicMessageReply);
        return result>0;
    }

    @Override
    public Object addTopic(Topic topic) {
        int result=customerDao.addTopic(topic);
        return result>0;
    }

    @Override
    public Object addTopicMessage(TopicMessage topicMessage) {
        int result=customerDao.addTopicMessage(topicMessage);
        return result>0;
    }

    @Override
    public Object deleteTopic(String topicid) {
        int result=customerDao.deleteTopic(topicid);
        return result>0;
    }

    @Override
    public Object deleteReply(String topicmessagereplydate) {
        int result=customerDao.deleteReply(topicmessagereplydate);
        return result>0;
    }

    @Override
    public Object deleteTopicMessage(String topicmessageid) {
        int result=customerDao.deleteTopicMessage(topicmessageid);
        return result>0;
    }

    @Override
    public Object deleteMessage(Message message) {
        int result=customerDao.deleteMessage(message);
        return result>0;
    }
}
