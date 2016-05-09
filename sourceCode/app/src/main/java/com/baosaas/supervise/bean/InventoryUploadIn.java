package com.baosaas.supervise.bean;


import com.baosaas.supervise.Model.Inventory;

import java.util.List;

/**
 * Created by ZhangZhiCheng on 2015/12/10.
 */
public class InventoryUploadIn {
    private String userId;
    private String token;
    private List<Inventory> taskList;

    public InventoryUploadIn(String userId, String token,List<Inventory> taskList) {
        this.userId = userId;
        this.token = token;
        this.taskList = taskList;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Inventory> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Inventory> taskList) {
        this.taskList = taskList;
    }
}
