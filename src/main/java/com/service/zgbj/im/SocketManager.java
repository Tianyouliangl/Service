package com.service.zgbj.im;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.service.zgbj.mysqlTab.controller.ChatServiceImpl;
import com.service.zgbj.mysqlTab.controller.HistoryServiceImpl;
import com.service.zgbj.mysqlTab.controller.UserServiceImpl;
import com.service.zgbj.utils.DateUtils;
import com.service.zgbj.utils.GsonUtil;
import com.service.zgbj.utils.OfTenUtils;

import java.util.HashMap;
import java.util.List;

public class SocketManager {


    public static AckRequest mAckRequest;
    //Socket客户端容器
    public static HashMap<String, SocketIOClient> mClientMap = new HashMap<>();

    private SocketConnectListener connectListener;
    private SocketDisConnectListener disConnectListener;
    private SocketDataListener chatListener;
    private UserServiceImpl sqlService;
    private static ChatServiceImpl chatService;
    public static SocketIOServer socketService;
    private HistoryServiceImpl historyService;


    public SocketManager(SocketIOServer server, UserServiceImpl userService,
                         ChatServiceImpl chatServiceImp, HistoryServiceImpl historyServiceImpl) {

        this.sqlService = userService;
        this.chatService = chatServiceImp;
        this.historyService = historyServiceImpl;

        if (server != null) {
            this.socketService = server;
            connectListener = new SocketConnectListener();
            disConnectListener = new SocketDisConnectListener();
            chatListener = new SocketDataListener();
            server.addConnectListener(connectListener);
            server.addDisconnectListener(disConnectListener);
            server.addEventListener("chat", String.class, chatListener);
        }
    }

    public static SocketIOServer getService() {
        return socketService;
    }


    /**
     * 客户端连接监听
     */
    class SocketConnectListener implements ConnectListener {

        @Override
        public void onConnect(SocketIOClient socketIOClient) {
            System.out.println("--------客户端连接------");
            SocketBean socketInfo = getClientInfo(socketIOClient);
            System.out.println("======token=====" + socketInfo.getToken());
            System.out.println("======uid=====" + socketInfo.getUid());
            System.out.println("======mobile=====" + socketInfo.getMobile());
            System.out.println("======desc=====" + socketInfo.getDesc());
            mClientMap.put(socketInfo.getToken(), socketIOClient);
            mClientMap.put(socketInfo.getUid(),socketIOClient);
            // 查询我所有的好友
            String json = sqlService.getAllFriend(socketInfo.getUid());
            FriendAllBean bean = GsonUtil.GsonToBean(json, FriendAllBean.class);
            List<FriendAllBean.DataBean> data = bean.getData();
            // 查看我的好友是否在线 在线的通知他们我上线了
            // 遍历我的好友给我的备注
            for (int i = 0; i < data.size(); i++) {
                FriendAllBean.DataBean dataBean = data.get(i);
                //  > 0 在线
                int online = dataBean.getOnline();
                SocketIOClient ct = mClientMap.get(dataBean.getUid());
                if (online > 0) {
                    String info = sqlService.getFriendUserInfo(dataBean.getUid(), socketInfo.getUid());
                    FriendBean userBean = GsonUtil.GsonToBean(info, FriendBean.class);
                    String remark = userBean.getData().getRemark();
                    int code = userBean.getCode();
                    if (code > 0) {
                        ChatMessage message = GsonUtil.chatOnLine(remark, 1);
                        if (ct != null) {
                            ct.sendEvent("chat", GsonUtil.BeanToJson(message));
                        }
                    }
                }
            }

            Boolean online = chatService.whereIsOnline(socketInfo.getUid());
            System.out.println("是否存在:" + online);
            if (online) {
                String token = chatService.getToken(socketInfo.getUid());
                if (!token.isEmpty() && !token.equals(socketInfo.getToken())) {
                    SocketIOClient ioClient = mClientMap.get(token);
                    if (ioClient != null) {
                        String time = DateUtils.getCurrentTime();
                        ChatMessage message = new ChatMessage();
                        message.setType(3);
                        message.setConversation("abcdefgl123456789");
                        message.setBody("你的账号于" + time + "在另一台" + socketInfo.getDesc() + "手机登录了Learn。如非本人操作,则密码可能一泄漏,建议修改密码。");
                        String toJson = GsonUtil.BeanToJson(message);
                        ioClient.sendEvent("chat", toJson);
                    }
                    chatService.updateToken(socketInfo.getToken(), socketInfo.getUid());
                }
            } else {
                chatService.insert(socketInfo);
            }
            System.out.println("-------当前连接人数--------" + (mClientMap.size()/2));
            sendChatMessage(socketInfo.getUid());
        }
    }


    /**
     * 客户端断开连接监听
     */
    class SocketDisConnectListener implements DisconnectListener {

