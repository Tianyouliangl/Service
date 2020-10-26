package com.service.zgbj.im;

/**
 * Created by fengzhangwei on 2020/10/21.
 */
public class LikePhotoBean {
    String url;
    String pid;
    int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPid() {
        return pid;
    }

    public String getUrl() {
        return url;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
