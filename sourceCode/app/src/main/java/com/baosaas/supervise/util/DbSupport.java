package com.baosaas.supervise.util;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

/**
 * Created by Ellrien on 2015/11/18.
 */
public class DbSupport {

    private static DbSupport dbSupport;
    private static DbUtils dbManager;
    public static DbUtils getInstance() {
        if (dbSupport == null) {
            dbSupport = new DbSupport();
            dbManager = DbUtils.create(AppContext.appContext, Config.DB_NAME, Config.DB_VERSION, new DbUtils.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbUtils db, int oldVersion, int newVersion) {
                    if (oldVersion != newVersion) {
                        LogUtils.i("oldVersion=" + oldVersion + ";newVersion=" + newVersion);

                    }
                }
            });
            dbManager.configAllowTransaction(true);
        }
        return dbManager;
    }

    public static void init() throws DbException {
        getInstance();
        //注册表
//        dbManager.createTableIfNotExist(Inventory.class);
//        dbManager.createTableIfNotExist(ErrorPlan.class);
//        dbManager.createTableIfNotExist(User.class);
    }
}
