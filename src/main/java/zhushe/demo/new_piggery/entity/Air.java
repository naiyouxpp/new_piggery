package zhushe.demo.new_piggery.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Air {
        private Integer id;
        private String  temperature;     //温度
        private String  co2;             //二氧化碳
        private String  ch2o;            //甲醛
        private String  tvoc;            //总挥发性有机化合物
        private String  pm25;            //PM2.5
        private String  pm10;
        private String  humidity;        //湿度
        @TableField(fill = FieldFill.INSERT)
        private LocalDateTime createTime;


        @TableField(fill = FieldFill.INSERT_UPDATE)
        private LocalDateTime updateTime;

        @JSONField(format = "yyyy-MM-dd HH:mm:ss")
        @TableField(fill = FieldFill.INSERT)
        private Integer createUser;

        @JSONField(format = "yyyy-MM-dd HH:mm:ss")
        @TableField(fill = FieldFill.INSERT_UPDATE)
        private Integer updateUser;

    //1.可以写是否启用
}
