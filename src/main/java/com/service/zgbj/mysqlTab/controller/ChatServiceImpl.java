package com.service.zgbj.mysqlTab.controller;

import com.service.zgbj.im.ChatMessage;
import com.service.zgbj.im.RedEnvelopeBean;
import com.service.zgbj.mysqlTab.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ChatServiceImpl implements ChatService{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Boolean insertChatMessage(ChatMessage msg) {
        String sql = "insert into table_offline_msg(from_id,to_id,pid,type,time,body,body_type,msg_status,conversation,displaytime) value (?,?,?,?,?,?,?,?,?,?)";
        System.out.println(sql);
        Object args[] = {msg.getFromId(),msg.getToId(),msg.getPid(),msg.getType(),msg.getTime(),msg.getBody(),msg.getBodyType(),msg.getMsgStatus(),msg.getConversation(),msg.getDisplaytime()};
        int code =  jdbcTemplate.update(sql,args);
        if (code > 0){
            return true;
        }
        return false;
    }

    @Override
    public List<ChatMessage> getOffLineMsg(String id) {
        List<ChatMessage> chatMessageList = new ArrayList<>();
        String sql = "select * from table_offline_msg WHERE to_id = " + "'" + id + "'";
        System.out.println(sql);
        try {
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
            for (int i=0;i<list.size();i++){
                Map<String, Object> map = list.get(i);
                ChatMessage message = new ChatMessage();
                message.setFromId((String) map.get("from_id"));
                message.setToId((String) map.get("to_id"));
                message.setPid((String) map.get("pid"));
                message.setBody((String) map.get("body"));
                message.setBodyType((int) map.get("body_type"));
                message.setType((int) map.get("type"));
                message.setTime((Long) map.get("time"));
                message.setMsgStatus((int) map.get("msg_status"));
                message.setMsgStatus((int)map.get("displaytime"));
                chatMessageList.add(message);
            }
        }catch (Exception e){

        }
        return chatMessageList;
    }

    @Override
    public Boolean removeMsg(String pid) {
        String sql = "DELETE FROM table_offline_msg WHERE pid = " + "'" + pid + "'";
        System.out.println(sql);
       int code =  jdbcTemplate.update(sql);
       if (code > 0){
           return true;
       }
       return false;
    }

    // 查询所有未领取红包
    @Override
    public List<RedEnvelopeBean> getAllRedEnvelope() {
        List<RedEnvelopeBean> redList = new ArrayList<>();
        String sql = "select * from table_red_envelope WHERE status = " + "'" + RedEnvelopeBean.STATUS_UNCLAIMED + "'";
        System.out.println(sql);
        try {
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = list.get(i);
                    RedEnvelopeBean bean = new RedEnvelopeBean();
                    bean.setFromId((String) map.get("from_id"));
                    bean.setToId((String) map.get("to_id"));
                    bean.setPid((String) map.get("pid"));
                    bean.setBody((String) map.get("body"));
                    bean.setTime((Long) map.get("time"));
                    bean.setStatus((int) map.get("status"));
                    bean.setConversation((String) map.get("conversation"));
                    redList.add(bean);
                }
            }
        }catch (Exception e){
            redList = null;
        }
        return redList;
    }

    // 修改未领取红包的状态
    @Override
    public void updateRedEnvelope(int status,String pid) {
        String sql = "UPDATE  table_red_envelope SET status = "+  "'" + status + "'"+ " WHERE pid = " + "'" + pid + "'";
        System.out.println(sql);
        jdbcTemplate.update(sql);
    }

    // 添加红包记录
    @Override
    public void addRedEnvelope(RedEnvelopeBean bean) {
        String sql = "INSERT INTO table_red_envelope(from_id,to_id,pid,body,time,status,conversation) VALUE (?,?,?,?,?,?,?)";
        System.out.println(sql);
        Object args[] = {bean.getFromId(), bean.getToId(), bean.getPid(), bean.getBody(), bean.getTime(), bean.getStatus(), bean.getConversation()};
        jdbcTemplate.update(sql, args);
    }
}
