package com.baosaas.supervise.bean;

/**
 * Created by ZhangZhiCheng on 2015/12/10.
 */
public class InventoryUploadOut {
    private int flag;//0 失败   1成功   2 token失效
    private String msg;

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
}
