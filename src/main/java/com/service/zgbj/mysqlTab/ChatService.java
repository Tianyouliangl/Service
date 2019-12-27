package com.service.zgbj.mysqlTab;

import com.service.zgbj.im.ChatMessage;

import java.util.List;

public interface ChatService {
    Boolean insertChatMessage(ChatMessage msg);
    List<ChatMessage> getOffLineMsg(String id);
    Boolean removeMsg(String pid);
}
