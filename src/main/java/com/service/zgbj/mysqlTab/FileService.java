package com.service.zgbj.mysqlTab;

/**
 * Created by fengzhangwei on 2020/10/17.
 */
public interface FileService {
    String getFileUrl(String fileName);
    void addOrUpdateFileUrl(String fileName,String fileUrl);
    String addLikePhoto(String uid,String url,String pid,int type);
    String getLikePhoto(String uid);
    void removeLikePhoto(String uid,String pid);
}
