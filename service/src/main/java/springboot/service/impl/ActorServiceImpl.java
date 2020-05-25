package springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import springboot.bean.Actor;
import springboot.dao.ActorDao;
import springboot.service.ActorService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@Service
@Transactional
public class ActorServiceImpl implements ActorService {
    @Autowired
    private ActorDao actorDao;

    @Transactional(readOnly = true)
    @Override
    public Object getActorList(Page page, Actor actor) {
        if(StringUtils.isEmpty(actor.getActorname())){
            return actorDao.selectPage(page,new QueryWrapper<Actor>());
        }else{
            return actorDao.selectPage(page,new QueryWrapper<Actor>().like("actorname",actor.getActorname()));
        }
    }

    @Override
    public Object updateActor(Actor actor, MultipartFile file, HttpServletRequest req) {
        if(null == file) {
            int result=actorDao.updateActor(actor);
            return result>0;
        }

        String realPath = req.getServletContext().getRealPath("/images/actor/");

        File folder = new File(realPath);
        File copyFile=new File("service/src/main/resources/static/images/actor/");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String oldName = file.getOriginalFilename();
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
        File imgTomcatPath = new File(folder,newName);
        File localPathFile=new File(copyFile.getAbsolutePath()+File.separator+ newName);
        System.out.println("真实路径"+localPathFile.toPath());
        try {
            file.transferTo(imgTomcatPath);
            Files.copy(imgTomcatPath.toPath(), localPathFile.toPath());
        }catch (IOException e1) {
            e1.printStackTrace();
        }
        String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/images/actor/"+ newName;
        System.out.println("url是"+url);
        actor.setActorimg(url);
        int result=actorDao.updateActor(actor);
        return result>0;
    }

    @Override
    public Object deleteActor(Actor actor) {
        int result=actorDao.deleteActor(actor);
        return result>0;
    }

    @Override
    public Object addActor(Actor actor, MultipartFile file,HttpServletRequest req) {
        if(null==file){
            int result=actorDao.addActor(actor);
            return result>0;
        }else {
            String realPath = req.getServletContext().getRealPath("/images/actor/");

            File folder = new File(realPath);
            File copyFile=new File("service/src/main/resources/static/images/actor/");
            if (!folder.exists()) {
                folder.mkdirs();
            }
            String oldName = file.getOriginalFilename();
            String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
            File imgTomcatPath = new File(folder,newName);
            File localPathFile=new File(copyFile.getAbsolutePath()+File.separator+ newName);
            System.out.println("真实路径"+localPathFile.toPath());
            try {
                file.transferTo(imgTomcatPath);
                Files.copy(imgTomcatPath.toPath(), localPathFile.toPath());
            }catch (IOException e1) {
                e1.printStackTrace();
            }
            String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/images/actor/"+ newName;
            System.out.println("url是"+url);
            actor.setActorimg(url);
            int result=actorDao.addActor(actor);
            return result>0;
        }
    }
}
