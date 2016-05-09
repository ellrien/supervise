package com.baosaas.supervise.bean;

import java.io.Serializable;

/**
 * Created by Ellrien on 2015/11/18.
 */
public class VersionOut implements Serializable {

    private String url;
    private String currentVersion;
    private int status;
    private String upgradeDetail;
    private int result;

    public VersionOut() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUpgradeDetail() {
        return upgradeDetail;
    }

    public void setUpgradeDetail(String upgradeDetail) {
        this.upgradeDetail = upgradeDetail;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
