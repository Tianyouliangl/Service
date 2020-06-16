package com.service.zgbj.im;

/**
 * Created by fengzhangwei on 2020/6/4.
 */
public class SocketBean {
    private String mobile;
    private String uid;
    private String token;
    private String desc;

    public String getMobile() {
        return mobile;
    }

    public String getToken() {
        return token;
    }

    public String getUid() {
        return uid;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
