package zhushe.demo.new_piggery.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import zhushe.demo.new_piggery.common.R;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(ServiceException.class)//捕获异常
    @ResponseBody//把我们的java对象转化成json
    public R serviceException(ServiceException e){
        return R.error(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(Exception.class)//捕获异常
    @ResponseBody//把我们的java对象转化成json
    public R globalException(Exception e){
        e.printStackTrace();
        return R.error("500","系统错误");
    }




}
