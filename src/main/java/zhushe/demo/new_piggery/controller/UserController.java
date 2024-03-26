package zhushe.demo.new_piggery.controller;

import cn.hutool.Hutool;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zhushe.demo.new_piggery.common.R;
import zhushe.demo.new_piggery.entity.Air;
import zhushe.demo.new_piggery.entity.User;
import zhushe.demo.new_piggery.service.UserService;
import zhushe.demo.new_piggery.utils.TokenUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/user")
@RestController
@CrossOrigin
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R<User> login(HttpServletRequest request,@RequestBody User user){
        if (StrUtil.isBlankIfStr(user.getUsername()) || StrUtil.isBlankIfStr(user.getPassword())){
            return R.error("500","账号或密码不可为空");
        }
        //登录成功，将用户id放入session
        request.getSession().setAttribute("currentUserId",user.getId());
        return R.success(userService.login(user));
    }

    @PostMapping("/register")
    public R<User> register(@RequestBody User user){
        //本地localhost  部署47.113.178.187
        user.setAvatar(" http://localhost:8080/file/download/default_avatar.jpg");
        if (StrUtil.isBlankIfStr(user.getUsername()) || StrUtil.isBlankIfStr(user.getPassword())|| StrUtil.isBlankIfStr(user.getRole())){
            return R.error("500","账号或密码不可为空");
        }
        return R.success(userService.register(user));
    }

    @GetMapping("/list")
    public  R<List<User>> list(){
      return R.success(userService.list());
    }



    //十分得有九分不对劲啊，这个设置token应该得再加个判断吧或者不要在这搞token
    @PostMapping("/update")
    public R<User> update(@RequestBody User user){
        log.info(user.toString());
        String originalToken = user.getToken();
        user.setToken(null);//将token置空不存进数据库
        if (userService.updateById(user)){
            User saved_user = userService.getById(user.getId());
            saved_user.setToken(originalToken);
            return R.success(saved_user);
        }
        else{
            return R.error("500","保存失败");
        }
    }


}
