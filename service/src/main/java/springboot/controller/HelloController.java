package springboot.controller;

import springboot.bean.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.token.annotation.UserLoginToken;

@RestController
@RequestMapping("/api/hello")
public class HelloController {

    @UserLoginToken
    @RequestMapping("/getReq")
    public String getReq(String id){
        System.out.println(id);
        return "Hello,Success";
    }


    @RequestMapping("/postReq")
    public Object postReq(User user){
        System.out.println(user.toString());
        return user;
    }
}
