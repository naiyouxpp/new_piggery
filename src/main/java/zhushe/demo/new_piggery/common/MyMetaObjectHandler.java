package zhushe.demo.new_piggery.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/*
* 自定义的元数据对象处理器
* */

@Component//不加则没发进入Handler，交给spring管理
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    /*
    * 新增的时候执行
    *
    * metaObject封装了元数据，包括前端传的employee数据
    * */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充[insert]");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }
    /*
    * 更新的时候执行
    * */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充[update]");
        log.info(metaObject.toString());

        //通过JDK提供的类获取线程id
        Long id =Thread.currentThread().getId();
        log.info("线程id为:{}",id);

        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }

    //mybatis-plus是如何判断该用updateFill还是insertFill的呢？
    //根据sql语句，如果是insert语句则使用insertFill，有update关键字则使用updateFill
}
