package springboot.controller;

import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springboot.bean.*;
import springboot.service.CustomerService;
import springboot.service.UserService;
import springboot.token.annotation.UserLoginToken;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/customer")
public class CustomerController extends ApiController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R login(User user, HttpServletResponse response){
        return success(customerService.login(user,response));
    }

    @PostMapping("/addCustomer")
    public R addCustomer(User user){return success(userService.addUser(user));}

    @PostMapping("/findPassword")
    public R findPassword(User user){return success(customerService.findPassword(user));}

    @UserLoginToken
    @GetMapping("/getFriend")
    public R getFriend(String userid){return success(customerService.getFriend(userid));}

    /*@CrossOrigin(origins = {"http://www.ljcblog.top:8088", "null"})*/
    @CrossOrigin(origins = {"http://localhost:8082", "null"})
    //@UserLoginToken
    @PostMapping("/uploadUserImg")
    public R uploadUserImg(MultipartFile file, HttpServletRequest req,User user){ return success(customerService.uploadUserImg(file,req,user)); }

    @UserLoginToken
    @GetMapping("/getMovieInfoById")
    public R getMovieInfoById(String movieid, String userid, Page page){return success(customerService.getMovieInfoById(movieid,userid,page));}

    @UserLoginToken
    @PostMapping("/collectMovie")
    public R collectMovie(String userid,String movieid){return success(customerService.collectMovie(userid,movieid));}

    @UserLoginToken
    @PostMapping("/removeCollect")
    public R removeCollect(String userid,String movieid){return success(customerService.removeCollect(userid,movieid));}

    @UserLoginToken
    @PostMapping("/addCommentLike")
    public R addCommentLike(String commentid){
        return success(customerService.addCommentLike(commentid));
    }

    @UserLoginToken
    @PostMapping("/addComment")
    public R addComment(Comment comment){return success(customerService.addComment(comment));}

    @UserLoginToken
    @GetMapping("/getTopComment")
    public R getTopComment(){return success(customerService.getTopComment());}

    @UserLoginToken
    @GetMapping("/search")
    public R search(String moviename,String actorname){return success(customerService.search(moviename,actorname));}

    @UserLoginToken
    @GetMapping("/getActorInfo")
    public R getActorInfo(String actorid){return success(customerService.getActorInfo(actorid));}

    @UserLoginToken
    @GetMapping("/getMovieOrder")
    public R getMovieOrder(String order,Page page){return success(customerService.getMovieOrder(order,page));}

    @UserLoginToken
    @GetMapping("/getAllMovieAndCountryAndSort")
    public R getAllMovieAndCountryAndSort(String moviesort,String moviecountry){return success(customerService.getAllMovieAndCountryAndSort(moviesort,moviecountry));}

    @UserLoginToken
    @GetMapping("/getUserInfo")
    public R getUserInfo(String userid){return success(customerService.getUserInfo(userid));}

    @UserLoginToken
    @PostMapping("/addMessage")
    public R addMessage(Message message){return success(customerService.addMessage(message));}

    @UserLoginToken
    @GetMapping("/addLikely")
    public R addLikely(String userid,String likelyUserid){return success(customerService.addLikely(userid,likelyUserid));}

    @UserLoginToken
    @GetMapping("/removeLikely")
    public R removeLikely(String userid,String likelyUserid){return success(customerService.removeLikely(userid,likelyUserid));}

    @UserLoginToken
    @GetMapping("/getTopicList")
    public R getTopicList(Page page){return success(customerService.getTopicList(page));}

    @UserLoginToken
    @GetMapping("/getTopicContent")
    public R getTopicContent(String topicid){return success(customerService.getTopicContent(topicid));}

    @UserLoginToken
    @PostMapping("/addTopicMessageReply")
    public R addTopicMessageReply(TopicMessageReply topicMessageReply){return success(customerService.addTopicMessageReply(topicMessageReply)); }

    @UserLoginToken
    @PostMapping("/addTopic")
    public R addTopic(Topic topic){return success(customerService.addTopic(topic));}

    @UserLoginToken
    @PostMapping("/addTopicMessage")
    public R addTopicMessage(TopicMessage topicMessage){return success(customerService.addTopicMessage(topicMessage));}

    @UserLoginToken
    @GetMapping("/deleteTopic")
    public R deleteTopic(String topicid){return success(customerService.deleteTopic(topicid));}

    @UserLoginToken
    @GetMapping("/deleteReply")
    public R deleteReply(String topicmessagereplydate){return success(customerService.deleteReply(topicmessagereplydate));}

    @UserLoginToken
    @GetMapping("/deleteTopicMessage")
    public R deleteTopicMessage(String topicmessageid){return success(customerService.deleteTopicMessage(topicmessageid));}

    @UserLoginToken
    @GetMapping("/deleteMessage")
    public R deleteMessage(Message message){return success(customerService.deleteMessage(message));}
}
