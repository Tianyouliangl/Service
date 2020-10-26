package com.service.zgbj.mysqlTab.controller;

import com.service.zgbj.im.LikePhotoBean;
import com.service.zgbj.mysqlTab.FileService;
import com.service.zgbj.utils.GsonUtil;
import com.service.zgbj.utils.OfTenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fengzhangwei on 2020/10/17.
 */
@Service
public class FileImpl implements FileService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String getFileUrl(String fileName) {
        createTable();
        HashMap<String, Object> statusMap = new HashMap<>();
        String sql_select = "SELECT * FROM file_url" + " WHERE filename = " + "'" + fileName + "'";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql_select);
        if (list.size() > 0) {
            statusMap.put("code", 1);
            statusMap.put("msg", "成功");
            Map<String, Object> map = list.get(0);
            String fileUrl = (String) map.get("fileurl");
            statusMap.put("data", fileUrl);
        } else {
            statusMap.put("code", 0);
            statusMap.put("msg", "失败");
            statusMap.put("data", null);
        }
        System.out.println("File-----sql:" + sql_select + "list == " + list.size());
        return GsonUtil.BeanToJson(statusMap);
    }

    @Override
    public void addOrUpdateFileUrl(String fileName, String fileUrl) {
        createTable();
        String sql_select = "SELECT * FROM file_url" + " WHERE filename = " + "'" + fileName + "'";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql_select);
        if (list.size() <= 0) {
            String sql = "insert into" + " file_url" + "(filename,fileurl) value (?,?)";
            Object args[] = {fileName, fileUrl};
            int code = jdbcTemplate.update(sql, args);
            if (code > 0) {
                System.out.println("插入FileUrl成功!----表名-=== file_url");
            }
        } else {
            String sql = "UPDATE file_url" + "   SET fileurl = " + "'" + fileUrl + "'" + " WHERE filename = " + "'" + fileName + "'";
            System.out.println(sql);
            int update = jdbcTemplate.update(sql);
            if (update > 0) {
                System.out.println("修改FileUrl成功!----表名-=== file_url");
            }
        }
    }

    @Override
    public String addLikePhoto(String uid, String url, String pid,int type) {
        createLikePhotoTable(uid);
        String replace = OfTenUtils.replace(uid);
        HashMap<String, Object> statusMap = new HashMap<>();
        String sql_select = "SELECT * FROM likePhoto_" + replace + " WHERE url = " + "'" + url + "'" + "AND pid = " + "'" + pid + "'";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql_select);

        if (list.size() > 0) {
            statusMap.put("code", 0);
            statusMap.put("msg", "表情已添加过");
        } else {
            String sql = "insert into" + " likePhoto_" + replace + "(url,pid,type,time) value (?,?,?,?)";
            Object args[] = {url, pid,type,System.currentTimeMillis()};
            int code = jdbcTemplate.update(sql, args);
            if (code > 0) {
                statusMap.put("code", 1);
                statusMap.put("msg", "添加成功");
            } else {
                statusMap.put("code", 0);
                statusMap.put("msg", "添加失败");

            }
        }
        statusMap.put("data","");
        return GsonUtil.BeanToJson(statusMap);
    }

    @Override
    public String getLikePhoto(String uid) {
        createLikePhotoTable(uid);
        HashMap<String, Object> statusMap = new HashMap<>();
        List<LikePhotoBean> photoBeanList = new ArrayList<>();
        String sql = "SELECT * FROM likePhoto_" + OfTenUtils.replace(uid) + " ORDER BY time DESC";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        for (int i = 0; i < list.size(); i++) {
            LikePhotoBean bean = new LikePhotoBean();
            Map<String, Object> map = list.get(i);
            String url = (String) map.get("url");
            String pid = (String) map.get("pid");
            int type = (int) map.get("type");
            bean.setUrl(url);
            bean.setPid(pid);
            bean.setType(type);
            photoBeanList.add(bean);
        }
        statusMap.put("code", 1);
        statusMap.put("msg", "成功");
        statusMap.put("data", photoBeanList);
        return GsonUtil.BeanToJson(statusMap);
    }

    @Override
    public void removeLikePhoto(String uid, String pid) {
        createLikePhotoTable(uid);
        String replace = OfTenUtils.replace(uid);
        String sql_select = "SELECT * FROM likePhoto_" + replace + " WHERE pid = " + "'" + pid + "'";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql_select);
        if (list.size() > 0) {
            String sql_delete = "DELETE FROM likePhoto_" + replace + " WHERE pid = " + "'" + pid + "'";
            int update = jdbcTemplate.update(sql_delete);
            if (update > 0){
                System.out.println("删除成功 === likePhoto_" + replace);
            }
        }
    }


    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS " + " file_url" + " (" +
                "id INT (11) NOT NULL AUTO_INCREMENT PRIMARY KEY ," +
                "filename VARCHAR (255)," +
                "fileurl VARCHAR (255)" + ")" +
                "ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET ='utf8'";
        System.out.println("Sql:-------" + sql);
        jdbcTemplate.update(sql);
    }

    public void createLikePhotoTable(String tableName) {
        String tName = OfTenUtils.replace(tableName);
        String sql = "CREATE TABLE IF NOT EXISTS " + " likePhoto_" + tName + " (" +
                "id INT (11) NOT NULL AUTO_INCREMENT PRIMARY KEY ," +
                "url VARCHAR (255)," +
                "pid VARCHAR (255)," +
                "type INT (11)," +
                "time BIGINT(20)" + ")" +
                "ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET ='utf8mb4'";
        System.out.println("Sql:-------" + sql);
        jdbcTemplate.update(sql);
    }
}
