package zhushe.demo.new_piggery.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import zhushe.demo.new_piggery.entity.Status;
import zhushe.demo.new_piggery.mapper.StatusMapper;
import zhushe.demo.new_piggery.service.StatusService;

@Service
public class StatusServiceImpl extends ServiceImpl<StatusMapper, Status>  implements StatusService {
}