        @Override
        public void onDisconnect(SocketIOClient socketIOClient) {
            HashMap<String, String[]> map = new HashMap<>();
            System.out.println("---------客户端断开连接------");
            SocketBean socketInfo = getClientInfo(socketIOClient);
            SocketIOClient ioClient = mClientMap.get(socketInfo.getToken());
            if (ioClient != null) {
                ioClient.disconnect();
            }
            mClientMap.remove(socketInfo.getToken());
            mClientMap.remove(socketInfo.getUid());
            chatService.delete(socketInfo.getToken());
            System.out.println("---------当前连接人数-------" + mClientMap.size());
            map.put("uid", new String[]{socketInfo.getUid()});
            map.put("online", new String[]{"0"});
            sqlService.updateUser(map);

            String json = sqlService.getAllFriend(socketInfo.getUid());
            FriendAllBean bean = GsonUtil.GsonToBean(json, FriendAllBean.class);
            List<FriendAllBean.DataBean> data = bean.getData();
            for (int i = 0; i < data.size(); i++) {
                FriendAllBean.DataBean dataBean = data.get(i);
                int online = dataBean.getOnline();
                if (online > 0) {
                    String info = sqlService.getFriendUserInfo(dataBean.getUid(), socketInfo.getUid());
                    FriendBean userBean = GsonUtil.GsonToBean(info, FriendBean.class);
                    String remark = userBean.getData().getRemark();
                    SocketIOClient ct = mClientMap.get(dataBean.getUid());
                    ChatMessage message = GsonUtil.chatOnLine(remark, 0);
                    if (ct != null) {
                        ct.sendEvent("chat", GsonUtil.BeanToJson(message));
                    }
                }
            }
        }
    }

    /**
     * 消息监听
     */
    class SocketDataListener implements DataListener<String> {

        @Override
        public void onData(SocketIOClient socketIOClient, String s, AckRequest ackRequest) throws Exception {
            System.out.println("监听到消息========" + s);
            mAckRequest = ackRequest;
            ChatMessage chatMessage = GsonUtil.GsonToBean(s, ChatMessage.class);
            handleChatMessage(chatMessage, ackRequest);
            historyService.createTable(OfTenUtils.replace(chatMessage.getFromId()));
            historyService.createTable(OfTenUtils.replace(chatMessage.getToId()));
            historyService.insetData(chatMessage, chatMessage.getFromId());
            if (chatMessage.getBodyType() == 3){
                chatMessage.setMsgStatus(4);
            }
            historyService.insetData(chatMessage, chatMessage.getToId());
        }
    }

    private void handleChatMessage(ChatMessage s, AckRequest ackRequest) {
        String to_id = s.getToId();
        int bodyType = s.getBodyType();
        int status = s.getMsgStatus();
        String token = chatService.getToken(to_id);
        if (token != null && !token.isEmpty()) {
            SocketIOClient client = mClientMap.get(token);
            addRedEnvelopeMsg(bodyType, status, s);
            s.setMsgStatus(2);
            if (client != null) {
                sendChatMessage(client, s);
            } else {
                addLineMsg(s);
            }
        } else {
            addLineMsg(s);
        }
        ackRequest.sendAckData(GsonUtil.BeanToJson(s));
    }

    private void addRedEnvelopeMsg(int bodyType, int status, ChatMessage s) {
        if (bodyType == 7) {
            if (status == 1) {
                RedEnvelopeBean bean = new RedEnvelopeBean();
                bean.setFromId(s.getFromId());
                bean.setToId(s.getToId());
                bean.setPid(s.getPid());
                bean.setStatus(RedEnvelopeBean.STATUS_UNCLAIMED);
                bean.setTime(s.getTime());
                bean.setBody(s.getBody());
                bean.setConversation(s.getConversation());
                chatService.addRedEnvelope(bean);
            }
        }
    }

    // 存入离线消息表
    public static void addLineMsg(ChatMessage s) {
        s.setMsgStatus(2);
        Boolean aBoolean = chatService.insertChatMessage(s);
        if (aBoolean) {
            System.out.println("-------添加离线消息成功--------");
        }
    }

    public static void sendChatMessage(SocketIOClient client, ChatMessage s) {
        String json = GsonUtil.BeanToJson(s);
        System.out.println("发送消息========" + json);
        client.sendEvent("chat", json);
    }

    public void sendChatMessage(String client) {
        List<ChatMessage> msg = chatService.getOffLineMsg(client);
        if (msg.size() > 0) {
            for (int i = 0; i < msg.size(); i++) {
                String toId = msg.get(i).getToId();
                String pid = msg.get(i).getPid();
                SocketIOClient clients = mClientMap.get(toId);
                ChatMessage chatMessage = msg.get(i);
                chatMessage.setMsgStatus(2);
                if (clients != null){
                    sendChatMessage(clients,chatMessage);
                    Boolean b = chatService.removeMsg(pid);
                    if (b) {
                        System.out.println("-------删除离线消息成功--------" + pid);
                    }
                }

            }
        }
    }

    String getClientUid(SocketIOClient client) {
        String uid = client.getHandshakeData().getSingleUrlParam("uid");
        if (uid == null || uid == "") {
            return null;
        }
        return uid;
    }

    SocketBean getClientInfo(SocketIOClient client) {
        SocketBean socketBean = new SocketBean();
        String desc = client.getHandshakeData().getSingleUrlParam("desc");
        String token = client.getHandshakeData().getSingleUrlParam("token");
        String uid = client.getHandshakeData().getSingleUrlParam("uid");
        String mobile = client.getHandshakeData().getSingleUrlParam("mobile");
        socketBean.setMobile(mobile);
        socketBean.setUid(uid);
        socketBean.setToken(token);
        socketBean.setDesc(desc);
        return socketBean;
    }

}
