package springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.multipart.MultipartFile;
import springboot.bean.Actor;

import javax.servlet.http.HttpServletRequest;

public interface ActorService {
    Object getActorList(Page page, Actor actor);

    Object updateActor(Actor actor, MultipartFile file, HttpServletRequest req);

    Object deleteActor(Actor actor);

    Object addActor(Actor actor, MultipartFile file,HttpServletRequest req);
}
