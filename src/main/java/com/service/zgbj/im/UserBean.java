package com.service.zgbj.im;

public class UserBean {
    private String uid;
    private String name;
    private String headUrl;

    public UserBean(String uid, String name, String headUrl) {
        this.uid = uid;
        this.name = name;
        this.headUrl = headUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", headUrl='" + headUrl + '\'' +
                '}';
    }
}
