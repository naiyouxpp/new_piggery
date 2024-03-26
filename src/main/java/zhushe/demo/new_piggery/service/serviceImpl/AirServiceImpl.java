package zhushe.demo.new_piggery.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import zhushe.demo.new_piggery.entity.Air;
import zhushe.demo.new_piggery.mapper.AirMapper;
import zhushe.demo.new_piggery.service.AirService;

@Service
public class AirServiceImpl extends ServiceImpl<AirMapper, Air> implements AirService  {
}
