package zhushe.demo.new_piggery.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zhushe.demo.new_piggery.common.R;
import zhushe.demo.new_piggery.entity.Status;
import zhushe.demo.new_piggery.service.StatusService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin
@RequestMapping("/status")
@RestController
@Slf4j
public class StatusController {
    @Autowired
    private StatusService statusService;

    //全局查询
    @GetMapping("/list")
    public R<List<Status>> list() {

        return R.success(statusService.list());

    }

    //分页查询
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {

        Page<Status> pageInfo = new Page<>(page, pageSize);

        statusService.page(pageInfo);

        return R.success(pageInfo);
    }

    //删除单条信息，根据id删除
    @DeleteMapping
    public R<String> deleteById(int id) {
        //不需要判断是否有数据，因为本删除功能是通过前端已经显示的数据进行删除的
        //是用来给update铺路的
        statusService.removeById(id);
        return R.success("删除成功!");
    }

    @PostMapping
    public R<String> save(@RequestBody Status status) {
        statusService.save(status);
        return R.success("保存成功");
    }

    @PostMapping("/update")
    public R<String> update(@RequestBody Status status) {
        //调用接口后写一个xx分钟后过期的key，置为1，告诉下位机已经发生了变化
        //先删除，再保存
       /* //1.删除
        this.deleteById(status.getId());
        //2.保存
        this.save(status);*/
        statusService.updateById(status);

        return R.success("更新成功");
    }


}
