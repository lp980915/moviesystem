package springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import springboot.bean.User;
import springboot.dao.UserDao;
import springboot.service.UserService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public List<User> getList() {
        return userDao.getList();
    }

    @Transactional(readOnly = true)
    @Override
    public IPage<User> selectPage(Page<User> page, User user) {
      //如果没有条件，就只做简单的查询
      if(StringUtils.isEmpty(user.getUsername())){
          return userDao.selectPage(page,new QueryWrapper<User>());
      }else {
          System.out.println("姓名为" + user.getUsername());
          return userDao.selectPage(page, new QueryWrapper<User>().like("username", user.getUsername()));
      }

    }

    @Override
    public User getLoginUser(User user) {
        return userDao.getLoginUser(user);
    }

    @Override
    public User getUser(User existUser) {
        return userDao.getUser(existUser);
    }



    @Override
    public Object updateUser(User user) {
        Map<String,Object> map=new HashMap<String, Object>();
        Integer result=userDao.updateUser(user);
        User newUser=userDao.getUser(user);
        map.put("result",result>0);
        map.put("newUser",newUser);
        return map;
    }

    @Override
    public Object addUser(User user) {
        Map<String,Object> map=new HashMap<String, Object>();
        int result=0;
        if(userDao.existUsername(user)==0) {
            result = userDao.insert(user);
        }
        map.put("result",result>0);
        return map;
    }

    @Override
    public Object deleteUser(User user) {
        Map<String,Object> map=new HashMap<String, Object>();
        System.out.println("userid是"+user.getUserid());
        int result=userDao.deleteById(user.getUserid());
        map.put("result",result>0);
        return map;
    }


}
