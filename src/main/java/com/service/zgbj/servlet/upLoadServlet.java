package com.service.zgbj.servlet;

import com.service.zgbj.utils.GsonUtil;
import com.service.zgbj.utils.OSSUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by fengzhangwei on 2020/6/5.
 */
@WebServlet("/uploadFile")
public class upLoadServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HashMap<String, Object> statusMap = new HashMap<>();


        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(req.getSession().getServletContext());


        // 判断是否是多数据段提交格式
        if (multipartResolver.isMultipart(req)) {
            MultipartHttpServletRequest multiRequest = multipartResolver.resolveMultipart(req);
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()){
                MultipartFile mf = multiRequest.getFile(iter.next());
                String fileName = mf.getOriginalFilename();
                System.out.print("filename---:" + fileName);
                if(fileName == null || fileName.trim().equals("")){
                    continue;
                }
                String fileType = fileName.substring(fileName.lastIndexOf('.'));
                System.out.print("---文件后缀名----" + fileType + "\n");
                File newFile = new File(fileName);
                FileOutputStream os = new FileOutputStream(newFile);
                os.write(mf.getBytes());
                os.close();
                mf.transferTo(newFile);
                //上传到OSS
                int type = OSSUtil.getFileType(fileType);
                String uploadUrl = OSSUtil.upload(newFile, type);
                if (null != uploadUrl && !uploadUrl.isEmpty()) {
                    statusMap.put("code", 1);
                    statusMap.put("msg", "成功");
                    statusMap.put("url", uploadUrl);
                } else {
                    statusMap.put("code", 0);
                    statusMap.put("msg", "失败");
                    statusMap.put("url", null);
                }
                resp.getWriter().write(GsonUtil.BeanToJson(statusMap));

            }
        }

    }

}
