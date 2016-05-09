package com.baosaas.supervise.bean;

/**
 * Created by ZhangZhiCheng on 2015/12/9.
 */
public class UserBillIn {
    private String userId;
    private String token;

    public UserBillIn(String userId, String token) {
        this.userId = userId;
        this.token = token;
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

    public void setUserId(String userCode) {
        this.userId = userCode;
    }
}
