package com.service.zgbj.mysqlTab;

import com.service.zgbj.im.SocketBean;

/**
 * Created by fengzhangwei on 2020/6/4.
 */
public interface SocketConnectService {
    void insert(SocketBean socketBean);
    Boolean whereIsOnline(String token);
    void delete(String token);
    String getToken(String uid);
    void updateToken(String token,String uid);

}
