package com.service.zgbj.mysqlTab;

import java.util.Map;

public interface UserService {
    void createUser();
    String updateUser(Map<String, String[]> map);
    void getAllUser();
    void deleteUser(String name);
    String Login(String mobile, String pwd);
}
