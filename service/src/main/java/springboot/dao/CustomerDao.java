package springboot.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.SelectPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import springboot.bean.*;

import java.util.List;

@Repository
public interface CustomerDao extends BaseMapper {
    int findPassword(User user);

    List<String> getFans(String userid);

    List<String> getLikely(String userid);

    int uploadUserImg(User user);

    Movie getMovieById(String movieid);

    List<Comment> getCommentListByMovieId(String movieid);

    List<String> getMoviesortByMovieId(String movieid);

    List<Actor> getActorListByMovieId(String movieid);

    int addMovieLike(String movieid);

    int collectMovie(@Param("userid") String userid, @Param("movieid") String movieid);

    int existCollect(@Param("userid") String userid, @Param("movieid") String movieid);

    int removeMovieLike(String movieid);

    int removeCollect(String userid, String movieid);

    int addCommentLike(String commentid);

    int addOneStar(Comment comment);

    int addTwoStar(Comment comment);

    int addThreeStar(Comment comment);

    int addFourStar(Comment comment);

    int addFiveStar(Comment comment);

    int addComment(Comment comment);

    List<Comment> getTopComment();

    List<MovieSort> searchMovie(String actorname);

    List<Actor> searchActor(String moviename);

    Actor getActorById(String actorid);

    List<Movie> getMovieListByActorId(String actorid);

    String getMovieCountryByMovieId(String movieid);

    IPage<Comment> selectPageComment(Page page, String movieid);

    IPage<MovieSort> getTopHotMovie(Page page);

    IPage<MovieSort> getTopScoreMovie(Page page);

    IPage<MovieSort> getTopDateMovie(Page page);

    List<CollectMovie> getCollectMovieListById(String userid);

    List<Comment> getCommentListByUserId(String userid);

    List<User> getFansInfo(String userid);

    List<User> getLikelyInfo(String userid);

    List<Message> getMessageListById(String userid);

    int addMessage(Message message);

    int addLikely(String userid, String likelyUserid);

    int removeLikely(String userid, String likelyUserid);

    int removeFiveStar(Comment comment);

    int removeFourStar(Comment comment);

    int removeThreeStar(Comment comment);

    int removeTwoStar(Comment comment);

    int removeOneStar(Comment comment);

    IPage<Topic> selectPageTopic(Page page);

    List<TopicMessage> getTopMessageListByTopicId(String topicid);

    List<TopicMessageReply> getTopicMessageReplyListByTopicId(String topicid);

    String getToUserNameByToUserId(String touserid);

    int addTopicMessageReply(TopicMessageReply topicMessageReply);

    int addTopic(Topic topic);

    List<Topic> getTopicListByMovieId(String movieid);

    int addTopicMessage(TopicMessage topicMessage);

    int deleteTopic(String topicid);

    int deleteReply(String topicmessagereplydate);

    int deleteTopicMessage(String topicmessageid);

    List<Topic> getTopicListByUserId(String userid);

    int deleteMessage(Message message);
}
