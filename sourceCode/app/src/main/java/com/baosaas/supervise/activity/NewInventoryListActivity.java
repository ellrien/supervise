package com.baosaas.supervise.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baosaas.supervise.AppManager;
import com.baosaas.supervise.Model.Inventory;
import com.baosaas.supervise.R;
import com.baosaas.supervise.adapter.InventoryAdapter;
import com.baosaas.supervise.api.InventoryInterface;
import com.baosaas.supervise.base.BaseActivity;
import com.baosaas.supervise.bean.InventoryBill;
import com.baosaas.supervise.bean.UserBillIn;
import com.baosaas.supervise.bean.InventoryBillOut;
import com.baosaas.supervise.util.Config;
import com.baosaas.supervise.util.DbSupport;
import com.baosaas.supervise.util.HandlerUtils;
import com.baosaas.supervise.util.NetSupport;
import com.baosaas.supervise.util.SharedUtil;
import com.baosaas.supervise.util.StringUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * 盘库单列表页
 * Created by llym on 2015/12/21.
 */
public class NewInventoryListActivity extends BaseActivity {

    //UI数据源
    private List<InventoryBill> uiData;
    //数据库DATA
    private List<InventoryBill> localData;

    InventoryAdapter adapter;
    private static final int MSG_SHOWTOAST = 1;
    private static final int MSG_SHOWDOWN = 2;
    private static final int MSG_NOTOKEN = 3;
    private static final int MSG_SHOWDATA = 4;
    private static final int IMPAWNS_TYPE = 6;

    @ViewInject(R.id.inventory_listview)
    private ListView mListview;

