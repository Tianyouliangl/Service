package com.service.zgbj.mysqlTab.tab;

import javax.persistence.*;

@Entity
@Table(name = "table_offline_msg")
public class offLineMsg {
    //自增ID
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "fromId")
    private String fromId;
    @Column(name = "toId")
    private String toId;
    @Column(name = "pid")
    private String pid;
    @Column(name = "bodyType")
    private int bodyType;
    @Column(name = "body")
    private String body;
    @Column(name = "msgStatus")
    private int msgStatus;
    @Column(name = "time")
    private Long time;
    @Column(name = "type")
    private int type;
    @Column(name = "conversation")
    private String conversation;

    public String getConversation() {
        return conversation;
    }

    public void setConversation(String conversation) {
        this.conversation = conversation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
