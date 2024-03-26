package zhushe.demo.new_piggery.exception;

import lombok.Data;

@Data
public class ServiceException extends RuntimeException{
    private final String code ;
    public ServiceException(String msg){
        super(msg);
        this.code=  "500";//默认code
    }

    public ServiceException(String code,String msg){
        super(msg);
        this.code = code;
    }
}
