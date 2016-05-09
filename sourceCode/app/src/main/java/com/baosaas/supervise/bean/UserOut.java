package com.baosaas.supervise.bean;

/**
 * Created by Ellrien on 2015/11/18.
 */
public class UserOut {
    private int flag;
    private String msg;
    private String token;
    private String userName;

    public UserOut() {
    }

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
