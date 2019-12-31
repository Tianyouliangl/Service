package com.service.zgbj.im;

/**
 * author : fengzhangwei
 * date : 2019/12/19
 */
public class ChatMessage {

    private String fromId;
    private String toId;
    private String pid;
    private int bodyType;
    private String body;
    private int msgStatus;
    private Long time;
    private int type;
    private String conversation;
    private int displaytime;

    public int getDisplaytime() {
        return displaytime;
    }

    public void setDisplaytime(int displaytime) {
        this.displaytime = displaytime;
    }

    public String getConversation() {
        return conversation;
    }

    public void setConversation(String conversation) {
        this.conversation = conversation;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getBodyType() {
        return bodyType;
    }

    public void setBodyType(int bodyType) {
        this.bodyType = bodyType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(int msgStatus) {
        this.msgStatus = msgStatus;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "fromId='" + fromId + '\'' +
                ", toId='" + toId + '\'' +
                ", pid='" + pid + '\'' +
                ", bodyType=" + bodyType +
                ", body='" + body + '\'' +
                ", msgStatus=" + msgStatus +
                ", time=" + time +
                '}';
    }
}
