package com.service.zgbj.mysqlTab;

import java.math.BigDecimal;
import java.util.Map;

public interface UserService {
    void createTableFriend(String uid);
    void createTableFriendMsg(String uid);
    String updateUser(Map<String, String[]> map);
    String getAllFriend(String uid);
    String getAllFriendMsg(String uid);
    String getAllAddFriendMsg(String uid);
    String getFriendUserInfo(String id,String uid);
    void deleteUser(String name);
    String Login(String mobile, String pwd);
    String register(String email,String imageUrl,String location,String mobile, String pwd,String sex,String name,String uid);
    String getUserInfo(String uid);
    String getUserInfoPhone(String uid,String phone);
    String addFriendMsg(String to_id,String from_id,String pid,int friend_type,int source,String content);
    String addFriend(String to_id,String from_id,String pid,int friend_type,int source);
    BigDecimal getUserMoney(String uid);
}
