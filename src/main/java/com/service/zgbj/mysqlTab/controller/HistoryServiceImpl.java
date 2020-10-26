package com.service.zgbj.mysqlTab.controller;

import com.service.zgbj.im.ChatMessage;
import com.service.zgbj.mysqlTab.HistoryService;
import com.service.zgbj.utils.GsonUtil;
import com.service.zgbj.utils.OfTenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HistoryServiceImpl implements HistoryService {

    private HashMap<String, Long> map = new HashMap();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void createTable(String tableName) {
        String tName = OfTenUtils.replace(tableName);
        String sql = "CREATE TABLE IF NOT EXISTS " + " history_" + tName + " (" +
                "id INT (11) NOT NULL AUTO_INCREMENT PRIMARY KEY ," +
                "from_id VARCHAR (255)," +
                "to_id VARCHAR (255)," +
                "pid VARCHAR (255)," +
                "body TEXT," +
                "conversation VARCHAR (255)," +
                "body_type INT (11)," +
                "msg_status INT (11)," +
                "type INT (11)," +
                "displaytime INT (11)," +
                "time BIGINT(20)" + ")" +
                "ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET ='utf8mb4'";
        System.out.println("Sql:-------" + sql);
        jdbcTemplate.update(sql);
    }

    @Override
    public void insetData(ChatMessage msg, String tableName) {
        String tName = OfTenUtils.replace(tableName);
        createTable(tName);
        String sql_select = "SELECT * FROM history_" + tName + " WHERE pid = " + "'" + msg.getPid() + "'";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql_select);
        if (list.size() > 0) {
            String sql_update = "UPDATE history_" + tName + " SET body_type = " + msg.getBodyType() + " WHERE pid = " + "'" + msg.getPid() + "'";
            int update = jdbcTemplate.update(sql_update);
            if (update > 0) {
                System.out.println("更改历史消息成功!----表名-=== history_" + tName);
            }
        } else {
            String sql = "insert into" + " history_" + tName + "(from_id,to_id,pid,type,time,body,body_type,msg_status,conversation,displaytime) value (?,?,?,?,?,?,?,?,?,?)";
            Object args[] = {msg.getFromId(), msg.getToId(), msg.getPid(), msg.getType(), msg.getTime(), msg.getBody(), msg.getBodyType(), msg.getMsgStatus(), msg.getConversation(), msg.getDisplaytime()};
            int code = jdbcTemplate.update(sql, args);
            if (code > 0) {
                System.out.println("插入历史消息成功!----表名-=== history_" + tName);
            }
        }

    }

    @Override
    public String getChatMessage(String tableName, String conversation, int pageNo, int pageSize) {
        String sql = null;
        HashMap<String, Object> statusMap = new HashMap<>();
        List<ChatMessage> chatMessageList = new ArrayList<>();
        String t_name = OfTenUtils.replace(tableName);
        createTable(t_name);
        Long mTime = map.get(conversation + t_name);
        if (pageNo > 1) {
            if (mTime != null) {
                sql = "SELECT * FROM ( SELECT * FROM history_" + t_name + " WHERE conversation = " + "'" + conversation + "'" + " AND time <" + mTime + " ORDER BY time DESC LIMIT " + pageSize + ") aa" + " ORDER BY time";
            } else {
                sql = "SELECT * FROM ( SELECT * FROM history_" + t_name + " WHERE conversation = " + "'" + conversation + "'" + " ORDER BY time DESC LIMIT " + (pageNo - 1) * pageSize + "," + pageSize + ") aa" + " ORDER BY time";
            }
        } else {
            sql = "SELECT * FROM ( SELECT * FROM history_" + t_name + " WHERE conversation = " + "'" + conversation + "'" + " ORDER BY time DESC LIMIT " + (pageNo - 1) * pageSize + "," + pageSize + ") aa" + " ORDER BY time";
        }
//        sql = "SELECT * FROM ( SELECT * FROM history_" + t_name + " WHERE conversation = " + "'" + conversation + "'" + " ORDER BY time DESC LIMIT " + (pageNo - 1) * pageSize + "," + pageSize + ") aa" + " ORDER BY time";
        System.out.println("sql   :::    " + sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

        if (list.size() > 0) {
            statusMap.put("code", 1);
            statusMap.put("msg", "成功");
            map.put((String) list.get(list.size() - 1).get("conversation") + t_name, (Long) list.get(0).get("time"));
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                ChatMessage message = new ChatMessage();
                message.setFromId((String) map.get("from_id"));
                message.setToId((String) map.get("to_id"));
                message.setPid((String) map.get("pid"));
                message.setBody((String) map.get("body"));
                message.setConversation((String) map.get("conversation"));
                message.setBodyType((int) map.get("body_type"));
                message.setMsgStatus((int) map.get("msg_status"));
                message.setType((int) map.get("type"));
                message.setDisplaytime((int) map.get("displaytime"));
                message.setTime((Long) map.get("time"));
                chatMessageList.add(message);
            }
            statusMap.put("data", chatMessageList);
        } else {
            statusMap.put("code", 0);
            statusMap.put("msg", "没有更多记录了");
        }
        String json = GsonUtil.BeanToJson(statusMap);
        System.out.println("历史记录===" + json);
        return json;
    }

    @Override
    public String updateHistoryStatus(String tabName, int status, String pid) {
        HashMap<String, Object> statusMap = new HashMap<>();
        String t_name = OfTenUtils.replace(tabName);
        createTable(tabName);
        String sql = "UPDATE history_" + t_name + "   SET msg_status = " + "'" + status + "'" + " WHERE pid = " + "'" + pid + "'";
        System.out.println(sql);
        int update = jdbcTemplate.update(sql);
        if (update > 0) {
            statusMap.put("code", 1);
            statusMap.put("msg", "成功");
            statusMap.put("data", new ChatMessage());
        } else {
            statusMap.put("code", 0);
            statusMap.put("msg", "失败");
        }
        return GsonUtil.BeanToJson(statusMap);
    }

    @Override
    public String getConversation(String fromId, String toId) {
        HashMap<String, Object> statusMap = new HashMap<>();
        String conversation = null;
        String t_name = OfTenUtils.replace(fromId);
        createTable(fromId);
        createTable(toId);
        String sql = "SELECT * FROM history_" + t_name + " WHERE from_id = " + "'" + fromId + "'" + " AND to_id = " + "'" + toId + "'";
        System.out.println("sql  查询conversation :::    " + sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list.size() > 0) {
            conversation = (String) list.get(0).get("conversation");
            System.out.println("conversation   :::    " + conversation);
        } else {
            String sql2 = "SELECT * FROM history_" + t_name + " WHERE from_id = " + "'" + toId + "'" + " AND to_id = " + "'" + fromId + "'";
            System.out.println("sql  查询conversation :::    " + sql2);
            List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sql2);
            if (list2.size() > 0) {
                conversation = (String) list2.get(0).get("conversation");
                System.out.println("conversation   :::    " + conversation);
            } else {
                String t_name2 = OfTenUtils.replace(toId);
                String sql3 = "SELECT * FROM history_" + t_name2 + " WHERE from_id = " + "'" + fromId + "'" + " AND to_id = " + "'" + toId + "'";
                System.out.println("sql 查询conversation  :::    " + sql3);
                List<Map<String, Object>> list3 = jdbcTemplate.queryForList(sql3);
                if (list3.size() > 0) {
                    conversation = (String) list3.get(0).get("conversation");
                    System.out.println("conversation   :::    " + conversation);
                }else {
                    String sql4 = "SELECT * FROM history_" + t_name2 + " WHERE from_id = " + "'" + toId + "'" + " AND to_id = " + "'" + fromId + "'";
                    System.out.println("sql  查询conversation :::    " + sql4);
                    List<Map<String, Object>> list4 = jdbcTemplate.queryForList(sql4);
                    if (list4.size() > 0) {
                        conversation = (String) list4.get(0).get("conversation");
                        System.out.println("conversation   :::    " + conversation);
                    }else {
                        conversation = OfTenUtils.getPid();
                        System.out.println("未找到conversation   :::    " + conversation);
                    }
                }
            }
        }
        statusMap.put("code", 1);
        statusMap.put("msg", "成功");
        statusMap.put("data",conversation);
        return GsonUtil.BeanToJson(statusMap);
    }

}
