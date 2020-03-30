package springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.multipart.MultipartFile;
import springboot.bean.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CustomerService {
    Object login(User user, HttpServletResponse response);

    Object findPassword(User user);

    Object getFriend(String userid);

    Object uploadUserImg(MultipartFile file, HttpServletRequest req,User user);

    Object getMovieInfoById(String movieid, String userid, Page page);

    Object collectMovie(String userid, String movieid);

    Object removeCollect(String userid, String movieid);

    Object addCommentLike(String commentid);

    Object addComment(Comment comment);

    Object getTopComment();

    Object search(String moviename, String actorname);

    Object getActorInfo(String actorid);

    Object getMovieOrder(String order,Page page);

    Object getAllMovieAndCountryAndSort(String moviesort,String moviecountry);

    Object getUserInfo(String userid);

    Object addMessage(Message message);

    Object addLikely(String userid, String likelyUserid);

    Object removeLikely(String userid, String likelyUserid);

    Object getTopicList(Page page);

    Object getTopicContent(String topicid);

    Object addTopicMessageReply(TopicMessageReply topicMessageReply);

    Object addTopic(Topic topic);

    Object addTopicMessage(TopicMessage topicMessage);

    Object deleteTopic(String topicid);

    Object deleteReply(String topicmessagereplydate);

    Object deleteTopicMessage(String topicmessageid);

    Object deleteMessage(Message message);
}
