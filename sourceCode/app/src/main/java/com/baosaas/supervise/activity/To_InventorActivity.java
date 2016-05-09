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
import com.baosaas.supervise.base.BaseActivity;
import com.baosaas.supervise.bean.InventoryIn;
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
public class To_InventorActivity extends BaseActivity {
    private static final int SHOW_DIALOG = 10;
    private static final int SHOW_TOAST = 1;
    private static final int ACTION_PHONE_CAMERA = 2;
    private static final int NOTOKEN = 3;
    private static final int SHOW_DATA = 4;
    private static final int NODATA = 5;
    private static final int SHOW_UPLOAD = 0;
    private final static int MSG_DATACHANGED = 6;
    private final static int SHOW_IMAGE = 7;
    private File filename;
    private String uring, imgstr = "";
    private String photoName;
    String taskCode = "";
    SharedUtil config;
    private byte[] bytes;
    private TaskImage taskImage;
    @ViewInject(R.id.tv_pushcount)
    TextView tvPcount;

    @ViewInject(R.id.listv_refresh)
    ListView mList;
    @ViewInject(R.id.image_photo)
    ImageView image_photo;
    UploadInventorAdpater adpater;
    List<Inventory> unboundlist;
    @ViewInject(R.id.tv_title_warehouse)
    TextView tvWarehouse;
    @ViewInject(R.id.tv_upload)
    TextView mTvupload;
    @ViewInject(R.id.upload_unboundlist)
    Button lnVisUnboud;
    Dialog mDialog;
    @ViewInject(R.id.ln_search)
    LinearLayout lnS;
    private List<InventoryIn> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadinventory);
        AppManager.getInstance().addExitActivity(this);
        ViewUtils.inject(this);
        config = SharedUtil.getInstance();
        init();
        new ReadLocalDBThread().start();
    }

    void init() {
        lnVisUnboud.setVisibility(View.GONE);
        mTvupload.setText("上传");
        lnS.setVisibility(View.VISIBLE);
        taskCode = getIntent().getStringExtra("taskCode");
        tvWarehouse.setText(taskCode.toString());
        unboundlist = new ArrayList<>();
        taskList = new ArrayList<>();
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(To_InventorActivity.this, InventoryDetailsActivity.class);
                intent.putExtra("billNo", unboundlist.get(position).getTaskCode());
                intent.putExtra("packNum", unboundlist.get(position).getPackNum());
                intent.putExtra("checkErrorDesc", unboundlist.get(position).getCheckErrorDesc());
                startActivityForResult(intent, InventoryDetailsActivity.DETAIL_REQCODE);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    taskImage = DbSupport.getInstance().findFirst(Selector.from(TaskImage.class).where("taskCode", "=", taskCode));
                    if (taskImage != null) {
                        if (StringUtils.isNotEmpty(taskImage.getTaskImage())) {
                            bytes = Base64.decode(taskImage.getTaskImage(), Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            HandlerUtils.sendMsg(handler, SHOW_IMAGE, bitmap);
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

    @OnClick(R.id.image_photo)
    public void imageOnclick(View view) {
        new android.app.AlertDialog.Builder(this).setTitle("提示").setMessage("图片操作").setPositiveButton("查看大图", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(To_InventorActivity.this, ShowImage.class);
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
            }
        }).setNeutralButton("重拍", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getPhotoCamera();
            }
        }).create().show();
    }

    @OnClick(R.id.frameLayout)
    public void buttonOnclick(View view) {
        getPhotoCamera();
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
        photoName = taskCode + dateFormat.format(date) + ".jpg";
        return photoName;
    }


    public static String splitString(String string) {
        String[] splitstr = string.split("\\.");
        for (int i = 0; i < splitstr.length; i++) {
            if (i == splitstr.length - 1) {
                return "." + splitstr[i];
            }
        }
        return "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                //【拍照
                if (requestCode == ACTION_PHONE_CAMERA) {
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                        //判断sd卡是否挂载
                        return;
                    }
                    image_photo.setVisibility(View.VISIBLE);
                    String filepath = filename.getAbsolutePath();
                    Log.e("filepath-------->", filepath);
                    Bitmap bitmap1 = BitmapUtil.getSmallBitmap(filename.getAbsolutePath());
                    Bitmap bitmap2 = BitmapUtil.compressImage(bitmap1);
//                    image_photo.setImageBitmap(BitmapUtil.rotateBitmap(bitmap2));
                    Matrix matrix = BitmapUtil.getMatrix(filepath);
                    bitmap2 = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix, true);
                    bitmap1 = BitmapUtil.compressImage(bitmap2);
                    image_photo.setImageBitmap(bitmap1);
                    byte[] byt = BitmapUtil.outstream.toByteArray();
                    Log.e("拍照bytes.lenght--------", byt.length + "");
                    imgstr = Base64.encodeToString(byt, Base64.DEFAULT);
                    bytes = byt;
                    if (filename.exists()) {
                        filename.delete();
                    }
                    try {
                        //保存到数据库
                        TaskImage taskImage = DbSupport.getInstance().findFirst(Selector.from(TaskImage.class).where("taskCode", "=", taskCode));
                        taskImage.setTaskImage(imgstr);
                        DbSupport.getInstance().update(taskImage);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case InventoryDetailsActivity.DETAIL_REQCODE:
                if (data == null) return;
                String billNo = data.getStringExtra("billNo");
                if (!taskCode.equals(billNo)) return;
                String packNum = data.getStringExtra("packNum");
//        int checkErrorType = data.getIntExtra("checkErrorType", Config.check_Error_Type);
//                String c = data.getStringExtra("checkErrorType");
//        int checkErrorType = data.getIntExtra("checkErrorType", Config.check_Error_Type);
//                int checkErrorType = Integer.parseInt(c);
                String checkErrorDesc = data.getStringExtra("checkErrorDesc");
                new SaveErrorThread(billNo, packNum, checkErrorDesc).start();
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
//            this.checkErrorType = checkErrorType;
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
                    hideProgress();
                    showToast(msg.obj.toString());
                    break;
                case NODATA:
                    hideProgress();
                    showToast(msg.obj.toString());
                    break;
                case SHOW_UPLOAD:
                    image_photo.setImageBitmap(null);
                    image_photo.setVisibility(View.INVISIBLE);
                    hideProgress();
                    showToast("上传成功");
                    adpater.notifyDataSetChanged();
                    break;
                case SHOW_DIALOG:
                    hideProgress();
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
                case SHOW_IMAGE:
                    image_photo.setVisibility(View.VISIBLE);
                    image_photo.setImageBitmap((Bitmap) msg.obj);
                    break;
            }
        }
    }

    private void clearlist() {
        unboundlist.clear();
        if (unboundlist.size() == 0) {
            tvPcount.setVisibility(View.VISIBLE);
            tvPcount.setText("您已经成功上传全部已盘数据");
        } else {
            tvPcount.setVisibility(View.VISIBLE);
            tvPcount.setText("共有" + unboundlist.size() + "条捆包可上传");
        }
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
        if (!NetSupport.checkNetWork(To_InventorActivity.this)) {
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
            Uploading();
        }
    }

    public void Uploading() {   //开启线程调用上传的方法

        if (unboundlist != null && unboundlist.size() > 0) {
            try {
                TaskImage taskImage = DbSupport.getInstance().findFirst(Selector.from(TaskImage.class).where("taskCode", "=", taskCode));
                if (taskImage != null && StringUtils.isNotEmpty(taskImage.getTaskImage())) {
                    ////有没有工单照片
                    Inventory inventory = DbSupport.getInstance().findFirst(Selector.from(Inventory.class).where("taskCode", "=", taskCode).and("status", "=", Config.NEW_DATA));
                    if (inventory == null) {
                        //表示全部盘完
                        //查询该工单下全部捆包信息
//                            unboundlist = DbSupport.getInstance().findAll(Selector.from(Inventory.class).where("taskCode", "=", taskCode).and("status", "=", Config.UN_UPLOAD));

                        InventoryIn in = new InventoryIn();
                        in.setTaskImage(taskImage.getTaskImage());
                        in.setTaskCode(taskImage.getTaskCode());
                        in.setPackList(unboundlist);
                        taskList.add(in);
//                        UploadInterface ui = new UploadInterface();
//                        UploadBills bills = new UploadBills(config.userCode, config.accessToken, taskList);
//                        UploadOut out = ui.getuploadOut(bills);
//                        if (out != null) {
//                            if (out.getFlag() == 0) { //失败
//                                HandlerUtils.sendMsg(handler, SHOW_TOAST, out.getMsg());
//                            } else if (out.getFlag() == 1) { //成功
//                                DbSupport.getInstance().deleteAll(taskList.get(0).getPackList());
//                                unboundlist.clear();
//                                DbSupport.getInstance().delete(taskImage);
//                                HandlerUtils.sendMsg(handler, SHOW_UPLOAD, out.getMsg());
//                            } else if (out.getFlag() == 2) {//部分判断出错
//
//                            } else if (out.getFlag() == 99) { //校验失败
//                                HandlerUtils.sendMsg(handler, NOTOKEN, "因长期未登录，请重新登录！");
//                                return;
//                            } else {
//                                HandlerUtils.sendMsg(handler, NODATA, "上传_失败");
//                            }
//                        }
//                    } else {
//                        HandlerUtils.sendMsg(handler, SHOW_TOAST, "该工单任务未完成");
//                        return;
//                    }
                    } else {
                        HandlerUtils.sendMsg(handler, SHOW_TOAST, "请添加工单拍照");
                    }

                }
            }catch (DbException e) {
                e.printStackTrace();
            }
        }
            else {
            //没有可上传数据
            HandlerUtils.sendMsg(handler, NODATA, "没有可上传的数据");
            return;
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
                HandlerUtils.sendMsg(handler, NODATA, "没有已盘捆包的数据。");
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
        To_InventorActivity.this.finish();
    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        new ReadLocalDBThread().start();
//    }
}
