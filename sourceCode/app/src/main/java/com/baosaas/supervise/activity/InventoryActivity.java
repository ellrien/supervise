package com.baosaas.supervise.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baosaas.supervise.AppManager;
import com.baosaas.supervise.Model.Inventory;
import com.baosaas.supervise.R;
import com.baosaas.supervise.adapter.InventoryPackAdapter;
import com.baosaas.supervise.base.BaseActivity;
import com.baosaas.supervise.bean.InventoryPack;
import com.baosaas.supervise.util.Config;
import com.baosaas.supervise.util.DbSupport;
import com.baosaas.supervise.util.HandlerUtils;
import com.baosaas.supervise.util.SharedUtil;
import com.baosaas.supervise.util.StringUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * 盘库页
 * Created by Ellrien on 2015/12/8.
 */
public class InventoryActivity extends BaseActivity {

    @ViewInject(R.id.tv_billno)
    private TextView tvBillno;
    @ViewInject(R.id.ll_goBack)
    private LinearLayout llBack;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.text_upload)
    private TextView text_upload;
    @ViewInject(R.id.lvpack)
    private ListView lvPack;
    private Button startRfid;
    @ViewInject(R.id.tv_boundcount)
    private TextView tv_boundcount;
    //盘库单号
    String mBillNo;

    //未匹配捆包
    private List<InventoryPack> unCheckList;

    private InventoryPackAdapter packAdapter;

    private final static int MSG_DATACHANGED = 1;
    private final static int MSG_SHOWDIALOG = 2;
    private final static int MSG_NODATA = 5;
    private final static int MSG_UPLOADFAIL = 6;
    private final static int MSG_UPLOADSUCCESS = 3;
    private final static int MSG_NOTOKEN = 4;
    private final static int MSG_DBREAD = 10;

    private Intent mIntent;
    //rfid模块是否开启
    boolean rfidStarted = false;

    HandlerThread localHandlerThread;
    Handler handlers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        ViewUtils.inject(this);

        mBillNo = getIntent().getStringExtra("billNo");
        tvBillno.setText(mBillNo);
        init();
        unCheckList = new ArrayList<>();
        new SelectDataThread().start();
        lvPack.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InventoryPack inv = unCheckList.get(i);
                if (inv != null) {
                    mIntent = new Intent();
                    mIntent.setClass(InventoryActivity.this, InventoryDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("billNo", mBillNo);
                    bundle.putString("packNum", inv.getPackNum());
//                    bundle.putInt("checkErrorType", inv.getCheckErrorType());
                    mIntent.putExtras(bundle);
                    //mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(mIntent, InventoryDetailsActivity.DETAIL_REQCODE);
                }
            }
        });
        text_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Inventory inventory = DbSupport.getInstance().findFirst(Selector.from(Inventory.class).where("status", "=", Config.UN_UPLOAD).and("taskCode", "=", mBillNo));
                    if (inventory != null) {
                        Intent intent = new Intent(InventoryActivity.this, To_InventorActivity.class);
                        intent.putExtra("taskCode", mBillNo);
                        startActivity(intent);
                    } else {
                        showToast("没有已盘数据");
                        return;
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    void init() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;//320
        int screenHeight = dm.heightPixels;//480
        if (screenHeight < 855 || screenWidth < 402) {
            tvBillno.setTextSize(9);
        }

        String a = "";
        if (AppManager.getInstance().getSeTerminal() == null) {
            startRfid.setClickable(false);
            startRfid.setText("手机无法扫描");

        } else {
            a = AppManager.getInstance().getSeTerminal().toString();
        }

        if ("201".equals(a)) {
            //手机
            startRfid.setVisibility(View.GONE);
        } else {
            //PDA
            startRfid.setVisibility(View.VISIBLE);

        }

    }




    @OnClick(R.id.ll_goBack)
    public void back(View view) {
        onBackPressed();
        InventoryActivity.this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();

    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null || InventoryDetailsActivity.DETAIL_REQCODE != requestCode) return;
        String billNo = data.getStringExtra("billNo");
        if (!mBillNo.equals(billNo)) return;
        String packNum = data.getStringExtra("packNum");
