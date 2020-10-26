package com.service.zgbj.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.service.zgbj.mysqlTab.controller.FileImpl;
import com.service.zgbj.mysqlTab.controller.HistoryServiceImpl;
import com.service.zgbj.oss.ConstantProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by fengzhangwei on 2020/6/5.
 */
public class OSSUtil {

    public static int TYPE_PIC = 0;
    public static int TYPE_VOICE = 1;
    public static int TYPE_OTHER = 2;


    public static String upload(File file, int type) {
        System.out.print("开始上传文件\n");
        String endpoint = ConstantProperties.OSS_BUCKET_URL;
        String accessKeyId = ConstantProperties.OSS_KEY_ID;
        String accessKeySecret = ConstantProperties.OSS_KEY_SECRET;
        String bucketName = ConstantProperties.OSS_BUCKET_NAME;
        String fileHost_pic = ConstantProperties.OSS_PIC_PATH;
        String fileHost_voice = ConstantProperties.OSS_VOICE_PATH;
        String fileHost_other = ConstantProperties.OSS_OTHER_PATH;

        if (null == file) {
            return null;
        }
        String fileUrl = null;
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            //容器不存在，就创建
            if (!ossClient.doesBucketExist(bucketName)) {
                ossClient.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                ossClient.createBucket(createBucketRequest);
            }
            if (type == TYPE_PIC) {
                //创建文件路径
                fileUrl = fileHost_pic + randomFilename() + file.getName();
            }
            if (type == TYPE_VOICE) {
                fileUrl = fileHost_voice + randomFilename() + file.getName();
            }

            if (type == TYPE_OTHER) {
                fileUrl = fileHost_other + randomFilename() + file.getName();
            }

            //上传文件
            PutObjectResult result = ossClient.putObject(new PutObjectRequest(bucketName, fileUrl, file));
            //设置权限 这里是公开读
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            if (null != result) {
                // "https://learn-com-earn-agg.oss-cn-beijing.aliyuncs.com/"
                String url = "https://" + bucketName +"."+ endpoint + "/" +fileUrl;
                System.out.print("文件上传成功----name:"+file.getName()+"---url:" + url+"\n");
                file.delete();
                return url;
            }
        } catch (OSSException oe) {
            return null;
        } catch (ClientException ce) {
            return null;
        } finally {
            //关闭
            ossClient.shutdown();
        }
        return null;
    }

    public static String randomFilename() {
        //通过UUID生成文件名
        String fileName = UUID.randomUUID().toString().toUpperCase()
                .replace("-", "");
        return fileName;
    }

    public static int getFileType(String filetype){
        switch (filetype){
            case ".jpg":
            case ".JPG":
            case ".png":
            case ".PNG":
            case ".jpeg":
            case ".JPEG":
            case ".RAW":
                return TYPE_PIC;
            case ".mp3":
            case ".mp4":
            case ".aac":
            case ".ape":
            case ".flac":
            case ".wav":
            case ".wma":
            case ".amr":
            case ".mid":
                return TYPE_VOICE;
        }
        return TYPE_OTHER;
    }


}
