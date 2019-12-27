package com.service.zgbj.mysqlTab;

import java.util.Map;

public interface UserService {
    void createUser();
    String updateUser(Map<String, String[]> map);
    String getAllUser(String uid);
    void deleteUser(String name);
    String Login(String mobile, String pwd);
    String getUserInfo(String uid);
}
