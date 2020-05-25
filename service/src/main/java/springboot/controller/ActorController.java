package springboot.controller;

import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springboot.bean.Actor;
import springboot.service.ActorService;
import springboot.token.annotation.UserLoginToken;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/actor")
public class ActorController extends ApiController {
    @Autowired
    private ActorService actorService;

    @UserLoginToken
    @GetMapping("/getActorList")
    public R getActorList(Page page, Actor actor){return success(actorService.getActorList(page,actor));}

    @UserLoginToken
    @PostMapping("/updateActor")
    public R updateActor(Actor actor, MultipartFile file, HttpServletRequest req){return success(actorService.updateActor(actor,file,req));}

    @UserLoginToken
    @GetMapping("/deleteActor")
    public R deleteActor(Actor actor){return success(actorService.deleteActor(actor));}

    @UserLoginToken
    @PostMapping("/addActor")
    public R addActor(Actor actor,MultipartFile file,HttpServletRequest req){return success(actorService.addActor(actor,file,req));}
}
