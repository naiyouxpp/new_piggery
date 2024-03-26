package zhushe.demo.new_piggery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import zhushe.demo.new_piggery.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
