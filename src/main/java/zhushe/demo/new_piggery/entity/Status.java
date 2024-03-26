package zhushe.demo.new_piggery.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Status {
    private Integer id;
    private Boolean light;
    private Boolean heater;
    private Boolean fan;
    private Boolean atomizer;
    private Boolean curtain;
    private Boolean drencher;
    private Boolean man;
    private Boolean pig;
    private Boolean water;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;
    //1.可以写是否启用
}
