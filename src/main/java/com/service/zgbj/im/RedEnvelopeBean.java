package com.service.zgbj.im;

import javax.persistence.Column;

public class RedEnvelopeBean {

    public static int STATUS_UNCLAIMED = 4; // 未领取
    public static int STATUS_ALREADY_RECEIVED = 5;  // 已领取
    public static int STATUS_OVERTIME = 6;   // 超时

    private String fromId;
    private String toId;
    private String body;
    private Long time;
    private String pid;
    private int status = STATUS_UNCLAIMED;
    private String conversation;

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getConversation() {
        return conversation;
    }

    public void setConversation(String conversation) {
        this.conversation = conversation;
    }
}
