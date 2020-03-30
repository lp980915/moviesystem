package springboot.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.injector.methods.SelectPage;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import springboot.bean.Actor;

@Repository
public interface ActorDao {
    IPage selectPage(@Param("page")IPage page, @Param(Constants.WRAPPER) Wrapper queryWrapper);

    int updateActor(Actor actor);

    int deleteActor(Actor actor);

    int addActor(Actor actor);
}
