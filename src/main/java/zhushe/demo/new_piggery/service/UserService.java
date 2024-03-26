package zhushe.demo.new_piggery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import zhushe.demo.new_piggery.entity.User;

public interface UserService extends IService<User> {
    User login(User user);

    User register(User user);
}
