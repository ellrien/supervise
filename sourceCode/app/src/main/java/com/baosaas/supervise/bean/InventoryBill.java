package com.baosaas.supervise.bean;

import java.io.Serializable;

/**
 * Created by ZhangZhiCheng on 2015/12/10.
 */
public class InventoryBill implements Serializable {
    private String taskCode; //盘库单号
    private String createDate; //创建日期
    private String sysId; //系统资源号
    private String type; //盘库类型
    private String warehouse; //仓库名称
    private String tag;//标签号
    private int nums; //件数
    private double weight;
    private String financialwarehouse;

    public String getFinancialwarehouse() {
        return financialwarehouse;
    }

    public void setFinancialwarehouse(String financialwarehouse) {
        this.financialwarehouse = financialwarehouse;
    }

    //全选
    public static final String QXTYPE = "0";
    //全盘
    public static final String QPTYPE = "10";
    //抽盘
    public static final String CPTYPE = "20";
    //质押
    public static final String ZYTYPE = "5";

    public InventoryBill() {
    }

    public String getTypeDesc()
    {
        switch (type)
        {
            case QPTYPE:
                return "全盘";
            case CPTYPE:
                return "抽盘";
            case ZYTYPE:
                return "质押";
        }
        return "";
    }


    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }
}
