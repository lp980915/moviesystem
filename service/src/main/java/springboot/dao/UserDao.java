package springboot.dao;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import springboot.bean.User;

import java.io.Serializable;
import java.util.List;

/**
 * @author ljc
 */
@Repository
public interface UserDao extends BaseMapper {

    List<User> getList();
   //Constants.WRAPPER的值在映射文件中用ew表示
    IPage<User> selectPage(@Param("page") IPage page,@Param(Constants.WRAPPER) QueryWrapper<User> wrapper);

    User getLoginUser(User user);

    User getUser(User existUser);

    Integer updateUser(User user);


    @Override
    int insert(@Param(Constants.WRAPPER) Object entity);

    int existUsername(User user);

    @Override
    int deleteById(Serializable id);

    String getUsernameById(String userid);
}