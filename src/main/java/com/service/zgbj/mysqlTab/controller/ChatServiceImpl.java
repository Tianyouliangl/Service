package com.service.zgbj.mysqlTab.controller;

import com.service.zgbj.im.ChatMessage;
import com.service.zgbj.im.RedEnvelopeBean;
import com.service.zgbj.im.SocketBean;
import com.service.zgbj.mysqlTab.ChatService;
import com.service.zgbj.mysqlTab.SocketConnectService;
import com.service.zgbj.utils.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatServiceImpl implements ChatService,SocketConnectService{
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
                message.setConversation((String)map.get("conversation"));
                message.setMsgStatus((int) map.get("msg_status"));
                message.setDisplaytime((int)map.get("displaytime"));
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

    @Override
    public String getRedEnvelopePid(String pid) {
        HashMap<String, Object> statusMap = new HashMap<>();
        HashMap<String, Object> redEnvelopeMap = new HashMap<>();
        String sql = "select * from table_red_envelope WHERE pid = " + "'" + pid + "'";
        System.out.println(sql);
        try {
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
            if (list.size() > 0) {
                statusMap.put("code", 1);
                statusMap.put("msg", "成功");
                Map<String, Object> map = list.get(0);
                redEnvelopeMap.put("fromId",map.get("from_id"));
                redEnvelopeMap.put("toId",map.get("to_id"));
                redEnvelopeMap.put("pid",map.get("pid"));
                redEnvelopeMap.put("body",map.get("body"));
                redEnvelopeMap.put("time",map.get("time"));
                redEnvelopeMap.put("msgStatus",map.get("status"));
                redEnvelopeMap.put("conversation",map.get("conversation"));
                statusMap.put("data",redEnvelopeMap);
            }else {
                statusMap.put("code", 0);
                statusMap.put("msg", "失败");
            }
        }catch (Exception e){

        }
        return GsonUtil.BeanToJson(statusMap);
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
        String sql = "INSERT into table_red_envelope(from_id,to_id,pid,body,time,status,conversation) VALUE (?,?,?,?,?,?,?)";
        System.out.println(sql);
        Object args[] = {bean.getFromId(), bean.getToId(), bean.getPid(), bean.getBody(), bean.getTime(), bean.getStatus(), bean.getConversation()};
        jdbcTemplate.update(sql, args);
    }

    @Override
    public void insert(SocketBean socketBean) {
        try {
            String sql = "INSERT into table_socket (uid,token,mobile) VALUE (?,?,?)";
            System.out.println(sql);
            Object args[] = {socketBean.getUid(), socketBean.getToken(), socketBean.getMobile()};
            int code = jdbcTemplate.update(sql, args);
            if (code > 0) {
                System.out.println("添加成功");
            }
        } catch (Exception e) {
            System.out.println("insert socket" + e.toString());
        }
    }

    @Override
    public Boolean whereIsOnline(String uid) {
        List<Map<String, Object>> map;
        try {
            String sql = "SELECT * FROM table_socket WHERE uid = " + "'" + uid + "'";
            System.out.println(sql);
            map = jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            map = null;
            System.out.println("whereIsOnline:" + e.toString());
        }
        if (map != null && map.size() > 0) {
            return true;
        }
        return false;
    }


    @Override
    public void delete(String token) {
        try {
            String sql = "DELETE FROM table_socket WHERE token = " + "'" + token + "'";
            System.out.println(sql);
            int update = jdbcTemplate.update(sql);
            if (update > 0) {
                System.out.println("删除成功");
            }
        } catch (Exception e) {
            System.out.println("delete token:" + e.toString());
        }
    }

    @Override
    public String getToken(String uid) {
        List<Map<String, Object>> map;
        try {
            String sql = "SELECT * FROM table_socket WHERE uid = " + "'" + uid + "'";
            System.out.println(sql);
            map = jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            map = null;
            System.out.println("getToken:" + e.toString());
        }
        if (map != null && map.size() > 0) {
            String token = map.get(0).get("token").toString();
            System.out.println("token:" + token);
            return token;
        }
        return "";
    }

    @Override
    public void updateToken(String token, String uid) {
        try {
            String sql = "UPDATE table_socket SET token =" + "'" + token + "'" + "WHERE uid = " + "'" + uid + "'";
            System.out.println(sql);
            int update = jdbcTemplate.update(sql);
            if (update > 0) {
                System.out.println("修改成功");
            }
        } catch (Exception e) {
            System.out.println("updateToken:" + e.toString());
        }
    }
}
