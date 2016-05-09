package com.baosaas.supervise.bean;


import com.baosaas.supervise.Model.Inventory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhangZhiCheng on 2015/12/9.
 */
public class InventoryBillOut {
    private int flag;//0 失败   1成功   2 token失效
    private String msg;
    List<Inventory> bills = new ArrayList<>();

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Inventory> getBills() {
        return bills;
    }

    public void setBills(List<Inventory> bills) {
        this.bills = bills;
    }
}
