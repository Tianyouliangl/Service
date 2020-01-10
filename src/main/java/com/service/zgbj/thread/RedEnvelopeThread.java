package com.service.zgbj.thread;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOServer;
import com.service.zgbj.im.ChatMessage;
import com.service.zgbj.im.RedEnvelopeBean;
import com.service.zgbj.im.RedEnvelopeBody;
import com.service.zgbj.im.SocketManager;
import com.service.zgbj.mysqlTab.DataBaseService;
import com.service.zgbj.mysqlTab.controller.ChatServiceImpl;
import com.service.zgbj.mysqlTab.controller.UserServiceImpl;
import com.service.zgbj.utils.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class RedEnvelopeThread extends Thread{

    @Autowired
    private ChatServiceImpl chatService;
    @Autowired
    private UserServiceImpl sqlService;
    @Autowired
    private UserServiceImpl userService;
    private Long INTERVAL_TIME = 59000L; // 每1分钟循环一次
    private Long INTERVAL_TIME_RED_ENVELOPE = 1000*60*5L;  // 红包过期时间间隔 5分钟

    @Override
    public synchronized void run() {
        try {
            System.out.println("线程开始运行-----");
            SocketIOServer service = SocketManager.getService();
            List<RedEnvelopeBean> envelope = chatService.getAllRedEnvelope();
            if (envelope != null && envelope.size() > 0){
                for (int i=0;i<envelope.size();i++){
                    RedEnvelopeBean bean = envelope.get(i);
                    Long sendTime = bean.getTime();
                    if (bean.getStatus() == RedEnvelopeBean.STATUS_UNCLAIMED){
                        long timeMillis = System.currentTimeMillis();
                        if (timeMillis >= (sendTime+INTERVAL_TIME_RED_ENVELOPE)){
                            // 红包通知
                            if (service != null){
                                ChatMessage message = GsonUtil.chatRedEnvelope(bean.getFromId());
                                String json =  GsonUtil.BeanToJson(message);
                                service.getBroadcastOperations().sendEvent("chat",json);
                            }
                            // + money
                            HashMap<String, String[]> map = new HashMap<>();
                            RedEnvelopeBody body = GsonUtil.GsonToBean(bean.getBody(), RedEnvelopeBody.class);
                            BigDecimal money = userService.getUserMoney(bean.getFromId());
                            if (money != null){
                                BigDecimal bigDecimalA = new BigDecimal(String.valueOf(money));
                                BigDecimal bigDecimalB = new BigDecimal(String.valueOf(body.getMoney()));
                                map.put("uid", new String[]{bean.getFromId()});
                                map.put("money", new String[]{String.valueOf(bigDecimalA.add(bigDecimalB))});
                                sqlService.updateUser(map);
                            }
                            chatService.updateRedEnvelope(RedEnvelopeBean.STATUS_OVERTIME,bean.getPid());
                        }
                    }
                }
            }
            sleep(INTERVAL_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
