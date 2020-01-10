package com.service.zgbj.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.service.zgbj.im.ChatMessage;
import com.service.zgbj.im.RedEnvelopeBean;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author : fengzhangwei
 * date : 2019/12/19
 */
public class GsonUtil {
    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    private GsonUtil() {
    }

    /**
     * 转成json
     *
     * @param object
     * @return
     */
    public static String BeanToJson(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }

    /**
     * 转成bean
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> T GsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }

    /**
     * 转成list
     * 泛型在编译期类型被擦除导致报错
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> List<T> GsonToList(String gsonString, Class<T> cls) {
        List<T> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
            }.getType());
        }
        return list;
    }

    /**
     * 转成list
     * 解决泛型问题
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public <T> List<T> jsonToList(String json, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }


    /**
     * 转成list中有map的
     *
     * @param gsonString
     * @return
     */
    public static <T> List<Map<String, T>> GsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }

    /**
     * 转成map的
     *
     * @param gsonString
     * @return
     */
    public static <T> Map<String, T> GsonToMaps(String gsonString) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }

    public static ChatMessage ackChatMessage(ChatMessage msg) {
        msg.setMsgStatus(2);
        return msg;
    }

    public static ChatMessage chatOnLine(String fId,int type){
        ChatMessage msg = new ChatMessage();
        msg.setFromId(fId);
        msg.setTime(System.currentTimeMillis());
        if (type == 1){
            msg.setType(2);
            msg.setBody(fId+"上线了!");
        }else if (type == 0){
            msg.setType(2);
            msg.setBody(fId+"下线了!");
        }
       return msg;
    }

    public static ChatMessage chatRedEnvelope(String fId){
        ChatMessage msg = new ChatMessage();
        msg.setFromId(fId);
        msg.setTime(System.currentTimeMillis());
        msg.setType(2);
        msg.setBodyType(7);
        msg.setMsgStatus(RedEnvelopeBean.STATUS_OVERTIME);
        msg.setBody("红包超时未领取,已退还到您的账户");
        return msg;
    }

    public static String unicodeToUtf8(String s) {
        String json = null;
        try {
            json =   new String(s.getBytes("utf-8"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return json;
    }

}
