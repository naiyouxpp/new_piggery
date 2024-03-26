package zhushe.demo.new_piggery.service.serviceImpl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import zhushe.demo.new_piggery.entity.User;
import zhushe.demo.new_piggery.exception.ServiceException;
import zhushe.demo.new_piggery.mapper.UserMapper;
import zhushe.demo.new_piggery.service.UserService;
import zhushe.demo.new_piggery.utils.TokenUtils;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User login(User user) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername,user.getUsername());
        User one = this.getOne(lambdaQueryWrapper);
        //找不到该账号,抛出异常
        if (StrUtil.isBlankIfStr(one)){
            throw new ServiceException("账号或密码错误");
        }
        if (!user.getPassword().equals(one.getPassword())){
            throw new ServiceException("账号或密码错误");
        }else{
            //生成Token
            String token = TokenUtils.createToken(String.valueOf(one.getId()), one.getPassword());
            one.setToken(token);
            return one;
        }
    }

    @Override
    public User register(User user) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername,user.getUsername());
        User one = this.getOne(lambdaQueryWrapper);
        //找到该账号,抛出异常
        if (!StrUtil.isBlankIfStr(one)){
            throw new ServiceException("500","该用户已存在");
        }
        super.save(user);
        return this.getOne(lambdaQueryWrapper);
    }
}
