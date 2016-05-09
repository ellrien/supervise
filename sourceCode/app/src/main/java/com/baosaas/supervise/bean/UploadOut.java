package com.baosaas.supervise.bean;

/**
 * Created by Administrator on 2015/12/15.
 */
public class UploadOut {
    private String msg; //flag = 0时的错误信息
    private int flag;   //0：失败 1：成功 99： token校验失败

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
