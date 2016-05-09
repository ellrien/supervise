package com.baosaas.supervise.bean;

/**
 * Created by Ellrien on 2015/11/18.
 */
public class VersionIn {

    private String packageName;
    private int appType;

    public VersionIn(String packageName, int appType) {
        this.packageName = packageName;
        this.appType = appType;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }
}
