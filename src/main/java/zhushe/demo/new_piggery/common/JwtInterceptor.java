package zhushe.demo.new_piggery.common;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import zhushe.demo.new_piggery.entity.User;
import zhushe.demo.new_piggery.exception.ServiceException;
import zhushe.demo.new_piggery.mapper.UserMapper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("进行token判断");
        String token = request.getHeader("token"); //header里面传过来的参数

        if (StrUtil.isBlank(token)) {
            token = request.getParameter("token"); //url里面传过来的参数 ?id=xxx
        }
        log.info("获取token成功");
        //上面两种方式获取token,任意一个有就默认他有token

/*        // 如果不是映射到方法直接通过
        if (handler instanceof HandlerMethod) {
            AuthAccess annotation = ((HandlerMethod) handler).getMethodAnnotation(AuthAccess.class);
            if (annotation != null) {
                return true;
            }
        }*/


        // 是不是真的有？ 如有
        if (StrUtil.isBlank(token)) {
            throw new ServiceException("401", "请登录");
        }
        // 获取 token 中的 user id

        String userId; //为什么存一个string类型的userId,因为token存的是string字符串

        try {
            //Audience是JWT里的一个小仓库，我们存进userId，decode解码后拿到第一个数据就是userID
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new ServiceException("401", "请登录");
        }
        // 把token中的userid转化成integer类型然后，查询数据库
        User user = userMapper.selectById(Integer.valueOf(userId));

        // 没这个用户则重新登录
        if (user == null) {
            throw new ServiceException("401", "请登录");
        }

        //JWTVerifier 是一个JWT官方提供的验证器
        //通过加密用户的密码 去生成一个验证器
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token); // 验证token
        } catch (JWTVerificationException e) {
            throw new ServiceException("401", "请登录");
        }
        log.info("真有token啊，行吧");

        String stringId = request.getHeader("X-Current-User-Id");

        Integer id = Integer.valueOf(stringId);

        BaseContext.setCurrentId(id);
        log.info("id存放成功");


        return true;
    }
}