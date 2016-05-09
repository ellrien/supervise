package com.baosaas.supervise.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baosaas.supervise.Model.Inventory;
import com.baosaas.supervise.Model.TaskImage;
import com.baosaas.supervise.R;
import com.baosaas.supervise.adapter.UploadListAdapter;
import com.baosaas.supervise.api.InventoryInterface;
import com.baosaas.supervise.base.BaseActivity;
import com.baosaas.supervise.bean.InventoryIn;
import com.baosaas.supervise.bean.UploadBills;
import com.baosaas.supervise.bean.UploadOut;
import com.baosaas.supervise.bean.Uploadill;
import com.baosaas.supervise.util.BitmapUtil;
import com.baosaas.supervise.util.Config;
import com.baosaas.supervise.util.DbSupport;
import com.baosaas.supervise.util.HandlerUtils;
import com.baosaas.supervise.util.NetSupport;
import com.baosaas.supervise.util.RedirectUtil;
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
 * 上传盘库页
 */
public class UploadLibraryActivity extends BaseActivity implements View.OnClickListener, UploadListAdapter.Callbacks {
    private UploadListAdapter listadapter;
    private List<Uploadill> uploadList;
    private List<Inventory> inventoryList;
    private String taskCode, photoName;
    private File filename;
    private List<InventoryIn> invenInList = new ArrayList<>();
    private final static int ACTION_PHONE_CAMERA = 2;
    private static final int OUT_NULL = 9;
    private static final int SHOW_DIALOG = 10;
    private static final int TOAST_SUCCEED = 666;
    private static final int TOAST_DEFEAT = 0;
    private static final int TOAST_SUCCESSD = 1;
    private static final int TOAST_DIE = 99;
    private static final int NODATA = 2;
    private static final int H_UNLL = 4;
    private ImageView imagephoto;
    @ViewInject(R.id.pk_list)
    private ListView listView;
    @ViewInject(R.id.img_back_upload)
    private ImageView backupload;
    @ViewInject(R.id.text_back)
    private TextView text_back;
    @ViewInject(R.id.btn_upload)
    private Button refresh;
    @ViewInject(R.id.ln_search)
    private LinearLayout ln_search;
    @ViewInject(R.id.tv_title_warehouse)
    private TextView tv_title_warehouse;
    @ViewInject(R.id.lin_1)
    private LinearLayout lin_1;
    @ViewInject(R.id.lin_tv)
    private TextView lin_tv;
    private SharedUtil config;
    private String imgstr;
    private byte[] bytes;
    private Bitmap bitmap2;
    private int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadlibary);
        ViewUtils.inject(this);
        config = SharedUtil.getInstance();
        inventoryList = new ArrayList<>();
        new NewThread().start();
        backupload.setOnClickListener(this);
        text_back.setOnClickListener(this);
        refresh.setOnClickListener(this);
    }


    public void getPhotoCamera() {
        //调用系统拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File filed = new File(Environment.getExternalStorageDirectory() + "/baosaasphoto");
        if (!filed.exists()) {
            filed.mkdir();
        }
        filename = new File(filed, getFileName());
        Uri uri = Uri.fromFile(filename);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, ACTION_PHONE_CAMERA);
    }

    public String getFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("_yyyy_MMdd_HHmmss");
        photoName = taskCode + dateFormat.format(date) + ".jpg";
        return photoName;
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {  //【拍照
                case ACTION_PHONE_CAMERA:
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                        //判断sd卡是否挂载
                        return;
                    }
                    String filepath = filename.getAbsolutePath();
                    Log.e("filepath-------->", filepath);
                    Bitmap bitmap1 = BitmapUtil.getSmallBitmap(filepath);
                    bitmap2 = BitmapUtil.compressImage(bitmap1);
                    Matrix matrix = BitmapUtil.getMatrix(filepath);
                    bitmap2 = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix, true);
                    bitmap1 = BitmapUtil.compressImage(bitmap2);
                    UploadListAdapter.imageView.setImageBitmap(bitmap1);
                    UploadListAdapter.textView.setVisibility(View.INVISIBLE);
                    bytes = BitmapUtil.outstream.toByteArray();
                    Log.e("拍照bytes.lenght--------", bytes.length + "");
                    imgstr = Base64.encodeToString(bytes, Base64.DEFAULT);
                    try {
                        TaskImage taskImage = DbSupport.getInstance().findFirst(Selector.from(TaskImage.class).where("taskCode", "=", taskCode));
                        if (taskImage != null) {
                            taskImage.setTaskImage(imgstr);
                            DbSupport.getInstance().update(taskImage);
                        } else {
                            TaskImage taskImage1 = new TaskImage();
                            taskImage1.setTaskCode(taskCode);
                            taskImage1.setTaskImage(imgstr);
                            DbSupport.getInstance().save(taskImage1);
                            return;
                        }
                        for (int i = 0; i < uploadList.size(); i++) {
                            if (uploadList.get(i).getTaskCode().equals(taskCode)) {
                                uploadList.get(i).setTaskImage(imgstr);
                                break;
                            }
                        }
                        if (filename.exists()) {
                            filename.delete();
                        }
//                        listadapter.notifyDataSetChanged();
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } else {
            return;
        }

    }

    @Override
    public void onclick(View view, int index) {
//        this.index = index;
//        if (UploadListAdapter.textView.getVisibility() == View.INVISIBLE) {
//            Intent intent = new Intent(UploadLibraryActivity.this, ShowImage.class);
//            intent.putExtra("boolean", false);
//            if (bytes == null) {
//                bytes = Base64.decode(uploadList.get(index).getTaskImage(), Base64.DEFAULT);
//            }
//            intent.putExtra("bitmap", bytes);
//            startActivity(intent);
//        } else {
        taskCode = uploadList.get(index).getTaskCode();
        getPhotoCamera();
//        }
    }


    class NewThread extends Thread {
        @Override
        public void run() {
            setValuse();
        }
    }

    //从本地数据库表中获取数据
    public void setValuse() {
        uploadList = new ArrayList<Uploadill>();
        try {
            inventoryList = DbSupport.getInstance().findAll(Selector.from(Inventory.class).where("status", "=", Config.UN_UPLOAD));
            if (inventoryList != null) {
                num = inventoryList.size();
            }
            String sql = "select taskCode,count(*) as nums,financialwarehouse,sum(weight) from inventory where status = 1 group by taskCode,financialwarehouse";
            Cursor cursor = DbSupport.getInstance().execQuery(sql);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Uploadill bill = new Uploadill();
                    bill.setNums(cursor.getInt(1));
                    bill.setTaskCode(cursor.getString(0));
                    bill.setFinancialwarehouse(cursor.getString(2));
                    bill.setWeights(cursor.getString(3));
                    uploadList.add(bill);
                }
//            //查询工单照片表  向upLoadList中赋值
                for (Uploadill uploadill : uploadList) {
                    TaskImage taskImages = DbSupport.getInstance().findFirst(Selector.from(TaskImage.class).where("taskCode", "=", uploadill.getTaskCode()));
                    if (taskImages != null && StringUtils.isNotEmpty(taskImages.getTaskImage())) {
                        uploadill.setTaskImage(taskImages.getTaskImage());
                    }
                }
                HandlerUtils.sendMsg(handler, TOAST_SUCCEED, "");
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        //获取要上传的实体类bu
        if (uploadList.size() == 0 || uploadList == null) {  //没数据  给提示
            HandlerUtils.sendMsg(handler, NODATA, "没有未上传盘库单");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_back:
            case R.id.img_back_upload:
                this.finish();
                break;
            case R.id.btn_upload:
                //上传事件
                setRefresh();
                break;
            default:
                break;
        }
    }

    public void handleMsg(Message msg) {
        if (msg != null) {
            switch (msg.what) {
                case TOAST_DEFEAT:
                    hideProgress();
                    showToast(msg.obj.toString().trim());
                    break;
                case TOAST_SUCCESSD:
                    hideProgress();
                    showToast(msg.obj.toString());
                    for (InventoryIn inventoryIn : invenInList) {
                        try {
                            num = num - inventoryIn.getPackList().size();
                            DbSupport.getInstance().deleteAll(inventoryIn.getPackList());
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                    invenInList.clear();
                    new NewThread().start();
                    break;
                case TOAST_DIE:
                    hideProgress();
                    showToast("校验失败");
                    break;
                case OUT_NULL:
                    hideProgress();
                    showToast("上传_失败 ");
                    break;
                case TOAST_SUCCEED:
                    hideProgress();
                    lin_tv.setText("共有" + uploadList.size() + "条盘库单" + num + "条捆包可上传");
                    listadapter = new UploadListAdapter(UploadLibraryActivity.this, uploadList, this);
                    listView.setAdapter(listadapter);
                    break;
                case NODATA:
                    hideProgress();
                    showToast(msg.obj.toString().trim());
                    lin_tv.setText(msg.obj.toString().trim());
                    refresh.setEnabled(false);
                    break;
                case H_UNLL:
                    hideProgress();
                    lin_tv.setText("没有未上传盘库单！！！");
                    break;
                case SHOW_DIALOG:
                    hideProgress();
                    showToast(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    }

    //上传按钮调用的方法
    public void setRefresh() {
        if (!NetSupport.checkNetWork(UploadLibraryActivity.this)) { //网络不通
//            showToast("当前没有网络，请在网络下上传");
            showToast(this, "当前没有网络，请在网络环境下上传", 1000);
            return;
        }
        new AlertDialog.Builder(this).setTitle("友情提示").setMessage("是否上传已盘库数据").setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //开启线程 上传盘库数据
                        showProgress("上传中", false);
                        new UploadThread().start();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();

    }

    class UploadThread extends Thread {
        @Override
        public void run() {
            Uploading();
        }
    }

    public void Uploading() {   //开启线程调用上传的方法

        if (uploadList != null && uploadList.size() != 0) {

            InventoryIn inventoryIn;
            for (int i = 0; i < uploadList.size(); i++) {
                if (StringUtils.isNotEmpty(uploadList.get(i).getTaskImage())) {
                    //判断图片
                    try {
                        Inventory inventory = DbSupport.getInstance().findFirst(Selector.from(Inventory.class).where("taskCode", "=", uploadList.get(i).getTaskCode()).and("status", "=", Config.NEW_DATA));
                        if (inventory == null) {
                            //判断是否盘完
                            List<Inventory> list = DbSupport.getInstance().findAll(Selector.from(Inventory.class).where("taskCode", "=", uploadList.get(i).getTaskCode()).and("status", "=", Config.UN_UPLOAD));
                            inventoryIn = new InventoryIn();
                            inventoryIn.setPackList(list);
                            inventoryIn.setTaskImage(uploadList.get(i).getTaskImage());
                            inventoryIn.setTaskCode(uploadList.get(i).getTaskCode());
                            invenInList.add(inventoryIn);
                        } else {
//                            HandlerUtils.sendMsg(handler, SHOW_DIALOG, "你的工单尚未完成！");
                            continue;
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                } else {
//                    HandlerUtils.sendMsg(handler, SHOW_DIALOG, "请先拍工单照片");
                    continue;
                }
            }
        }


        if (null == invenInList || invenInList.size() == 0) {
            HandlerUtils.sendMsg(handler, SHOW_DIALOG, "没有可以上传的盘库单");
            hideProgress();
            return;
        }
        InventoryInterface ui = new InventoryInterface();
        UploadBills bills = new UploadBills(config.userCode, config.accessToken, invenInList);
        UploadOut out = ui.updateInventoryTask(bills);
        if (out != null) {
            if (out.getFlag() == 0) { //失败
                HandlerUtils.sendMsg(handler, TOAST_DEFEAT, out.getMsg());
            } else if (out.getFlag() == 1) { //成功
                HandlerUtils.sendMsg(handler, TOAST_SUCCESSD, out.getMsg());
            } else if (out.getFlag() == 2) {//部分判断出错

            } else if (out.getFlag() == 99) { //校验失败
                HandlerUtils.sendMsg(handler, TOAST_DIE, "因长期未登录，请重新登录！");
                return;
            }
        }
    }

    //防止结束activity。finish 后tosat还依次弹出
    private static Toast mToast = null;

    public static void showToast(Context context, String text, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, duration);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.setText(text);
            mToast.setDuration(duration);
        }

        mToast.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new NewThread().start();
    }
}