//        int checkErrorType = data.getIntExtra("checkErrorType", Config.check_Error_Type);
//        String c = data.getStringExtra("checkErrorType");
//        int checkErrorType = data.getIntExtra("checkErrorType", Config.check_Error_Type);
//        int checkErrorType = Integer.parseInt(c);
        String checkErrorDesc = data.getStringExtra("checkErrorDesc");
        new SaveErrorThread(billNo, packNum, checkErrorDesc).start();
    }

    @Override
    public void handleMsg(Message msg) {
        hideProgress();
        if (msg != null) {
            switch (msg.what) {
                case MSG_DATACHANGED:
                    packAdapter.notifyDataSetChanged();
//                    if (msg.obj != null) {
                    if (StringUtils.isNotEmpty(msg.obj.toString())) {
                        showToast(msg.obj.toString());
                    }

                    break;
                case MSG_SHOWDIALOG:
                    new AlertDialog.Builder(InventoryActivity.this).setTitle("提示!").setMessage(msg.obj.toString()).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                    break;
                case MSG_NOTOKEN:
                    SharedUtil.getInstance().reset();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    break;
                case MSG_UPLOADFAIL:
                    showToast(msg.obj.toString());
                    break;
                case MSG_UPLOADSUCCESS:
//                    if (checked) {
//                        packAdapter = new InventoryPackAdapter(this, checkList);
//                    } else {
                    packAdapter = new InventoryPackAdapter(this, unCheckList);
//                    }
                    lvPack.setAdapter(packAdapter);
                    showToast("上传成功");
                    break;
                case MSG_NODATA:
                    showToast(msg.obj.toString());
                    break;
                case MSG_DBREAD:
                    tv_boundcount.setText("共有"+unCheckList.size()+"条未盘库捆包");
                    packAdapter = new InventoryPackAdapter(this, unCheckList);
                    lvPack.setAdapter(packAdapter);
                    break;
            }
        }
    }

    class SaveErrorThread extends Thread {
        private String billNo;
        private String packNum;
        private String checkErrorDesc;

        public SaveErrorThread(String billNo, String packNum, String checkErrorDesc) {
            this.billNo = billNo;
            this.packNum = packNum;
            this.checkErrorDesc = checkErrorDesc;
        }

        @Override
        public void run() {
            try {
                Inventory inventory = DbSupport.getInstance().findFirst(Selector.from(Inventory.class).where("taskCode", "=", billNo).and("packNum", "=", packNum));
                if (inventory != null) {

                    for (InventoryPack unCheck : unCheckList) {
                        if (unCheck.getPackNum().equals(packNum)) {
                            unCheck.setCheckErrorDesc(checkErrorDesc);
                            unCheck.setStatus(Config.UN_UPLOAD);
                            unCheckList.remove(unCheck);
//                            checkList.add(unCheck);
                            break;
                        }
                    }
                    inventory.setCheckErrorDesc(checkErrorDesc);
                    inventory.setStatus(Config.UN_UPLOAD);
                    DbSupport.getInstance().update(inventory);
                    HandlerUtils.sendMsg(handler, MSG_DATACHANGED, "");
                } else {
                    HandlerUtils.sendMsg(handler, MSG_DATACHANGED, "检查捆包" + packNum + "!");
                }
            } catch (DbException e) {
                e.printStackTrace();
            }

        }
    }

    class SelectDataThread extends Thread {
        @Override
        public void run() {
            //从数据库取列表信息
            String sql_unLoad = "select packNum,weight,barCode,position,standard,status,tag,checkErrorDesc from inventory  where taskCode = '" + mBillNo + "' and status = '0' order by position";
            try {
                Cursor cursor = DbSupport.getInstance().execQuery(sql_unLoad);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        InventoryPack pack = new InventoryPack();
                        pack.setPackNum(cursor.getString(0));
                        pack.setWeight(cursor.getString(1));
                        pack.setBarCode(cursor.getString(2));
                        pack.setPosition(cursor.getString(3));
                        pack.setStandard(cursor.getString(4));
                        pack.setStatus(cursor.getInt(5));
                        pack.setTag(cursor.getString(6));
                        pack.setCheckErrorDesc(cursor.getString(7));
                        unCheckList.add(pack);
                    }
                } else {
                    return;
                }

            } catch (DbException e) {
                HandlerUtils.sendMsg(handler, MSG_SHOWDIALOG, "未盘数据加载出错!");
            }
            HandlerUtils.sendMsg(handler, MSG_DBREAD, "盘库数据加载完成!");
        }
    }

    class CheckDataThread extends Thread {
        private String tagId;

        public CheckDataThread(String tagId) {
            this.tagId = tagId;
        }

        @Override
        public void run() {
            checkData(tagId);
        }
    }

    private void checkData(String tagId) {
        InventoryPack pack = null;
        for (InventoryPack unCheck : unCheckList) {
            if (tagId.equals(unCheck.getTag())) {
                pack = unCheck;
                try {
                    Inventory inventory = DbSupport.getInstance().findFirst(Selector.from(Inventory.class).where("taskCode", "=", mBillNo).and("packNum", "=", pack.getPackNum()));
                    if (inventory != null) {
                        inventory.setStatus(Config.UN_UPLOAD);
                        DbSupport.getInstance().update(inventory);
                        unCheckList.remove(pack);
                        HandlerUtils.sendMsg(handler, MSG_DATACHANGED, "发现捆包" + inventory.getPackNum() + "!");
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

}
