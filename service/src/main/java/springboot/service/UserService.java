package springboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import springboot.bean.User;

import java.util.List;

public interface UserService {
    List<User> getList();

    IPage<User> selectPage(Page<User> page, User user);

    User getLoginUser(User user);

    User getUser(User existUser);


    Object updateUser(User user);

    Object addUser(User user);

    Object deleteUser(User user);
}
