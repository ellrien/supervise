package com.baosaas.supervise.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Ocean on 8/20/15.
 */
public class ClientInfoUtil {

    /**
     * 获取应用程序名称
     *
     * @return
     */
    public static String getApplicationName() {
        int stringId = AppContext.getInstance().getApplicationInfo().labelRes;
        return AppContext.getInstance().getString(stringId);
    }

    public static String getPackageName() {
        return AppContext.getInstance().getPackageName();
    }

    public static String getApplicationVersion() {
        try {
            PackageInfo packageInfo = AppContext.getInstance().getPackageManager().getPackageInfo(ClientInfoUtil.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }


    /**
     * apk的完整路径
     * @return
     */
    public static String getApkPath() {
        return getApkDirPath() + getApkName();
    }

    /**
     * apk的目录路径
     * @return
     */
    public static String getApkDirPath() {
        return "//sdcard/Android/data/" + SharedUtil.getInstance().packageName + "/apk/";
    }

    private static String getApkName() {
        StringBuffer sb = new StringBuffer("ouyeel_");
        sb.append(DateUtil.getNewTimeStr()).append(".apk");
        return sb.toString();
    }
}
