package com.nanjing.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUploadUtils {
    public static File fileUpload(MultipartFile files){
        try {
            //获取上传文件的原始名称
            String originalFilename = files.getOriginalFilename();
            //可以实现重命名：
            /*
             * 重命名的方案：
             * 1、可以考虑把文件放到不同的文件夹(此时可以只针对文件夹进行名称处理，那么此时不一定需要重命名文件)
             * 2、可以考虑使用登录用户做文件名称，登录用户唯一，文件名称也唯一
             * 3、使用毫秒值+随机数字命名；年月日时分秒实现命名
             * 4、随机字符串
             * 5、等等
             * */
            //获取文件扩展名
            int indexOf = originalFilename.lastIndexOf(".");
            String extName = "";
            if(indexOf != -1){
                extName = originalFilename.substring(indexOf);
            }
            String newName = UUID.randomUUID().toString().replaceAll("-","") + extName;
            //指定文件保存位置
            File file = new File("D:\\OA",newName);
            //向指定的位置复制上传文件
            files.transferTo(file);
            return  file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}