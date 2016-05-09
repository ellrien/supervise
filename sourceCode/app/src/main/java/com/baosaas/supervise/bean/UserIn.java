package com.baosaas.supervise.bean;

/**
 * Created by Ellrien on 2015/11/16.
 */
public class UserIn {
    private String userId;
    private String password;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserIn(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
