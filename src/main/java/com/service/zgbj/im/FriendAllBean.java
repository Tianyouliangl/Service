package com.service.zgbj.im;

import java.util.List;

public class FriendAllBean {
    /**
     * msg : 成功
     * code : 1
     * data : [{"birthday":"2000-01-01","sex":"男","mobile":"17600463504","sign":"退一步海阔天空.","remark":"吴成亚","source":1,"uid":"39c036c5-1fa7-4f6d-86f9-3dcd5eb8b0f6","imageUrl":"https://b-ssl.duitang.com/uploads/item/201807/06/20180706112250_3iBxt.thumb.700_0.jpeg","online":1,"location":"北京市丰台区公益西桥","isFriend":true,"age":20,"email":"wuchengya@qq.com","username":"吴成亚"},{"birthday":"2000-01-01","sex":"男","mobile":"17600463505","sign":"退一步海阔天空.","remark":"佩奇","source":1,"uid":"c413fc34-7a8e-4616-8e7d-f5b3a603c6bf","imageUrl":"https://b-ssl.duitang.com/uploads/item/201807/06/20180706112251_niE3Y.thumb.700_0.jpeg","online":1,"location":"北京市丰台区公益西桥","isFriend":true,"age":20,"email":"peiqi@qq.com","username":"佩奇"},{"birthday":"2000-01-01","sex":"男","mobile":"17600463506","sign":"退一步海阔天空.","remark":"皮皮","source":1,"uid":"e02ff4f4-b87d-40ed-afb5-80363500c84f","imageUrl":"https://b-ssl.duitang.com/uploads/item/201804/29/20180429111927_4i2Ks.thumb.700_0.jpeg","online":1,"location":"北京市丰台区公益西桥","isFriend":true,"age":20,"email":"pipi@qq.com","username":"皮皮"},{"birthday":"2000-01-01","sex":"男","mobile":"17600463507","sign":"退一步海阔天空.","remark":"丢丢","source":1,"uid":"241c311f-be28-4417-9203-c5fae65d23d5","imageUrl":"https://b-ssl.duitang.com/uploads/item/201707/06/20170706164810_kiCre.jpeg","online":1,"location":"北京市丰台区公益西桥","isFriend":true,"age":20,"email":"diudiu@qq.com","username":"丢丢"},{"birthday":"2000-01-01","sex":"男","mobile":"17600463508","sign":"退一步海阔天空.","remark":"给我发","source":1,"uid":"5e33fb19-e2b7-4ba8-875f-cdcd3372659d","imageUrl":"http://img3.imgtn.bdimg.com/it/u=466636099,2440212896&fm=11&gp=0.jpg","online":1,"location":"北京市丰台区公益西桥","isFriend":true,"age":20,"email":"fa@qq.com","username":"给我发"}]
     */

    private String msg;
    private int code;
    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * birthday : 2000-01-01
         * sex : 男
         * mobile : 17600463504
         * sign : 退一步海阔天空.
         * remark : 吴成亚
         * source : 1
         * uid : 39c036c5-1fa7-4f6d-86f9-3dcd5eb8b0f6
         * imageUrl : https://b-ssl.duitang.com/uploads/item/201807/06/20180706112250_3iBxt.thumb.700_0.jpeg
         * online : 1
         * location : 北京市丰台区公益西桥
         * isFriend : true
         * age : 20
         * email : wuchengya@qq.com
         * username : 吴成亚
         */

        private String birthday;
        private String sex;
        private String mobile;
        private String sign;
        private String remark;
        private int source;
        private String uid;
        private String imageUrl;
        private int online;
        private String location;
        private boolean isFriend;
        private int age;
        private String email;
        private String username;

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getSource() {
            return source;
        }

        public void setSource(int source) {
            this.source = source;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public int getOnline() {
            return online;
        }

        public void setOnline(int online) {
            this.online = online;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public boolean isIsFriend() {
            return isFriend;
        }

        public void setIsFriend(boolean isFriend) {
            this.isFriend = isFriend;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
