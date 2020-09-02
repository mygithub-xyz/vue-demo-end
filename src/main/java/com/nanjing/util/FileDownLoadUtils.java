package com.nanjing.util;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class FileDownLoadUtils {
    public static void fileDownLoad(HttpServletResponse response,String filename) {
        try {
            //C:\analysis\烟草v6需求说明书.doc
            //一个头、两个流
       /* AnalysisExtends analysisExtends=projectNeedService.selectProjectNeedById(id);
        String filename=analysisExtends.getProname();*/
            File file = new File("D:\\OA\\" + filename);
            //处理下载时名称中文乱码
            String name = file.getName();
            //先解码，再编码
            name = new String(name.getBytes("utf-8"), "iso8859-1");
            //一个头--响应头，作用是设置下载时的文件名称的
            response.setHeader("Content-Disposition", "attachment;filename=" + name);
            //读服务器端本地文件的读入流
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            //把服务器端文件写出到用户端的写出流
            OutputStream os = response.getOutputStream();
            byte[] bs = new byte[1024];
            int len = -1;
            while ((len = bis.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            os.close();
            bis.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}