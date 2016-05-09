package com.baosaas.supervise.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.baosaas.supervise.AppManager;

/**
 * 网络服务
 *
 * @Author Ellrien
 * @Date 2015年7月2日下午1:32:40
 * @Version 1.0
 */
public class NetSupport {
    /**
     * 有网络true 无网络false
     *
     * @param context
     * @return
     */
    public static boolean checkNetWork(Context context) {
        boolean flag;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获取代表联网状态的NetWorkInfo对象
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        // 获取当前的网络连接是否可用
        if (null == networkInfo) {
            // 当网络不可用时，跳转到网络设置页面　　
            flag = false;
        } else if (!networkInfo.isAvailable()) {
            flag = false;
        } else {
            flag = true;
        }
        if (!flag) {
            Log.e(context.toString(), "网络没有连通");
        }
        return flag;
    }

    public static void exitSysByNetErr(Activity activity, final boolean isExit) {
        while (activity.getParent() != null) {
            activity = activity.getParent();
        }
        final Activity context = activity;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("网络错误，请检查手机网络设置或重启手机");
        builder.setCancelable(false);
        builder.setPositiveButton("设置网络", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                context.finish();
            }
        });
        builder.setNeutralButton("退出", new DialogInterface.OnClickListener() {
            // 停止启动退出程序
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isExit) {
                    AppManager.getInstance().exitAll();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
