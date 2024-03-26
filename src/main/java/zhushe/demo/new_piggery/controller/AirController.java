package zhushe.demo.new_piggery.controller;


import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import zhushe.demo.new_piggery.common.R;
import zhushe.demo.new_piggery.entity.Air;
import zhushe.demo.new_piggery.service.AirService;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CrossOrigin
@RequestMapping("/air")
@RestController
@Slf4j
public class AirController {
    @Autowired
    private AirService airService;
    @Autowired
    private RedisTemplate redisTemplate;

    private static final String CO2 = "Co2_";
    private static final String CH2O = "Ch2o_";
    private static final String TVOC = "TVOC_";
    private static final String PM25 = "PM25_";
    private static final String PM10 = "PM10_";
    private static final String HUMIDITY = "Humidity_";
    private static final String TEMP = "Temperature_";


    //全局查询
    @GetMapping("/list")
    public R<List<Air>> list() {
        //获取空气数据先从redis缓存中获取，没有再去sql中获取并加入缓存中

        //查全部太耗时了，要先get所有key然后挨个查，要循环，不搞了

        redisTemplate.opsForValue().get("*");
        //获取到的数据是否要进行处理？
        return R.success(airService.list());
    }

    //分页查询
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {

        Page<Air> pageInfo = new Page<>(page, pageSize);

        airService.page(pageInfo);

        return R.success(pageInfo);
    }

    //删除单条信息，根据id删除
    @DeleteMapping
    public R<String> deleteById(int id) {
        //不需要判断是否有数据，因为本删除功能是通过前端已经显示的数据进行删除的
        //是用来给update铺路的
        airService.removeById(id);
        return R.success("删除成功!");
    }

    @PostMapping
    public R<String> save(@RequestBody Air air) {
        airService.save(air);
        return R.success("保存成功");
    }

    @PostMapping("/update")
    public R<String> update(@RequestBody Air air) {
        //先删除，再保存
        //1.删除
        this.deleteById(air.getId());
        //2.保存
        this.save(air);
        //拼接key
        String airKey = "air_id:" + Integer.toString(air.getId());
        //redis,每次更新都会往redis中缓存数据并且更新数据库,每2天过期一次
        long l = System.currentTimeMillis();
        String updateCo2Time = CO2 + l;
        String updateCho2Time = CH2O + l;
        String updateTVOCTime = TVOC + l;
        String updatePM25Time = PM25 + l;
        String updatePM10Time = PM10 + l;
        String updateHumidityTime = HUMIDITY + l;
        String updateTEMPTime = TEMP + l;
        List<String> updateTimeList = Arrays.asList(updateCo2Time, updateCho2Time, updateTVOCTime, updatePM25Time, updatePM10Time, updateHumidityTime, updateTEMPTime);

        //将数据缓存进redis
        updateTimeList.stream().map(item -> {
            log.info("成功进入Stream");
            String[] parts = item.split("_");
            log.info(parts[0]);
            if ("Co2".equals(parts[0])) {
                redisTemplate.opsForHash().put(airKey, item, air.getCo2());
                log.info("Co2数据缓存成功");
            }
            if ("Ch2o".equals(parts[0])) {
                redisTemplate.opsForHash().put(airKey, item, air.getCh2o());
                log.info("Ch2o数据缓存成功");
            }
            if ("TVOC".equals(parts[0])) {
                redisTemplate.opsForHash().put(airKey, item, air.getTvoc());
                log.info("TVOC数据缓存成功");
            }
            if ("PM25".equals(parts[0])) {
                redisTemplate.opsForHash().put(airKey, item, air.getPm25());
                log.info("PM25数据缓存成功");
            }
            if ("PM10".equals(parts[0])) {
                redisTemplate.opsForHash().put(airKey, item, air.getPm10());
                log.info("PM10数据缓存成功");
            }
            if ("Humidity".equals(parts[0])) {
                redisTemplate.opsForHash().put(airKey, item, air.getHumidity());
                log.info("Humidity数据缓存成功");
            }
            if ("Temperature".equals(parts[0])) {
                redisTemplate.opsForHash().put(airKey, item, air.getTemperature());
                log.info("Temperature数据缓存成功");
            }
            return null;
        }).collect(Collectors.toList());
        redisTemplate.expire("air_key", 1, TimeUnit.DAYS);
        log.info("通过update方法缓存redis成功");

        return R.success("更新成功");
    }

