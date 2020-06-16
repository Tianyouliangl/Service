package com.service.zgbj.mysqlTab.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.service.zgbj.im.ChatMessage;
import com.service.zgbj.im.SocketManager;
import com.service.zgbj.mysqlTab.UserService;
import com.service.zgbj.utils.GsonUtil;
import com.service.zgbj.utils.OfTenUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public void createTableFriend(String uid) {
        // remark 备注
        String tName = OfTenUtils.replace(uid);
        String sql = "CREATE TABLE IF NOT EXISTS " + " friend_" + tName + " (" +
                "id INT (11) NOT NULL AUTO_INCREMENT PRIMARY KEY ," +
                "age INT (11)," +
                "birthday VARCHAR(255)," +
                "email VARCHAR(255)," +
                "image_url VARCHAR(255)," +
                "location VARCHAR(255)," +
                "mobile VARCHAR(255)," +
                "online INT(11)," +
                "sex VARCHAR (255)," +
                "sign VARCHAR(255)," +
                "uid VARCHAR(255)," +
                "username VARCHAR(255)," +
                "source INT (11)," +
                "remark VARCHAR (255)" + ")" +
                "ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET ='utf8'";
        System.out.println("Sql:-------" + sql);
        jdbcTemplate.update(sql);
    }

    @Override
    public void createTableFriendMsg(String uid) {

        // friend_type 当前状态 拒绝/同意 0：同意 1 拒绝 2 等待
        // source 来源 扫一扫/手机号 0 1
        // content 验证信息 （我是***）
        String tName = OfTenUtils.replace(uid);
        String sql = "CREATE TABLE IF NOT EXISTS " + " friend_msg_" + tName + " (" +
                "id INT (11) NOT NULL AUTO_INCREMENT PRIMARY KEY ," +
                "to_id VARCHAR (255)," +
                "from_id VARCHAR (255)," +
                "pid VARCHAR (255)," +
                "friend_type INT (11)," +
                "source INT (11)," +
                "content VARCHAR (255)" + ")" +
                "ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET ='utf8'";
        System.out.println("Sql:-------" + sql);
        jdbcTemplate.update(sql);
    }

    @Override
    public String updateUser(Map<String, String[]> map) {
        String sql = new String();
        String uid = null;
        HashMap<String, Object> statusMap = new HashMap<>();
        if (map.size() == 2) {
            List<String> uid_list = new ArrayList<>();
            List<Integer> code_list = new ArrayList<>();
            if (map.get("uid") != null && map.get("online") != null) {
                String sql_user = "SELECT * FROM table_user";
//            String sql = "SELECT * FROM friend_ + " + OfTenUtils.replace(uid) + " where friend_type = 1 ";
                System.out.println(sql_user);
                List<Map<String, Object>> user_list = jdbcTemplate.queryForList(sql_user);
                if (user_list.size() > 1) {
                    for (int i = 0; i < user_list.size(); i++) {
                        Map<String, Object> map_user = user_list.get(i);
                        if (!map_user.get("uid").equals(map.get("uid"))) {
                            String uid_user = (String) map_user.get("uid");
                            uid_list.add(uid_user);
                        }
                    }
                    for (Map.Entry<String, String[]> entry : map.entrySet()) {
                        String key = entry.getKey();
                        String[] value = entry.getValue();
                        String value2 = value[0];
                        if (!key.equals("uid")) {
                            sql += key + "=" + "'" + value2 + "'" + ",";
                        } else {
                            uid = value2;
                        }
                    }
                    String sql2 = sql.trim().substring(0, sql.length() - 1);
//                    for (int j = 0; j < uid_list.size(); j++) {
//                        String s = uid_list.get(j);
//                        String replace = OfTenUtils.replace(s);
//                        String sql_friend = "SELECT * FROM friend_" + replace + " WHERE uid = " + "'" + uid + "'";
//                        System.out.println(sql_friend);
//                        List<Object> list = jdbcTemplate.query(sql_friend, new Object[]{}, new RowMapper<Object>() {
//                            @Override
//                            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
//                                return null;
//                            }
//                        });
//                        if (list.size() <= 0 || list == null) {
//                            Iterator<String> it = uid_list.iterator();
//                            while(it.hasNext()){
//                                String x = it.next();
//                                if(x.equals(s)){
//                                    it.remove();
//                                }
//                            }
//                        }
//                    }

                    if (uid_list.size() > 0) {
                        for (int k = 0; k < user_list.size(); k++) {
                            String s = uid_list.get(k);
                            String replace = OfTenUtils.replace(s);
                            String mSql = "UPDATE friend_" + replace + " SET " + sql2 + " WHERE uid = " + "'" + uid + "'";
                            System.out.println(mSql);
                            int code = jdbcTemplate.update(mSql);
                            code_list.add(code);
                        }
                    }
                    System.out.println("uid_List:::::" + uid_list.size() + "--------code_list:::::" + code_list.size());
                    if (code_list.size() == uid_list.size()) {
                        for (Map.Entry<String, String[]> entry : map.entrySet()) {
                            String key = entry.getKey();
                            String[] value = entry.getValue();
                            String value2 = value[0];
                            if (!key.equals("uid")) {
                                sql += key + "=" + "'" + value2 + "'" + ",";
                            } else {
                                uid = value2;
                            }
                        }
                        if (!sql.isEmpty() && uid != null) {
                            String mSql = "UPDATE table_user SET " + sql2 + " WHERE uid = " + "'" + uid + "'";
                            System.out.println(mSql);
                            int code = jdbcTemplate.update(mSql);
                            if (code != 0) {
                                statusMap.put("code", 1);
                                statusMap.put("msg", "更改成功");
                            } else {
                                statusMap.put("code", 0);
                                statusMap.put("msg", "更改失败");
                            }
                        }
                    } else {
                        statusMap.put("code", 0);
                        statusMap.put("msg", "更改失败");
                    }
                }
            }
        } else {
            for (Map.Entry<String, String[]> entry : map.entrySet()) {
                String key = entry.getKey();
                String[] value = entry.getValue();
                String value2 = value[0];
                if (!key.equals("uid")) {
                    sql += key + "=" + "'" + value2 + "'" + ",";
                } else {
                    uid = value2;
                }
            }
            if (!sql.isEmpty() && uid != null) {
                String sql2 = sql.trim().substring(0, sql.length() - 1);
                String mSql = "UPDATE table_user SET " + sql2 + " WHERE uid = " + "'" + uid + "'";
                System.out.println(mSql);
                int code = jdbcTemplate.update(mSql);
                if (code != 0) {
                    statusMap.put("code", 1);
                    statusMap.put("msg", "更改成功");
                } else {
                    statusMap.put("code", 0);
                    statusMap.put("msg", "更改失败");
                }
            }
        }

        return GsonUtil.BeanToJson(statusMap);
    }

    @Override
    public String getAllFriend(String uid) {
        HashMap<String, Object> statusMap = new HashMap<>();

        List<Map<String, Object>> list = null;
        List<Map<String, Object>> userList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM friend_" + OfTenUtils.replace(uid) + " ORDER BY online DESC ";
            System.out.println(sql);
            list = jdbcTemplate.query(sql, new Object[]{}, new RowMapper<Map<String, Object>>() {
                @Override
                public HashMap<String, Object> mapRow(ResultSet resultSet, int i) throws SQLException {
                    HashMap row = new HashMap();
                    row.put("username", resultSet.getString("username"));
                    row.put("mobile", resultSet.getString("mobile"));
                    row.put("age", resultSet.getInt("age"));
                    row.put("sex", resultSet.getString("sex"));
                    row.put("imageUrl", resultSet.getString("image_url"));
                    row.put("location", resultSet.getString("location"));
                    row.put("birthday", resultSet.getString("birthday"));
                    row.put("sign", resultSet.getString("sign"));
                    row.put("email", resultSet.getString("email"));
                    row.put("uid", resultSet.getString("uid"));
                    row.put("online", resultSet.getInt("online"));
                    row.put("isFriend", true);
                    row.put("source", resultSet.getInt("source"));
                    row.put("remark", resultSet.getString("remark"));
                    return row;
                }
            });
        } catch (Exception e) {
            list = null;
        }
        if (list != null && list.size() > 0) {
            statusMap.put("code", 1);
            statusMap.put("msg", "成功");
            System.out.println("共-----" + list.size() + "人.");
            for (int i = 0; i < list.size(); i++) {
                userList.add(list.get(i));
            }
        } else {
            statusMap.put("code", 0);
            statusMap.put("msg", "暂无联系人.");
        }
        statusMap.put("data", userList);
        String json = GsonUtil.BeanToJson(statusMap);
        System.out.println(json);
        return GsonUtil.unicodeToUtf8(json);
    }

    @Override
    public String getAllFriendMsg(String uid) {
        String tName = OfTenUtils.replace(String.valueOf(uid));
        HashMap<String, Object> statusMap = new HashMap<>();
        List<Map<String, Object>> list = null;
        List<HashMap<String, Object>> userList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM friend_msg_" + tName + " ORDER BY friend_type DESC";
            System.out.println(sql);
            list = jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            list = null;
        }
        if (list != null && list.size() > 0) {
            statusMap.put("code", 1);
            statusMap.put("msg", "成功");
            System.out.println("共-----" + list.size() + "条.");
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                HashMap<String, Object> userMap = new HashMap<>();
                userMap.put("to_id", map.get("to_id"));
                userMap.put("from_id", map.get("from_id"));
                userMap.put("pid", map.get("pid"));
                userMap.put("friend_type", map.get("friend_type"));
                userMap.put("source", map.get("source"));
                userMap.put("content", map.get("content"));
                String json_from = getUserInfo(map.get("from_id").toString());
                String json_to = getUserInfo(map.get("to_id").toString());
                userMap.put("fromUserInfo",json_from);
                userMap.put("toUserInfo",json_to);
                userList.add(userMap);
            }
        } else {
            statusMap.put("code", 0);
            statusMap.put("msg", "暂无消息.");
        }
        statusMap.put("data", userList);
        String json = GsonUtil.BeanToJson(statusMap);
        System.out.println(json);
        return GsonUtil.unicodeToUtf8(json);
    }

    @Override
    public String getAllAddFriendMsg(String uid) {
        String tName = OfTenUtils.replace(String.valueOf(uid));
        HashMap<String, Object> statusMap = new HashMap<>();
        List<Map<String, Object>> list = null;
        List<HashMap<String, Object>> userList = new ArrayList<>();
        try {
            // friend_type ==  1 同意添加好友/是好友    2 拒绝添加好友 3 请求添加好友
            String sql = "SELECT * FROM friend_msg_" + tName + " WHERE friend_type = 2";
//            String sql = "SELECT * FROM friend_ + " + OfTenUtils.replace(uid) + " where friend_type = 1 ";
            System.out.println(sql);
            list = jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            list = null;
        }
        if (list != null && list.size() > 0) {
            statusMap.put("code", 1);
            statusMap.put("msg", "成功");
            System.out.println("共-----" + list.size() + "条.");
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                HashMap<String, Object> userMap = new HashMap<>();
                userMap.put("to_id", map.get("to_id"));
                userMap.put("from_id", map.get("from_id"));
                userMap.put("pid", map.get("pid"));
                userMap.put("friend_type", map.get("friend_type"));
                userMap.put("source", map.get("source"));
                userMap.put("content", map.get("content"));
                userList.add(userMap);
            }
        } else {
            statusMap.put("code", 0);
            statusMap.put("msg", "暂无消息.");
        }
        statusMap.put("data", userList);
        String json = GsonUtil.BeanToJson(statusMap);
        System.out.println(json);
        return GsonUtil.unicodeToUtf8(json);
    }

    @Override
    public String getFriendUserInfo(String id, String uid) {
        String tName = OfTenUtils.replace(String.valueOf(id));
        HashMap<String, Object> statusMap = new HashMap<>();
        Map<String, Object> map = null;
        HashMap<String, Object> userMap = new HashMap<>();
        try {
            String sql = "SELECT * FROM friend_" + tName + " WHERE uid = " + "'" + uid + "'";
            System.out.println(sql);
            map = jdbcTemplate.queryForMap(sql);
        } catch (Exception e) {
            map = null;
            statusMap.put("code", 0);
            statusMap.put("msg", "获取好友信息失败.");
        }
        if (map != null && map.size() > 0) {
            System.out.println("共-----" + map.size() + "条.");
            userMap.put("username", map.get("username"));
            userMap.put("mobile", map.get("mobile"));
            userMap.put("age", map.get("age"));
            userMap.put("sex", map.get("sex"));
            userMap.put("imageUrl", map.get("image_url"));
            userMap.put("location", map.get("location"));
            userMap.put("birthday", map.get("birthday"));
            userMap.put("sign", map.get("sign"));
            userMap.put("email", map.get("email"));
            userMap.put("uid", map.get("uid"));
            userMap.put("online", map.get("online"));
            userMap.put("isFriend", true);
            userMap.put("source", map.get("source"));
            userMap.put("remark", map.get("remark"));
            statusMap.put("code", 1);
            statusMap.put("msg", "成功");
        } else {
            statusMap.put("code", 0);
            statusMap.put("msg", "暂无好友信息.");
        }
        statusMap.put("data", userMap);
        String json = GsonUtil.BeanToJson(statusMap);
        System.out.println(json);
        return GsonUtil.unicodeToUtf8(json);
    }

    @Override
    public void deleteUser(String name) {

    }

    @Override
    public String Login(String mobile, String pwd) {
        HashMap<String, Object> statusMap = new HashMap<>();
        HashMap<String, Object> userMap = new HashMap<>();
        Map<String, Object> map = null;
        try {
            String sql = "SELECT * FROM table_user WHERE mobile = " + mobile + " AND  password = " + pwd;
            System.out.println(sql);
            map = jdbcTemplate.queryForMap(sql);
        } catch (Exception e) {
            map = null;
        }
        if (map != null && map.size() > 0) {
            statusMap.put("code", 1);
            statusMap.put("msg", "登录成功");
            userMap.put("username", map.get("username"));
            userMap.put("mobile", map.get("mobile"));
            userMap.put("age", map.get("age"));
            userMap.put("sex", map.get("sex"));
            userMap.put("imageUrl", map.get("image_url"));
            userMap.put("location", map.get("location"));
            userMap.put("birthday", map.get("birthday"));
            userMap.put("sign", map.get("sign"));
            userMap.put("email", map.get("email"));
            userMap.put("uid", map.get("uid"));
            userMap.put("money", map.get("money"));
            userMap.put("online", map.get("online"));
            userMap.put("token",OfTenUtils.getRandomUid());
        } else {
            statusMap.put("code", 0);
            statusMap.put("msg", "用户不存在或者密码错误");
        }
        statusMap.put("data", userMap);
        String json = GsonUtil.BeanToJson(statusMap);
        System.out.println(json);
        return GsonUtil.unicodeToUtf8(json);
    }

    @Override
    public String register(String email, String imageUrl, String location, String mobile, String pwd, String sex, String name, String uid) {
        HashMap<String, Object> statusMap = new HashMap<>();
        Map<String, Object> map = null;
        try {
            String sql = "SELECT * FROM table_user WHERE mobile = " + mobile;
            System.out.println(sql);
            map = jdbcTemplate.queryForMap(sql);
        } catch (Exception e) {
            map = null;
        }
        if (map != null && map.size() > 0) {
            statusMap.put("code", 0);
            statusMap.put("msg", "用户已存在");
        } else {
            try {
                String sql = "INSERT into table_user (age,birthday,email,image_url,location,mobile,money,online,password,sex,sign,uid,username) VALUE (?,?,?,?,?,?,?,?,?,?,?,?,?)";
                System.out.println(sql);
                Object args[] = {20, "2000-01-01", email, imageUrl, location, mobile, 100.0, 0, pwd, sex, "退一步海阔天空.", uid, name};
                int code = jdbcTemplate.update(sql, args);
                if (code > 0) {
                    statusMap.put("code", 1);
                    statusMap.put("msg", "注册成功");
                } else {
                    statusMap.put("code", 0);
                    statusMap.put("msg", "注册失败");
                }
            } catch (Exception e) {
                statusMap.put("code", 0);
                statusMap.put("msg", "注册失败:" + e.toString());
            }

        }
        statusMap.put("data", new HashMap<>());
        String json = GsonUtil.BeanToJson(statusMap);
        System.out.println(json);
        return GsonUtil.unicodeToUtf8(json);
    }

    @Override
    public String getUserInfo(String uid) {
        HashMap<String, Object> statusMap = new HashMap<>();
        HashMap<String, Object> userMap = new HashMap<>();
        Map<String, Object> map = null;
        try {
            String sql = "SELECT * FROM table_user WHERE uid = " + "'" + uid + "'";
            System.out.println(sql);
            map = jdbcTemplate.queryForMap(sql);
        } catch (Exception e) {
            map = null;
        }
        if (map != null && map.size() > 0) {
            statusMap.put("code", 1);
            statusMap.put("msg", "成功");
            userMap.put("username", map.get("username"));
            userMap.put("mobile", map.get("mobile"));
            userMap.put("age", map.get("age"));
            userMap.put("sex", map.get("sex"));
            userMap.put("imageUrl", map.get("image_url"));
            userMap.put("location", map.get("location"));
            userMap.put("birthday", map.get("birthday"));
            userMap.put("sign", map.get("sign"));
            userMap.put("email", map.get("email"));
            userMap.put("uid", map.get("uid"));
            userMap.put("online", map.get("online"));
        } else {
            statusMap.put("code", 0);
            statusMap.put("msg", "请求错误");
        }
        statusMap.put("data", userMap);
        String json = GsonUtil.BeanToJson(statusMap);
        System.out.println(json);
        return GsonUtil.unicodeToUtf8(json);
    }

    @Override
    public String getUserInfoPhone(String uid, String phone) {
        HashMap<String, Object> statusMap = new HashMap<>();
        HashMap<String, Object> userMap = new HashMap<>();
        Map<String, Object> map = null;
        Map<String, Object> map2 = null;
        try {
            String sql = "SELECT * FROM table_user WHERE mobile = " + "'" + phone + "'";
            System.out.println(sql);
            map = jdbcTemplate.queryForMap(sql);
        } catch (Exception e) {
            map = null;
        }
        if (map != null && map.size() > 0) {
            String tName = OfTenUtils.replace(String.valueOf(uid));
            try {
                String sql2 = "SELECT * FROM " + " friend_" + tName + " WHERE mobile = " + "'" + phone + "'";
                System.out.println(sql2);
                map2 = jdbcTemplate.queryForMap(sql2);
            } catch (Exception e) {
                map2 = null;
            }
            statusMap.put("code", 1);
            statusMap.put("msg", "成功");
            userMap.put("username", map.get("username"));
            userMap.put("mobile", map.get("mobile"));
            userMap.put("age", map.get("age"));
            userMap.put("sex", map.get("sex"));
            userMap.put("imageUrl", map.get("image_url"));
            userMap.put("location", map.get("location"));
            userMap.put("birthday", map.get("birthday"));
            userMap.put("sign", map.get("sign"));
            userMap.put("email", map.get("email"));
            userMap.put("uid", map.get("uid"));
            userMap.put("online", map.get("online"));
            if (map2 != null && map2.size() > 0) {
                userMap.put("isFriend", true);
                userMap.put("source", map2.get("source"));
                userMap.put("remark", map2.get("remark"));
            } else {
                userMap.put("isFriend", false);
            }
        } else {
            statusMap.put("code", 0);
            statusMap.put("msg", "无结果");
        }
        statusMap.put("data", userMap);
        String json = GsonUtil.BeanToJson(statusMap);
        System.out.println(json);
        return GsonUtil.unicodeToUtf8(json);
    }

    @Override
    public String addFriendMsg(String to_id, String from_id, String pid, int friend_type, int source, String content) {
        createTable(to_id, from_id);
        String json = GsonUtil.BeanToJson(updateInsertTable(to_id, from_id, pid, friend_type, source, content));
        SocketIOClient client = SocketManager.mClientMap.get(to_id);
        if (client != null){
            String info = getUserInfo(to_id);
            try {
                JSONObject object = new JSONObject(info);
                if (object.has("username")){
                    Object username = object.get("username");
                    ChatMessage message = new ChatMessage();
                    message.setType(2);
                    message.setBody(username+"请求加为好友");
                    SocketManager.sendChatMessage(client,message);
                }
            }catch (Exception e){

            }
        }
        System.out.println(json);
        return GsonUtil.unicodeToUtf8(json);
    }

    @Override
    public String addFriend(String to_id, String from_id, String pid, int friend_type, int source) {
        HashMap<String, Object> statusMap = new HashMap<>();
        // friendtype =  拒绝/同意 0：同意 / 1 拒绝
        // friendtype = 0 or friendtype =  1   处理 to_id
        // friendtype =  1 不添加到好友列表                    更改双方friend_msg_you id  表
        // friendtype =  0 各添加到双方好友列表 friend_you id  更改双方friend_msg_you id  表
        Boolean msg = updateFriendMsg(from_id, to_id, pid, friend_type);
        if (msg) {
            if (friend_type == 0) {
                Boolean aBoolean = addFriendTable(from_id, to_id, source);
                if (aBoolean) {
                    Boolean table = addFriendTable(to_id, from_id, source);
                    if (table) {
                        statusMap.put("code", 1);
                        statusMap.put("msg", "成功");
                    }
                }
            }
        } else {
            statusMap.put("code", 0);
            statusMap.put("msg", "失败");
        }
        statusMap.put("data", new HashMap<>());
        return GsonUtil.BeanToJson(statusMap);
    }

    private Boolean addFriendTable(String from_id, String to_id, int source) {
        Map<String, Object> map = null;
        try {
            String sql = "SELECT * FROM table_user WHERE uid = " + "'" + from_id + "'";
            System.out.println(sql);
            map = jdbcTemplate.queryForMap(sql);
        } catch (Exception e) {
            map = null;
        }
        if (map != null) {
            try {
                String sql = "INSERT into friend_" + OfTenUtils.replace(to_id) + " (age,birthday,email,image_url,location,mobile,online,sex,sign,uid,username,source,remark) VALUE (?,?,?,?,?,?,?,?,?,?,?,?,?)";
                System.out.println(sql);
                Object args[] = {map.get("age"), map.get("birthday"), map.get("email"), map.get("image_url"), map.get("location"), map.get("mobile"), map.get("online"), map.get("sex"), map.get("sign"), map.get("uid"), map.get("username"), source, map.get("username")};
                int code = jdbcTemplate.update(sql, args);
                if (code > 0) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                System.out.println(e.toString());
                return false;
            }

        } else {
            return false;
        }
    }

    private Boolean updateFriendMsg(String from_id, String to_id, String pid, int friend_type) {
        String sql_from_update = "UPDATE " + "friend_msg_" + OfTenUtils.replace(from_id) + " SET friend_type =" + friend_type + " WHERE pid = " + "'" + pid + "'";
        System.out.println(sql_from_update);
        int code = jdbcTemplate.update(sql_from_update);
        if (code > 0) {
            String sql_to_update = "UPDATE " + "friend_msg_" + OfTenUtils.replace(to_id) + " SET friend_type =" + friend_type + " WHERE pid = " + "'" + pid + "'";
            System.out.println(sql_to_update);
            int code_to = jdbcTemplate.update(sql_to_update);
            if (code_to > 0) {
                return true;
            }
        }
        return false;
    }

    private HashMap<String, Object> updateInsertTable(String to_id, String from_id, String pid, int friend_type, int source, String content) {
        HashMap<String, Object> statusMap = new HashMap<>();
        Map<String, Object> map_from_select = null;
        Map<String, Object> map_to_select = null;
        String table_from_name = OfTenUtils.replace(from_id);
        String table_to_name = OfTenUtils.replace(to_id);
        try {
            String sql_from_select = "SELECT * FROM " + " friend_msg_" + table_from_name + " WHERE pid = " + "'" + pid + "'";
            System.out.println(sql_from_select);
            map_from_select = jdbcTemplate.queryForMap(sql_from_select);
        } catch (Exception e) {
            map_from_select = null;
        }
        if (map_from_select != null && map_from_select.size() > 0) {
            String sql_from_update = "UPDATE " + "friend_msg_" + table_from_name + " SET friend_type =" + friend_type + " WHERE pid = " + "'" + pid + "'";
            System.out.println(sql_from_update);
            int code = jdbcTemplate.update(sql_from_update);
            if (code > 0) {
                try {
                    String sql_to_select = "SELECT * FROM " + " friend_msg_" + table_to_name + " WHERE pid = " + "'" + pid + "'";
                    System.out.println(sql_to_select);
                    map_to_select = jdbcTemplate.queryForMap(sql_to_select);
                } catch (Exception e) {
                    map_to_select = null;
                }
                if (map_to_select != null && map_to_select.size() > 0) {
                    String sql_to_update = "UPDATE " + "friend_msg_" + table_to_name + " SET friend_type =" + friend_type + " WHERE pid = " + "'" + pid + "'";
                    System.out.println(sql_to_update);
                    int code_to = jdbcTemplate.update(sql_to_update);
                    if (code_to > 0) {
                        statusMap.put("code", 1);
                        statusMap.put("msg", "成功");
                    } else {
                        statusMap.put("code", 0);
                        statusMap.put("msg", "失败");
                    }
                } else {
                    String sql_to_insert = "INSERT into friend_msg_" + table_to_name + " (to_id,from_id,pid,friend_type,source,content) VALUE (?,?,?,?,?,?)";
                    System.out.println(sql_to_insert);
                    Object args[] = {to_id, from_id, pid, friend_type, source, content};
                    int code_to = jdbcTemplate.update(sql_to_insert, args);
                    if (code_to > 0) {
                        statusMap.put("code", 1);
                        statusMap.put("msg", "成功");
                    } else {
                        statusMap.put("code", 0);
                        statusMap.put("msg", "失败");
                    }
                }
            } else {
                statusMap.put("code", 0);
                statusMap.put("msg", "失败");
            }
        } else {
            try {
                String sql_from_insert = "INSERT into friend_msg_" + table_from_name + " (to_id,from_id,pid,friend_type,source,content) VALUE (?,?,?,?,?,?)";
                System.out.println(sql_from_insert);
                Object args[] = {to_id, from_id, pid, friend_type, source, content};
                int code = jdbcTemplate.update(sql_from_insert, args);
                if (code > 0) {
                    try {
                        String sql_to_select = "SELECT * FROM " + " friend_msg_" + table_to_name + " WHERE pid = " + "'" + pid + "'";
                        System.out.println(sql_to_select);
                        map_to_select = jdbcTemplate.queryForMap(sql_to_select);
                    } catch (Exception e) {
                        map_to_select = null;
                    }
                    if (map_to_select != null && map_to_select.size() > 0) {
                        String sql_to_update = "UPDATE " + "friend_msg_" + table_to_name + " SET friend_type =" + friend_type + " WHERE pid = " + "'" + pid + "'";
                        System.out.println(sql_to_update);
                        int code_to = jdbcTemplate.update(sql_to_update);
                        if (code_to > 0) {
                            statusMap.put("code", 1);
                            statusMap.put("msg", "成功");
                        } else {
                            statusMap.put("code", 0);
                            statusMap.put("msg", "失败");
                        }
                    } else {
                        String sql_to_insert = "INSERT into friend_msg_" + table_to_name + " (to_id,from_id,pid,friend_type,source,content) VALUE (?,?,?,?,?,?)";
                        System.out.println(sql_to_insert);
                        Object args2[] = {to_id, from_id, pid, friend_type, source, content};
                        int code_to = jdbcTemplate.update(sql_to_insert, args2);
                        if (code_to > 0) {
                            statusMap.put("code", 1);
                            statusMap.put("msg", "成功");
                        } else {
                            statusMap.put("code", 0);
                            statusMap.put("msg", "失败");
                        }
                    }

                } else {
                    statusMap.put("code", 0);
                    statusMap.put("msg", "失败");
                }
            } catch (Exception e) {
                System.out.println("错误:" + e.toString());
            }

        }
        statusMap.put("data", new HashMap<>());
        return statusMap;

    }

    private void createTable(String to_id, String from_id) {
        createTableFriend(to_id);
        createTableFriend(from_id);
        createTableFriendMsg(to_id);
        createTableFriendMsg(from_id);
    }

    @Override
    public BigDecimal getUserMoney(String uid) {
        BigDecimal money = null;
        String sql = "SELECT * FROM table_user WHERE uid = " + "'" + uid + "'";
        System.out.println(sql);
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        if (maps.size() > 0) {
            money = (BigDecimal) maps.get(0).get("money");
        }
        return money;
    }
}
