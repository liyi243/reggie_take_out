package com.itheima.reggie.controller;


import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @ClassName 文件上传和下载
 * @Description
 * @Author:liyunzhi
 * @Date
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    //路径
    @Value("${reggie.path}")
    private String basePath;

    /**
    * @Description: 采用file.TransferTo来保存上传的文件
    * @Param: [file]
    * @return: com.itheima.reggie.common.R<java.lang.String>
    * @Author: liyunzhi
    * @Date: 2022/6/25
    */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        log.info(file.toString());
        //原始文件名,后缀
        String originalFilename = file.getOriginalFilename();//abc.jpg
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID重新生成文件名,来防止文件覆盖的问题
        String fileName= UUID.randomUUID().toString() + suffix;
        //创建一个目录对象
        File dir = new File(basePath);
        System.out.println(dir.getAbsolutePath());
        //判断这个目录对象是否存在
        if(!dir.exists()){
            dir.mkdirs();
        }
        //System.out.println("上传文件保存地址:"+dir);
        try {
            //将临时文件存到指定位置
            //file.transferTo(new File(basePath+fileName));
            file.transferTo(new File(dir.getAbsolutePath(),fileName));
        }catch (IOException e){
            e.printStackTrace();
        }
        //return R.success("上传成功");------------注意这里
        return R.success(fileName);
    }
    /**
    * @Description: 下载，具体实现的是上传的文件在浏览器中那个框里显示
     * 本质是服务器将文件以流的形式写回浏览器的过程（直接在浏览器打开）
     * 形式一：以附件形式下载，弹出保存对话框，将文件保存到指定磁盘目录
     * 形式二：直接在浏览器中打开
    * @Param: [name, response]
    * @return: void
    * @Author: liyunzhi
    * @Date: 2022/6/25
    */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            //读取输入流文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath+name));
            //输出流，通过输出流将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            ////响应回去的类型:图片文件("image/jpeg")
            response.setContentType("image/png");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();//刷新
            }
            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
