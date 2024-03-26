package zhushe.demo.new_piggery.controller;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zhushe.demo.new_piggery.common.R;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@RequestMapping("/file")
@RestController
@Slf4j
public class FileController {

    @Value("${ip}")
    String ip;

    @Value("${server.port}")
    String port;

    //System.getProperty("user.dir")调用静态方法，usr.dir是一个属性键，带回来的值是工作目录路径的字符串
    //“/files”
    private static final String ROOT_PATH = System.getProperty("user.dir")+File.separator+"files";

    @PostMapping("/upload")
    public R upload(MultipartFile file) throws IOException {
        log.info("1");
        String originalFilename = file.getOriginalFilename();//文件的原始名称
        //假如传入的file为 aaa.png

        String mainName = FileUtil.mainName(originalFilename);//aaa,也就是文件重命名高亮的名称
        // String extName = FileUtil.extName("文件的后缀");//png,文件的后缀,获取的名字不带.
        String extName = originalFilename.substring(file.getOriginalFilename().lastIndexOf(".")); //png,文件的后缀,获取的名字带.

        File parentFile = new File(ROOT_PATH);
        //判断在该路径下存储的文件的父级目录是否存在
        if(!parentFile.exists()){
            //不存在则mkdir创建父级目录
            parentFile.mkdirs();
        }

        File saveFile = new File(ROOT_PATH + File.separator + originalFilename);
        if (saveFile.exists()){
         //如果当前创建的文件已经存在了，那么我们就需要重命名文件
            originalFilename=System.currentTimeMillis() + "_" + mainName + extName;       //重命名完毕后覆盖掉原名
            saveFile = new File(ROOT_PATH + File.separator + originalFilename);
        }

        file.transferTo(saveFile);  //储存文件到本地磁盘中去
        String url = "http://"+ ip+":"+ port + "/file/download/" + originalFilename;
        log.info("=============================");
        log.info("下载路径是:"+url);
        return R.success(url);

    }


    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        String filePath = ROOT_PATH + File.separator + fileName;
        if (!FileUtil.exist(filePath)){
         return ;
        }
        byte[] bytes = FileUtil.readBytes(filePath);
        //将文件的字节流数组写进io流传给前端
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }
}
