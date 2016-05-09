package com.baosaas.supervise.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/12/15.
 */
public class UploadBills {
    private String userId;
    private String token;

    private List<InventoryIn> taskList;


    public UploadBills(String userId, String token, List<InventoryIn> taskList) {
        this.userId = userId;
        this.token = token;
        this.taskList = taskList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public List<InventoryIn> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<InventoryIn> taskList) {
        this.taskList = taskList;
    }
}
