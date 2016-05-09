package com.baosaas.supervise.bean;


import com.baosaas.supervise.Model.Inventory;

import java.util.List;

/**
 * Created by Administrator on 2015/12/21.
 */
public class InventoryIn {
    private String taskCode;
    private String taskImage;
    private List<Inventory> packList;

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getTaskImage() {
        return taskImage;
    }

    public void setTaskImage(String taskImage) {
        this.taskImage = taskImage;
    }

    public List<Inventory> getPackList() {
        return packList;
    }

    public void setPackList(List<Inventory> packList) {
        this.packList = packList;
    }

}