    @ViewInject(R.id.tv_boundcount)
    private TextView tv_boundcount;
    @ViewInject(R.id.img_back_inventory)
    private ImageView ivBack;
    //上一次的盘库类型
    String oldtype = InventoryBill.QXTYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventorylist);
        ViewUtils.inject(this);
        AppManager.getInstance().addExitActivity(this);
        //先从数据库获取盘库单列表
        new ReadFromDBThread().start();
    }

    @Override
    public void handleMsg(Message msg) {
        if (msg != null) {
            hideProgress();
            switch (msg.what) {
                case MSG_NOTOKEN:
                    SharedUtil.getInstance().reset();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case MSG_SHOWTOAST:
                    showToast(msg.obj.toString());
                    break;
                case MSG_SHOWDOWN:
                    showToast("下载成功");
                    new ReadFromDBThread().start();
                    break;
                case MSG_SHOWDATA:
                    //刷新数据
                    setAdapter(localData);
                    break;
                case IMPAWNS_TYPE:
                    //数据库中没数据
                    tv_boundcount.setText(msg.obj.toString());
                    break;
            }
        }
    }


    public void setAdapter(List<InventoryBill> uiData) {
//        this.uiData.clear();
        if (uiData.size() == 0) {
            tv_boundcount.setText("没有未盘任务");
        } else {
            tv_boundcount.setText("共有" + uiData.size() + "条盘库任务");
        }
        adapter = new InventoryAdapter(NewInventoryListActivity.this, uiData);
        mListview.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.ll_back_inventory)
    public void back(View view) {
        onBackPressed();
        NewInventoryListActivity.this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //从远程服务器获取盘库数据
    class ReadFromRemoteThread extends Thread {
        @Override
        public void run() {
            refreshFromService();
        }
    }

    private void refreshFromService() {
        if (NetSupport.checkNetWork(NewInventoryListActivity.this)) {
            InventoryInterface inventoryInterface = new InventoryInterface();
            UserBillIn billIn = new UserBillIn(SharedUtil.getInstance().userCode, SharedUtil.getInstance().accessToken);
            InventoryBillOut billOut = null;
            billOut = inventoryInterface.getInventoryTask(billIn);
            if (billOut != null) {
                if (billOut.getFlag() == 0) {
                    HandlerUtils.sendMsg(handler, MSG_SHOWTOAST, billOut.getMsg());
                    return;
                } else if (billOut.getFlag() == 1) {
                    //插数据库
                    insertDB(billOut);
                    return;
                } else if (billOut.getFlag() == 2) {
                    HandlerUtils.sendMsg(handler, MSG_NOTOKEN, "登录失效");
                }
            } else {
                HandlerUtils.sendMsg(handler, MSG_SHOWTOAST, "下载失败");
            }
        } else {
            HandlerUtils.sendMsg(handler, MSG_SHOWTOAST, "当前没有网络，请在网络下更新");
        }
    }

    private void insertDB(InventoryBillOut out) {

        List<Inventory> listBill = out.getInventoryTasks();

        if (listBill != null && listBill.size() > 0) {
            List<Inventory> needInsertData = new ArrayList<>();//需要插入的数据源
            try {
                //拿到所有的数据（此时的数据状态都不为0）
                List<Inventory> dbListBill = DbSupport.getInstance().findAll(Selector.from(Inventory.class));

                for (Inventory bill : listBill) {
                    //先置状态为0
                    bill.setStatus(Config.NEW_DATA);
                }
                if (dbListBill == null || dbListBill.size() == 0) {
                    //数据库里没有数据，那就直接插入
                    needInsertData = listBill;
                } else {
                    //数据库里有数据，轮询，不在列表里的数据将插入，否则就抛弃
                    for (Inventory bill : listBill) {
                        boolean flag = false;
                        for (int i = 0; i < dbListBill.size(); i++) {
                            Inventory bill1 = dbListBill.get(i);
                            if (StringUtils.isNotEmpty(bill1.getTaskCode()) && bill1.getTaskCode().equals(bill.getTaskCode()) && bill1.getPackNum().equals(bill.getPackNum())) {
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) {
                            //匹配不上的数据将插入
                            needInsertData.add(bill);
                        }
                    }
                }
                DbSupport.getInstance().saveOrUpdateAll(needInsertData);
            } catch (DbException e) {
                e.printStackTrace();
            }
            if (needInsertData == null || needInsertData.size() == 0) {
                HandlerUtils.sendMsg(handler, MSG_SHOWTOAST, "没有新的任务");
                return;
            }

        }
        HandlerUtils.sendMsg(handler, MSG_SHOWDOWN, "ok");
    }

    @OnClick(R.id.refreshlayout)
    public void refresh(View view) {
        if (!NetSupport.checkNetWork(NewInventoryListActivity.this)) {
            showToast("当前没有网络，请在网络下更新");
            return;
        }
        new AlertDialog.Builder(this).setTitle("友情提示").setMessage("是否下载盘库数据?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showProgress("正在下载盘库数据", false);
                new ReadFromRemoteThread().start();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    //从本地数据库获取盘库数据
    class ReadFromDBThread extends Thread {
        @Override
        public void run() {
            readFromLocalDB();
        }
    }

    private void readFromLocalDB() {
        String sql = "select taskCode,count(*) as nums,max(createDate) as createDate,max(sysId) as sysId,max(type) as type,max(warehouse) as warehouse,financialwarehouse from inventory where status = 0 group by taskCode,financialwarehouse";
        Cursor cursor;
        Cursor cursor1;
        String type;
        localData = new ArrayList<>();
        try {
            cursor = DbSupport.getInstance().execQuery(sql);
            while (cursor.moveToNext()) {
                String weightsql = "select weight from inventory where taskCode='" + cursor.getString(0) + "' and status = '" + Config.NEW_DATA + "'";
                cursor1 = DbSupport.getInstance().execQuery(weightsql);
                List<String> weightlist = new ArrayList<>();
                BigDecimal sd = new BigDecimal(0);
                double num = 0;
                while (cursor1.moveToNext()) {
                    String weight1 = cursor1.getString(0);
                    weightlist.add(weight1);
                }
                for (int i = 0; i < weightlist.size(); i++) {
                    double dw = Double.valueOf(weightlist.get(i));
                    sd = sd.add(new BigDecimal(weightlist.get(i)));
                    num += dw;
                }
                Double dou = sd.doubleValue();
                type = cursor.getString(4);
                InventoryBill bill = new InventoryBill();
//                bill.setWeight(num);
                bill.setWeight(dou);
                bill.setTaskCode(cursor.getString(0));
                bill.setNums(cursor.getInt(1));
                bill.setCreateDate(cursor.getString(2));
                bill.setSysId(cursor.getString(3));
                bill.setType(type);
                bill.setWarehouse(cursor.getString(5));
                bill.setFinancialwarehouse(cursor.getString(6));
                localData.add(bill);
            }
            if (localData != null && localData.size() != 0) {
                HandlerUtils.sendMsg(handler, MSG_SHOWDATA, "");
            } else {
                HandlerUtils.sendMsg(handler, MSG_SHOWDATA, "没有未盘任务");
            }

        } catch (DbException e) {
            e.printStackTrace();
            HandlerUtils.sendMsg(handler, IMPAWNS_TYPE, "盘库列表为空，点击右上角下载盘库列表");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new ReadFromDBThread().start();
    }



}
