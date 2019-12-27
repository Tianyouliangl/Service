package com.service.zgbj.mysqlTab.controller;

import com.service.zgbj.im.ChatMessage;
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
        String sql = "insert into table_offline_msg(from_id,to_id,pid,type,time,body,body_type,msg_status,conversation) value (?,?,?,?,?,?,?,?,?)";
        System.out.println(sql);
        Object args[] = {msg.getFromId(),msg.getToId(),msg.getPid(),msg.getType(),msg.getTime(),msg.getBody(),msg.getBodyType(),msg.getMsgStatus(),msg.getConversation()};
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
            chatMessageList.add(message);
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
}
