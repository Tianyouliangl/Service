package com.service.zgbj.mysqlTab.tab;

import javax.persistence.*;

@Entity
@Table(name = "table_red_envelope")
public class Red_Envelope {

    //自增ID
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    // 发送者
    @Column(name = "fromId")
    private String fromId;

    // 接受者
    @Column(name = "toId")
    private String toId;

    // 详细内容
    @Column(name = "body")
    private String body;

    // 发送时间
    @Column(name = "time")
    private Long time;

    // 消息的唯一标识
    @Column(name = "pid")
    private String pid;

    // 状态  4 未领取  5 已领取
    @Column(name = "status")
    private int status;

    // 两个人聊天的唯一标识
    @Column(name = "conversation")
    private String conversation;

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
