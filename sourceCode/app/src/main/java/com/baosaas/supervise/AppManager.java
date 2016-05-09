package com.baosaas.supervise;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * activity集合
 * Created by Ellrien on 2015/12/8.
 */
public class AppManager {

    //退出登录集合
    private List<Activity> activityList = new ArrayList<>();
    //退出app集合
    private List<Activity> activityAppList = new ArrayList<>();
    private static AppManager instance;

    private String seTerminal;//手持终端区别

    private AppManager() {
    }

    //单例模式中获取唯一的MyApplication实例
    public static AppManager getInstance() {
        if (null == instance) {
            instance = new AppManager();
        }
        return instance;
    }

    //添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
        activityAppList.add(activity);
    }

    public void addExitActivity(Activity activity) {
        activityAppList.add(activity);
    }

    //退出登录
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        System.exit(0);
    }

    public void exitAll() {
        for (Activity activity : activityAppList) {
            activity.finish();
        }
        System.exit(0);
    }


//    //设置终端版本区别号
//    public void setSelecTerminal(){
//
//    }

    public String getSeTerminal() {
        return seTerminal;
    }

    public void setSeTerminal(String seTerminal) {
        this.seTerminal = seTerminal;
    }
}
