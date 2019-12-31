package com.service.zgbj.mysqlTab.controller;

import com.service.zgbj.mysqlTab.UserService;
import com.service.zgbj.utils.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void createUser() {

    }

    @Override
    public String updateUser(Map<String, String[]> map) {
        String sql = new String();
        String uid = null;
        HashMap<String, Object> statusMap = new HashMap<>();
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
        return GsonUtil.BeanToJson(statusMap);
    }

    @Override
    public String getAllUser(String uid) {
        HashMap<String, Object> statusMap = new HashMap<>();

        List<Map<String, Object>> list = null;
        List<HashMap<String, Object>> userList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM table_user ORDER BY online DESC ";
            System.out.println(sql);
            list = jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            list = null;
        }
        if (list != null && list.size() > 0) {
            statusMap.put("code", 1);
            statusMap.put("msg", "成功");
            System.out.println("共-----" + list.size() + "人.");
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                String uids = (String) map.get("uid");
                if (!uids.equals(uid)) {
                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("username", map.get("username"));
                    userMap.put("imageUrl", map.get("imageUrl"));
                    userMap.put("sign", map.get("sign"));
                    userMap.put("online", map.get("online"));
                    userMap.put("uid", uids);
                    userList.add(userMap);
                }
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
    public void deleteUser(String name) {
        String sql = "DELETE  FROM table_user WHERE username = ?";
        jdbcTemplate.update(sql, name);
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
            userMap.put("imageUrl", map.get("imageUrl"));
            userMap.put("location", map.get("location"));
            userMap.put("birthday", map.get("birthday"));
            userMap.put("sign", map.get("sign"));
            userMap.put("email", map.get("email"));
            userMap.put("uid", map.get("uid"));
            userMap.put("money", map.get("money"));
            userMap.put("online",map.get("online"));
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
            userMap.put("imageUrl", map.get("imageUrl"));
            userMap.put("location", map.get("location"));
            userMap.put("birthday", map.get("birthday"));
            userMap.put("sign", map.get("sign"));
            userMap.put("email", map.get("email"));
            userMap.put("uid",map.get("uid"));
            userMap.put("online",map.get("online"));
        } else {
            statusMap.put("code", 0);
            statusMap.put("msg", "请求错误");
        }
        statusMap.put("data", userMap);
        String json = GsonUtil.BeanToJson(statusMap);
        System.out.println(json);
        return GsonUtil.unicodeToUtf8(json);
    }
}
