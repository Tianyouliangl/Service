package com.service.zgbj.mysqlTab.controller;

import com.service.zgbj.im.ChatMessage;
import com.service.zgbj.mysqlTab.HistoryService;
import com.service.zgbj.utils.GsonUtil;
import com.service.zgbj.utils.OfTenUtils;
import org.omg.PortableServer.LIFESPAN_POLICY_ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HistoryServiceImpl implements HistoryService {

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
                "body VARCHAR (255)," +
                "conversation VARCHAR (255)," +
                "body_type INT (11)," +
                "msg_status INT (11)," +
                "type INT (11)," +
                "displaytime INT (11)," +
                "time BIGINT(20)" + ")" +
                "ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET ='utf8'";
        jdbcTemplate.update(sql);
    }

    @Override
    public void insetData(ChatMessage msg, String tableName) {
        String tName = OfTenUtils.replace(tableName);
        String sql = "insert into" + " history_" + tName + "(from_id,to_id,pid,type,time,body,body_type,msg_status,conversation,displaytime) value (?,?,?,?,?,?,?,?,?,?)";
        Object args[] = {msg.getFromId(), msg.getToId(), msg.getPid(), msg.getType(), msg.getTime(), msg.getBody(), msg.getBodyType(), msg.getMsgStatus(), msg.getConversation(), msg.getDisplaytime()};
        int code = jdbcTemplate.update(sql, args);
        if (code > 0) {
            System.out.println("插入历史消息成功!----表名-=== history_" + tName);
        }
    }

    @Override
    public String getChatMessage(String tableName,String conversation,int pageNo, int pageSize) {
        HashMap<String, Object> statusMap = new HashMap<>();
        String t_name = OfTenUtils.replace(tableName);
        String sql = "SELECT  * FROM history_"+t_name + " where id >" +(pageNo-1)*pageSize + " AND conversation = " + "'" + conversation + "'" + " ORDER BY time ASC LIMIT " + pageSize;
       System.out.println("sql   :::    " + sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list.size() > 0){
            statusMap.put("code",1);
            statusMap.put("msg","成功");
            statusMap.put("data", list);
        }else {
            statusMap.put("code", 0);
            statusMap.put("msg", "没有更多记录了");
        }
        String json = GsonUtil.BeanToJson(statusMap);
        return json;
    }
}
