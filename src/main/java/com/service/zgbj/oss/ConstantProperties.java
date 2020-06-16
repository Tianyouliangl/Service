package com.service.zgbj.oss;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by fengzhangwei on 2020/6/5.
 */

@Component
public class ConstantProperties implements InitializingBean {

    @Value("${bucketUrl}")
    private String aliyun_bucketUrl;

    @Value("${bucketName}")
    private String aliyun_bucketName;

    @Value("${picLocation}")
    private String aliyun_picLocation;

    @Value("${voiceLocation}")
    private String aliyun_voiceLocation;

    @Value("${otherLocation}")
    private String aliyun_otherLocation;

    @Value("${accessKeyId}")
    private String aliyun_accessKeyId;

    @Value("${accessKeySecret}")
    private String aliyun_accessKeySecret;


    public static String OSS_BUCKET_URL;
    public static String OSS_BUCKET_NAME;
    public static String OSS_PIC_PATH;
    public static String OSS_VOICE_PATH;
    public static String OSS_OTHER_PATH;
    public static String OSS_KEY_ID;
    public static String OSS_KEY_SECRET;


    @Override
    public void afterPropertiesSet() throws Exception {
        OSS_BUCKET_URL = aliyun_bucketUrl;
        OSS_BUCKET_NAME = aliyun_bucketName;
        OSS_PIC_PATH = aliyun_picLocation;
        OSS_VOICE_PATH = aliyun_voiceLocation;
        OSS_OTHER_PATH = aliyun_otherLocation;
        OSS_KEY_ID = aliyun_accessKeyId;
        OSS_KEY_SECRET = aliyun_accessKeySecret;
    }
}
