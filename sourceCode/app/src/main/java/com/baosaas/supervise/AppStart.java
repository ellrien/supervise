package com.baosaas.supervise;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

import com.baosaas.supervise.api.SystemInterface;
import com.baosaas.supervise.base.BaseFragment;
import com.baosaas.supervise.bean.VersionIn;
import com.baosaas.supervise.bean.VersionOut;
import com.baosaas.supervise.fragment.AutomaticUpdateDialogFragment;

import com.baosaas.supervise.fragment.VersionTipsDialogFragment;
import com.baosaas.supervise.util.Config;
import com.baosaas.supervise.util.DbSupport;
import com.baosaas.supervise.util.HandlerUtils;
import com.baosaas.supervise.util.NetSupport;
import com.baosaas.supervise.util.RedirectUtil;
import com.baosaas.supervise.util.SharedUtil;
import com.baosaas.supervise.util.StringUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 启动页
 */
public class AppStart extends BaseFragment {

    @ViewInject(R.id.tv_version)
    TextView tvVersion;

    @ViewInject(R.id.tv_hint)
    TextView tvHint;

    SharedUtil sp;
    String packageName;
    private String strSize;

    private final int SHOW_TOAST = 1;
    private final int SHOW_HINT = 2;
    private final int NEED_UPDATE = 3;
    private final int OK = 5;
    private static long FILE_SIZE = 0;

    @Override
    public void handleMsg(Message msg) {
        if (msg != null) {
            if (msg.what == SHOW_HINT) {
                tvHint.setText(msg.obj.toString());
            } else if (msg.what == SHOW_TOAST) {
                showToast(msg.obj.toString());
            } else if (msg.what == NEED_UPDATE) {
                VersionOut out = (VersionOut) msg.obj;
                versionUpdate(out);
            } else if (msg.what == OK) {
                RedirectUtil.go2Login(AppStart.this);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_start);
        sp = SharedUtil.getInstance();
        AppManager.getInstance().addExitActivity(this);
        ViewUtils.inject(this);
        AnalyticsConfig.setAppkey(this, Config.UMENG_API_KEY);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setDebugMode(true);
        MobclickAgent.updateOnlineConfig(this);
        init();
    }

    public void urlFileSize(VersionOut versionOut) {
        long fileSize = getFileSize(versionOut);
        double mbSize = fileSize / (double) (1024 * 1024);
        String str = mbSize + "";
        strSize = str.contains(".") ? str.substring(0, str.indexOf(".") + 2) : str;
    }

    public static long getFileSize(VersionOut out) {
        try {
            URL url = new URL(out.getUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            FILE_SIZE = conn.getContentLength();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FILE_SIZE;
    }


    private void versionUpdate(final VersionOut out) {
        VersionTipsDialogFragment versionTipsDialogFragment = (VersionTipsDialogFragment) VersionTipsDialogFragment.newInstance(out, strSize);
        versionTipsDialogFragment.setCancelable(false);
        versionTipsDialogFragment.versionTipsDialogListener = new VersionTipsDialogFragment.VersionTipsDialogListener() {
            @Override
            public void openDialogFragment() {
                AutomaticUpdateDialogFragment automaticUpdateDialogFragment = (AutomaticUpdateDialogFragment) AutomaticUpdateDialogFragment.newInstance(out);
                automaticUpdateDialogFragment.setCancelable(false);
                automaticUpdateDialogFragment.show(getSupportFragmentManager(), "automaticUpdateDialogFragment");
            }

            @Override
            public void goLogin() {
                initDb();
//                initDbLibrary();
            }
        };
        versionTipsDialogFragment.show(getSupportFragmentManager(), "versionTipsDialogFragment");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(OK);
    }

    private void init() {
        setVersion();
        //1.检查网络
        tvHint.setText("正在检查网络...");
        new Thread(new CheckEnvironment()).start();
        //2.检查版本
        //3.检查数据库
    }

    class CheckEnvironment implements Runnable {

        @Override
        public void run() {
            sleep();
            if (!NetSupport.checkNetWork(AppStart.this)) {
                // 网络连接失败的处理
              //  NetSupport.exitSysByNetErr(AppStart.this, true);
                return;
            }
            HandlerUtils.sendMsg(handler, SHOW_HINT, "正在检查版本...");
            callInterface4Appversion(packageName);
        }
    }

    private void sleep() {
        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void callInterface4Appversion(String packageName) {
        sleep();
        SystemInterface systemI = new SystemInterface();
        VersionIn in = new VersionIn(packageName, Config.APP_TYPE);
        VersionOut out = systemI.getAppVersion(in);
        if (out == null) {
            HandlerUtils.sendMsg(handler, SHOW_TOAST, "连接服务器失败");
        } else {
            if (out.getCurrentVersion()!=null){
                if (sp.version.compareTo(out.getCurrentVersion()) < 0) {
                    urlFileSize(out);
                    HandlerUtils.sendMsg(handler, NEED_UPDATE, out);
                    return;
                }
            }else{
                HandlerUtils.sendMsg(handler, SHOW_TOAST, "连接服务器失败");
            }
        }
        initDb();
//        initDbLibrary();
    }

    private void initDb() {
        HandlerUtils.sendMsg(handler, SHOW_HINT, "正在检查数据...");
        sleep();

        //建表
        try {
            DbSupport.init();
        } catch (DbException e) {
            e.printStackTrace();
            HandlerUtils.sendMsg(handler, SHOW_TOAST, "数据库初始化失败");
            return;
        }
        HandlerUtils.sendMsg(handler, OK, "redirect");
    }

    private void initDbLibrary() {
        HandlerUtils.sendMsg(handler, SHOW_HINT, "正在检查数据...");
        sleep();
        //建表
        try {
            DbSupport.init();
        } catch (DbException e) {
            e.printStackTrace();
            HandlerUtils.sendMsg(handler, SHOW_TOAST, "数据库初始化失败");
            return;
        }
        HandlerUtils.sendMsg(handler, OK, "redirect");
    }

    void setTestData() {

    }

    //版本号显示
    private void setVersion() {
        try {
            PackageInfo pkg = getPackageManager().getPackageInfo(getApplication().getPackageName(), 0);
            packageName = pkg.packageName;
            String versionName = pkg.versionName;
            sp.version = versionName;
            sp.packageName = packageName;
            sp.saveInstance();
            if (StringUtils.isNotEmpty(versionName)) {
                tvVersion.setText("版本号：v" + versionName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
