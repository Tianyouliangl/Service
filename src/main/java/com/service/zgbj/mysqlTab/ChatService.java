package com.service.zgbj.mysqlTab;

import com.service.zgbj.im.ChatMessage;
import com.service.zgbj.im.RedEnvelopeBean;

import java.util.List;

public interface ChatService {
    Boolean insertChatMessage(ChatMessage msg);
    List<ChatMessage> getOffLineMsg(String id);
    Boolean removeMsg(String pid);
    List<RedEnvelopeBean> getAllRedEnvelope();
    void updateRedEnvelope(int status,String pid);
    void addRedEnvelope(RedEnvelopeBean bean);
}
