package com.service.zgbj.mysqlTab;

import com.service.zgbj.im.ChatMessage;

public interface HistoryService {
    void createTable(String tableName);
    void insetData(ChatMessage msg,String tableName);
    String getChatMessage(String tableName,String conversation,int pageNo,int pageSize);
    void updateHistoryStatus(String tabName,int status,String pid);
    int getMsg(String tabName,String pid);

}