    @GetMapping("/{id}")
    public R<List<Air>> selectByIdFromRedis(@PathVariable Integer id) {
        //key为猪舍号标识，flied用来记录时间也是通过拼接字符串，格式为 气体名_时间戳，value则为气体得具体数值
        // 构建 Redis 中的键，例如：air_id:1
        String airKey = "air_id:" + Integer.toString(id);

        List<Air> airList = new ArrayList<>();
        //如果缓存内有这个key
        if (redisTemplate.hasKey(airKey)) {

            //取前60、50、40、30、20、10分钟前的数值，再取一个最新的值为当前数据，共七个
            long l = System.currentTimeMillis();

            //先模拟拿个5分钟内的所有数据吧，呃算了就拿5个吧，拿5、4、3、2、1分钟前的数据
            //拿到当前时间后开始减毫秒数300*1000
            //最终决定还是 30 分钟吧 每5分钟取一次
/*            long fiveMin = l - 300 * 1000;
            long fourMin = l - 240 * 100;
            long threeMin = l - 180 * 100;
            long twoMin = l - 120 * 100;
            long oneMin = l - 60 * 100;*/
            for (int i = 0; i <= 6; i++) {
                long min = l - (6 - i) * 300 * 1000;
                String minFieldCO2 = CO2 + min;
                String minFieldCH2O = CH2O + min;
                String minFieldTVOC = TVOC + min;
                String minFieldPM25 = PM25 + min;
                String minFieldPM10 = PM10 + min;
                String minFieldHUMIDITY = HUMIDITY + min;
                String minFieldTEMP = TEMP + min;
                List<String> fiveMinList = Arrays.asList(minFieldCO2, minFieldCH2O, minFieldTVOC, minFieldPM25, minFieldPM10, minFieldHUMIDITY, minFieldTEMP);

                //判断是否每个value都不为空
            /*boolean allExist = fiveMinList.stream()
                    .allMatch(item -> redisTemplate.opsForHash().hasKey(airKey, item));*/

                //逐个判断value值是否为空
                List<String> newFiveMinList = fiveMinList.stream().map(item -> {
                    //不为空就写入新list
                    if (redisTemplate.opsForHash().hasKey(airKey, item)) {
                        return item;
                    } else {
                        //为空就向前找1s数据，最多10s，有则写入新list,没有的话就返回原数据，就这样吧，查不到的话前端搞个假数据....(感觉不太合理，但是没办法了，进一步是需要微服务来解决吗，后面再看看)
                        for (int s = 0; s < 10; s++) {
                            int j = s + 1;
                            String[] strings = item.split("_");
                            Long newString = Long.parseLong(strings[1]) - (1000 * j);
                            strings[1] = String.valueOf(newString);
                            String newItem = strings[0] + "_" + strings[1];
                            if (redisTemplate.opsForHash().hasKey(airKey, newItem)) {
                                return newItem;
                            }
                        }
                    }

                    return item;
                }).collect(Collectors.toList());

                //全查
                List<String> list = redisTemplate.opsForHash().multiGet(airKey, newFiveMinList);

                //封装数据
                if (list != null) {
                    Air air = new Air();
                    air.setId(id);
                    air.setCo2(list.get(0));
                    air.setCh2o(list.get(1));
                    air.setTvoc(list.get(2));
                    air.setPm25(list.get(3));
                    air.setPm10(list.get(4));
                    air.setHumidity(list.get(5));
                    air.setTemperature(list.get(6));
                    // 将毫秒数转换为 Instant 对象
                    //直接封装5分钟前的数据，无论newFiveMinList是否用了10s前的数据，就当他是当时的数据
                    Instant instant = Instant.ofEpochMilli(min);
                    // 使用 ZoneId.systemDefault() 获取默认时区，并将 Instant 对象转换为 LocalDateTime 对象
                    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Shanghai"));
                    air.setUpdateTime(localDateTime);
                    log.info((6-i)*5 + "分钟前的air数据");
                    log.info(String.valueOf(air));
                    airList.add(air);
                }

                // 如果 Redis 中存在对应的数据，则将 JSON 字符串转换为 Air 对象并返回
                // Air air = JSONObject.parseObject(json, Air.class)

            }


        } else
            return R.error("不存在该猪舍");
        return R.success(airList);
    }
}
