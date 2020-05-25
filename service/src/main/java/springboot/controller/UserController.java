package springboot.controller;

import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.bean.User;
import springboot.service.UserService;
import springboot.token.TokenService;
import springboot.token.TokenUtil;
import springboot.token.annotation.UserLoginToken;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController extends ApiController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;


    @RequestMapping("/getList")     //分页list
    public R getList(User user, Page page){
        System.out.println("传来的"+page.getCurrent()+"/"+page.getSize());
        List<User> list= userService.getList();
        return success(userService.selectPage(page,user));
    }


    @PostMapping("/login")
    public Object login(User user, HttpServletResponse response){
        Map<String,Object> map = new HashMap<String, Object>();
     User loginUser= userService.getLoginUser(user);
     if(loginUser==null|| "0".equals(loginUser.getRowid())){
         map.put("message", "登录失败,账号或密码有误");
         System.out.println("jsonobject为"+map);
         return map.toString();
     }else {
         String token=tokenService.getToken(loginUser);
         map.put("token", token);
         map.put("user",loginUser);
         Cookie cookie=new Cookie("token",token);
         cookie.setPath("/");
         response.addCookie(cookie);
         System.out.println("jsonobject为"+map);
         return map;
     }

    }

    /***
     * 这个请求需要验证token才能访问
     */
    @UserLoginToken
    @GetMapping("/getMessage")
    public String getMessage() {

        // 取出token中带的用户id 进行操作
        System.out.println("您的id为"+TokenUtil.getTokenUserId());

        return "你已通过验证";
    }

    @UserLoginToken
    @GetMapping("/getUserList")
    public R getUserList(Page page,User user){
        return success(userService.selectPage(page,user));
    }

    @UserLoginToken
    @PostMapping("/updateUser")
    public R updateUser(User user){
        return success(userService.updateUser(user));
    }
    @UserLoginToken
    @PostMapping("/addUser")
    public R addUser(User user){
        return success(userService.addUser(user));
    }

    @UserLoginToken
    @GetMapping("/deleteUser")
    public R deleteUser(User user){
        return success(userService.deleteUser(user));
    }

}

