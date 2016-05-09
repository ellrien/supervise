package com.baosaas.supervise.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ellrien on 2015/11/16.
 */
public class SharedUtil {
    private static SharedUtil CONFIG;

    /**
     * 属性集合
     */
    public String accessToken;//token
    public String userCode;//用户代码 （=手机号）
    public String userName;//用户名称
    public String version;//版本号
    private String Terminal;
    public String channelID;//设备号
    public String packageName;//包名

    public static SharedUtil getInstance() {
        if (CONFIG == null) {
            CONFIG = new SharedUtil();
        }
        SharedPreferences sp = AppContext.appContext.getSharedPreferences
                (Config.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        CONFIG.accessToken = sp.getString("accessToken", "");
        CONFIG.userCode = sp.getString("userCode", "");
        CONFIG.userName = sp.getString("userName", "");
        CONFIG.version = sp.getString("version", "");
        CONFIG.channelID = sp.getString("channelID", "");
        CONFIG.packageName = sp.getString("packageName", "");

        /**
         * 新属性补上
         */
        return CONFIG;
    }

    public void saveInstance() {
        SharedPreferences preferences = AppContext.appContext.getSharedPreferences(Config.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("accessToken", CONFIG.accessToken);
        editor.putString("userCode", CONFIG.userCode);
        editor.putString("userName", CONFIG.userName);
        editor.putString("version", CONFIG.version);
        editor.putString("channelID", CONFIG.channelID);
        editor.putString("packageName", CONFIG.packageName);
        /**
         * 新属性补上
         */
        editor.commit();
    }


    /**
     * 是否登陆
     */
    public boolean isLogin() {
        if (StringUtils.isEmpty(CONFIG.accessToken)) {
            return false;
        } else {
            return true;
        }
    }

    public void reset() {
        CONFIG.accessToken = "";
        CONFIG.userCode = "";
        CONFIG.userName = "离线状态";
        CONFIG.saveInstance();
    }

    public String getTerminal() {
        return Terminal;
    }

    public void setTerminal(String terminal) {
        Terminal = terminal;
    }
}
