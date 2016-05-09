package com.baosaas.supervise.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baosaas.supervise.AppManager;
import com.baosaas.supervise.Model.Inventory;
import com.baosaas.supervise.Model.TaskImage;
import com.baosaas.supervise.R;
import com.baosaas.supervise.adapter.UploadInventorAdpater;
import com.baosaas.supervise.api.InventoryInterface;
import com.baosaas.supervise.base.BaseActivity;
import com.baosaas.supervise.bean.InventoryIn;
import com.baosaas.supervise.bean.UploadBills;
import com.baosaas.supervise.bean.UploadOut;
import com.baosaas.supervise.util.BitmapUtil;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 离线上传盘库页
 * Created by lym
 */
public class UploadInventorActivity extends BaseActivity {
    private static final int SHOW_DIALOG = 10;
    private static final int SHOW_TOAST = 1;
    private static final int SHOW_DOWN = 2;
    private static final int NOTOKEN = 3;
    private static final int SHOW_DATA = 4;
    private static final int NODATA = 5;
    private static final int SHOW_UPLOAD = 0;
    private final static int MSG_DATACHANGED = 6;
    public final static int ACTION_PHONE_CAMERA = 9;
    private File filename;
    private String imgstr = "";
    private byte[] bytes;

//    @ViewInject(R.id.button_photo)
//    TextView button_photo;
    @ViewInject(R.id.image_photo)
    ImageView image_photo;
    @ViewInject(R.id.tv_pushcount)
    TextView tvPcount;
    @ViewInject(R.id.listv_refresh)
    ListView mList;
    @ViewInject(R.id.tv_upload)
    TextView tv_upload;
    UploadInventorAdpater adpater;
    List<Inventory> unboundlist;
    @ViewInject(R.id.tv_title_warehouse)
    TextView tvWarehouse;
    @ViewInject(R.id.upload_unboundlist)
    Button lnVisUnboud;
    Dialog mDialog;
    @ViewInject(R.id.ln_search)
    LinearLayout lnS;
    String taskCode = "";
    SharedUtil config;
    private List<InventoryIn> inList;
    private TaskImage taskImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadunbound);
        AppManager.getInstance().addExitActivity(this);
        ViewUtils.inject(this);
        config = SharedUtil.getInstance();
        init();
        inList = new ArrayList<>();
        new ReadLocalDBThread().start();
        findImage();
    }

    void init() {
        tv_upload.setText("上传");
        lnVisUnboud.setVisibility(View.GONE);
//        lnS.setVisibility(View.GONE);
        taskCode = getIntent().getStringExtra("taskCode");
        tvWarehouse.setText(taskCode.toString());
        unboundlist = new ArrayList<>();
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UploadInventorActivity.this, InventoryDetailsActivity.class);
                intent.putExtra("billNo", unboundlist.get(position).getTaskCode());
                intent.putExtra("packNum", unboundlist.get(position).getPackNum());
                intent.putExtra("checkErrorDesc",unboundlist.get(position).getCheckErrorDesc());
                startActivityForResult(intent, InventoryDetailsActivity.DETAIL_REQCODE);
            }
        });
    }

    public void findImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    taskImage = DbSupport.getInstance().findFirst(Selector.from(TaskImage.class).where("taskCode", "=", taskCode));
                    if (taskImage != null) {
                        if (StringUtils.isNotEmpty(taskImage.getTaskImage())) {
                            bytes = Base64.decode(taskImage.getTaskImage(), Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            HandlerUtils.sendMsg(handler, SHOW_DOWN, bitmap);
                        }
                    } else {
                        TaskImage taskImage1 = new TaskImage();
                        taskImage1.setTaskCode(taskCode);
                        DbSupport.getInstance().save(taskImage1);
                        return;
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTION_PHONE_CAMERA:
                switch (resultCode) {
                    case RESULT_OK:
                        String sdStatus = Environment.getExternalStorageState();
                        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                            //判断sd卡是否挂载
                            return;
                        }
//                        button_photo.setVisibility(View.INVISIBLE);
                        image_photo.setVisibility(View.VISIBLE);
                        String filepath = filename.getAbsolutePath();
                        Log.e("filepath-------->", filepath);
                        Bitmap bitmap1 = BitmapUtil.getSmallBitmap(filename.getAbsolutePath());
                        Bitmap bitmap2 = BitmapUtil.compressImage(bitmap1);
                        Matrix matrix = BitmapUtil.getMatrix(filepath);
                        bitmap2 = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix, true);
                        bitmap1 = BitmapUtil.compressImage(bitmap2);
                        image_photo.setImageBitmap(bitmap1);
                        byte[] byt = BitmapUtil.outstream.toByteArray();
                        Log.e("拍照byt.lenght--------", byt.length + "");
                        bytes = byt;
                        imgstr = Base64.encodeToString(byt, Base64.DEFAULT);
                        try {
                            TaskImage taskImage = DbSupport.getInstance().findFirst(Selector.from(TaskImage.class).where("taskCode", "=", taskCode));
                            if (taskImage != null) {
                                taskImage.setTaskImage(imgstr);
                                DbSupport.getInstance().update(taskImage);
                            }
                            if (filename.exists()) {
                                filename.delete();
                            }
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                break;
            case Config.UN_UPLOAD:
                if (data != null) {
//                    String packNum = data.getStringExtra("packNum");
//                    String checkErrorDesc = data.getStringExtra("checkErrorDesc");
//                    new SaveErrorThread(taskCode, packNum, checkErrorDesc).start();
                    String billNo = data.getStringExtra("billNo");
                    if (!taskCode.equals(billNo)) return;
                    String packNum = data.getStringExtra("packNum");
//                    String c = data.getStringExtra("checkErrorType");
//                    int checkErrorType = Integer.parseInt(c);
                    String checkErrorDesc = data.getStringExtra("checkErrorDesc");
                    new SaveErrorThread(billNo, packNum, checkErrorDesc).start();
                }
                break;

        }

    }

    class SaveErrorThread extends Thread {
        private String billNo;
        private String packNum;
        private int checkErrorType;
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
                    inventory.setCheckErrorDesc(checkErrorDesc);
                    inventory.setStatus(Config.UN_UPLOAD);
                    DbSupport.getInstance().update(inventory);
                    for (Inventory check : unboundlist) {
                        if (check.getPackNum().equals(packNum)) {
                            check.setCheckErrorDesc(checkErrorDesc);
                            check.setStatus(Config.UN_UPLOAD);
                            break;
                        }
                    }
//                    packAdapter.notifyDataSetChanged();
                    HandlerUtils.sendMsg(handler, MSG_DATACHANGED, "");
                } else {
                    HandlerUtils.sendMsg(handler, MSG_DATACHANGED, "检查捆包" + packNum + "!");
                }
            } catch (DbException e) {
                e.printStackTrace();
            }

        }
    }


    void setData() {

        if (unboundlist.size() == 0) {
            tvPcount.setVisibility(View.VISIBLE);
            tvPcount.setText("您还没有盘库过的数据");
            return;
        } else {
            tvPcount.setVisibility(View.VISIBLE);
            tvPcount.setText("该工单号有" + unboundlist.size() + "条数据可上传");
        }
        adpater = new UploadInventorAdpater(this, unboundlist);
        mList.setAdapter(adpater);
    }


    @Override
    public void handleMsg(Message msg) {
        if (msg != null) {
            switch (msg.what) {
                case NOTOKEN:
                    SharedUtil.getInstance().reset();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case SHOW_TOAST:
                    showToast(msg.obj.toString());
                    break;
                case NODATA:
                    showToast(msg.obj.toString());
                    break;
                case SHOW_UPLOAD:
                    image_photo.setImageBitmap(null);
                    image_photo.setVisibility(View.INVISIBLE);
                    showToast("上传成功");
                    clearlist();
                    break;
                case SHOW_DIALOG:
                    clearShowDialog(msg.obj.toString());
//                    cd(msg.obj.toString());
                    break;
                case SHOW_DATA:
                    setData();
                    break;
                case MSG_DATACHANGED:
                    adpater.notifyDataSetChanged();
//                    if (msg.obj != null) {
                    if (StringUtils.isNotEmpty(msg.obj.toString())) {
                        showToast(msg.obj.toString());
                    }

                    break;
                case SHOW_DOWN:
//                    button_photo.setVisibility(View.INVISIBLE);
                    image_photo.setVisibility(View.VISIBLE);
                    image_photo.setImageBitmap((Bitmap) msg.obj);
                    break;
            }
        }
    }

    @OnClick(R.id.frameLayout)
    public void onclick(View view) {
        getPhotoCamera(); //拍照
    }

    public void getPhotoCamera() {
        //调用系统拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File filed = new File(Environment.getExternalStorageDirectory() + "/baosaasphoto");
        if (!filed.exists()) {
            filed.mkdir();
        }
        filename = new File(filed, getFileName());
//        uring = filename.getAbsolutePath();
        Uri uri = Uri.fromFile(filename);
//        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, ACTION_PHONE_CAMERA);
    }

    public String getFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("_yyyy_MMdd_HHmmss");
        String photoName = taskCode + dateFormat.format(date) + ".jpg";
        return photoName;
    }

    private void clearlist() {
        unboundlist.clear();
        tvPcount.setVisibility(View.VISIBLE);
        tvPcount.setText("您已经成功上传全部已解押数据");
        adpater.notifyDataSetChanged();
    }


    void clearShowDialog(String msg) {
//        showProgress(msg);
        new AlertDialog.Builder(this).setTitle("友情提示").setMessage(msg).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
        unboundlist.clear();
        if (unboundlist.size() == 0) {
            tvPcount.setVisibility(View.VISIBLE);
            tvPcount.setText("您已成功上传全部已解押数据");
        } else {
            tvPcount.setVisibility(View.VISIBLE);
            tvPcount.setText("共有" + unboundlist.size() + "条解押可上传");
        }
        adpater.notifyDataSetChanged();


    }

    @OnClick(R.id.ln_search)
    public void refresh(View view) {
        if (unboundlist == null || unboundlist.size() == 0) {
            HandlerUtils.sendMsg(handler, NODATA, "没有可上传的已解押数据");
            return;
        }
        if (!NetSupport.checkNetWork(UploadInventorActivity.this)) {
            showToast("当前没有网络，请在网络下上传");
            return;
        }
        new AlertDialog.Builder(this).setTitle("友情提示").setMessage("是否上传已解押数据?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //上传
                showProgress("正在上传数据...", false);
                new UploadFromUnboundThread().start();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    //调用上传
//    void uploadUnboundlist(List<Unbound> ulist){
//        UnboudInterface ui = new UnboudInterface();
//        Uploadunbound u = new Uploadunbound(config.userCode,config.accessToken,ulist);
//        UnboudOut uo =ui.uploadUnboudList(u);
//
//
//    }

    //上传本地数据到远程服务器
    class UploadFromUnboundThread extends Thread {
        @Override
        public void run() {
            refreshFromService();
        }
    }

    private void refreshFromService() {
        if (NetSupport.checkNetWork(UploadInventorActivity.this)) {
            try {
                TaskImage taskImage = DbSupport.getInstance().findFirst(Selector.from(TaskImage.class).where("taskCode", "=", taskCode));
                if (taskImage != null && StringUtils.isNotEmpty(taskImage.getTaskImage())) {
                    Inventory inventory = DbSupport.getInstance().findFirst(Selector.from(Inventory.class).where("taskCode", "=", taskCode).and("status", "=", Config.NEW_DATA));
                    if (inventory == null) {
                        InventoryIn inventoryIn = new InventoryIn();
                        inventoryIn.setTaskImage(taskImage.getTaskImage());
                        inventoryIn.setTaskCode(taskCode);
                        inventoryIn.setPackList(unboundlist);
                        inList.add(inventoryIn);
                    } else {
                        //还有未盘捆包
                        HandlerUtils.sendMsg(handler, SHOW_TOAST, "还有未盘捆包");
                        return;
                    }
                } else {
                    //请添加工单照片
                    HandlerUtils.sendMsg(handler, SHOW_TOAST, "请添加工单照片");
                    return;
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
            InventoryInterface ui = new InventoryInterface();
            UploadBills bills = new UploadBills(config.userCode, config.accessToken, inList);
            UploadOut uo = ui.updateInventoryTask(bills);
            hideProgress();
            if (uo != null) {
                if (uo.getFlag() == 0) {
                    HandlerUtils.sendMsg(handler, SHOW_TOAST, uo.getMsg());
                    return;
                } else if (uo.getFlag() == 1) {
//                    unboundlist.clear();
                    try {
//                        if (unboundlist == null || unboundlist.isEmpty()) {
//                            HandlerUtils.sendMsg(handler, NODATA, "没有已解押的数据。");
//                            return;
//                        }
                        DbSupport.getInstance().delete(taskImage);
                        DbSupport.getInstance().deleteAll(unboundlist);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }

                    HandlerUtils.sendMsg(handler, SHOW_UPLOAD, "上传成功");
                    return;
                } else if (uo.getFlag() == 2) {

                    try {
                        if (unboundlist == null || unboundlist.isEmpty()) {
                            HandlerUtils.sendMsg(handler, NODATA, "没有已解押的数据。");
                            return;
                        }
                        DbSupport.getInstance().deleteAll(unboundlist);
                    } catch (DbException e) {
                        HandlerUtils.sendMsg(handler, NODATA, "数据库异常。");
                    }
                    HandlerUtils.sendMsg(handler, SHOW_DIALOG, uo.getMsg());
                } else if (uo.getFlag() == 99) {
                    HandlerUtils.sendMsg(handler, NOTOKEN, "因长期未登录，请重新登录！");
                }
            } else {
                HandlerUtils.sendMsg(handler, NODATA, "程序异常，请稍后重试");
            }
        } else {
            HandlerUtils.sendMsg(handler, SHOW_TOAST, "当前没有网络，请在网络下更新");
        }

    }


    //从本地数据库获取已经解押的列表数据
    class ReadLocalDBThread extends Thread {
        @Override
        public void run() {
            readFromLocalDB();
        }
    }

    private void readFromLocalDB() {
        //查询所有已解押状态的
        try {
            unboundlist = DbSupport.getInstance().findAll(Selector.from(Inventory.class).where("STATUS", "=", Config.UN_UPLOAD).and("taskCode", "=", taskCode).orderBy("position"));
//            DbSupport.getInstance().deleteAll(unboundlist);
            if (unboundlist == null || unboundlist.isEmpty()) {
                HandlerUtils.sendMsg(handler, NODATA, "没有已解押的数据。");
                return;
            }
            HandlerUtils.sendMsg(handler, SHOW_DATA, "");
        } catch (DbException e) {
            HandlerUtils.sendMsg(handler, SHOW_DATA, "没有需要上传的数据");
        }
    }

    @OnClick(R.id.ll_goBack_unbound)
    public void goBack(View view) {
        onBackPressed();
        UploadInventorActivity.this.finish();
    }

    @OnClick(R.id.image_photo)
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
//        Intent intent = new Intent(DetailsActivity.this, ShowImage.class);
//        intent.putExtra("taskCode", mBillNo);
//        intent.putExtra("packNum", mPackNum);
//        intent.putExtra("modelId", 1);
//        intent.putExtra("isFlags", isFlags);
//        if (!isFlags) {
//            intent.putExtra("bitmap", bytes);
//        }
//        startActivityForResult(intent, ACTION_PHONE_CAMERA);

        new android.app.AlertDialog.Builder(this).setTitle("提示").setMessage("图片操作").setPositiveButton("查看大图", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(UploadInventorActivity.this, ShowImage.class);
                intent.putExtra("bitmap", bytes);
                startActivity(intent);
            }
        }).setNegativeButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    TaskImage taskImage = DbSupport.getInstance().findFirst(Selector.from(TaskImage.class).where("taskCode", "=", taskCode));
                    if (taskImage != null) {
                        taskImage.setTaskImage("");
                        DbSupport.getInstance().update(taskImage);
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
                imgstr = "";
                image_photo.setImageBitmap(null);
                image_photo.setVisibility(View.INVISIBLE);
//                button_photo.setVisibility(View.VISIBLE);
            }
        }).setNeutralButton("重拍", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getPhotoCamera();
            }
        }).create().show();
    }
}
