package zhushe.demo.new_piggery.entity;

import lombok.Data;

@Data
public class User {
    private int id;             //id
    private String username;    //用户名
    private String password;    //密码
    private String name;        //姓名
    private String phone;       //手机号
    private String email;       //邮箱
    private String address;     //地址
    private String avatar;      //头像
    private String role;        //角色
    private String token;
}
