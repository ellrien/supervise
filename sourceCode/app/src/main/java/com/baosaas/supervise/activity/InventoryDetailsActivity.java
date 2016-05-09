package com.baosaas.supervise.activity;

import android.app.AlertDialog;
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
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baosaas.supervise.Model.Inventory;
import com.baosaas.supervise.R;
import com.baosaas.supervise.adapter.UnusualMsgAdapter;
import com.baosaas.supervise.base.BaseActivity;
import com.baosaas.supervise.util.BitmapUtil;
import com.baosaas.supervise.util.Config;
import com.baosaas.supervise.util.DbSupport;
import com.baosaas.supervise.util.HandlerUtils;
import com.baosaas.supervise.util.StringUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/12/25.
 */
public class InventoryDetailsActivity extends BaseActivity {
    public static final int DETAIL_REQCODE = 1;
    private final static int ACTION_PHONE_CAMERA = 1, ACTION_PHONE_ALBUM = 2;
    private final static int IMAGE_SHOW = 5;
    private File filename;
    private String imgstr = "";
    private String photoName;
    private boolean flags = false;
    private byte[] bytes;
    /**
     * 货物状态集合
     */
    private final int[] TYPES = new int[]{Config.check_Error_Type1,
            Config.check_Error_Type2,
            Config.check_Error_Type3,
            Config.check_Error_Type4,
            Config.check_Error_Type5,
            Config.check_Error_Type6,
            Config.check_Error_Type};
    /**
     * 标题
     */
    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.image_photo)
    private ImageView image_photo;

    @ViewInject(R.id.button_photo)
    private TextView button_photo;

    @ViewInject(R.id.details_linear)
    private LinearLayout details_linear;

    @ViewInject(R.id.tv_back)
    TextView tBack;
    /**
     * 正常布局
     */
    @ViewInject(R.id.ll_normal)
    private LinearLayout llNormal;
    /**
     * 正常选中图片
     */
    @ViewInject(R.id.iv_normal)
    private ImageView ivNormal;
    /**
     * 异常listview
     */
    @ViewInject(R.id.lv_unusual)
    private ListView lvUnusual;

    private UnusualMsgAdapter mAdapter;
//    /**
//     * 货物状态
//     */
//    private String mType;
    /**
     * 货物异常信息
     */
    private String mTypeMsg;
    private String mPackNum;
    private String taskCode;

    @Override
    public void handleMsg(Message msg) {
        switch (msg.what) {
            case IMAGE_SHOW:
                button_photo.setVisibility(View.INVISIBLE);
                image_photo.setVisibility(View.VISIBLE);
                image_photo.setImageBitmap((Bitmap) msg.obj);
                break;
        }
        super.handleMsg(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ViewUtils.inject(this);
        init();
        findImage();

    }

    public void findImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Inventory unbounds = DbSupport.getInstance().findFirst(Selector.from(Inventory.class).where("packNum", "=", mPackNum).and("taskCode", "=", taskCode));
                    if (unbounds != null) {
                        if (StringUtils.isNotEmpty(unbounds.getPackImage())) {
                            bytes = Base64.decode(unbounds.getPackImage(), Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            HandlerUtils.sendMsg(handler, IMAGE_SHOW, bitmap);
                        }
                    } else {
                        return;
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void init() {
        Intent intent = getIntent();
        mPackNum = intent.getStringExtra("packNum");
        taskCode = intent.getStringExtra("billNo");
        tBack.setText("返回");
        mTypeMsg = intent.getStringExtra("checkErrorDesc");
        if (null == mTypeMsg || mTypeMsg.equals("")) {
            mTypeMsg = "正常";
        }
//        int intmType=Integer.parseInt(mType);
//        mTypeMsg = Config.getCheckErrorDesc(intmType);
        if (!TextUtils.isEmpty(mPackNum)) {
            tvTitle.setText(mPackNum);
        }

        List<String> data = Config.getCheckErrorTypes();
        //移除Config中最后一个type（无）
        data.remove(data.size() - 1);
        mAdapter = new UnusualMsgAdapter(this, data);
        lvUnusual.setAdapter(mAdapter);

        if (mTypeMsg.equals("正常")) {
            ivNormal.setImageResource(R.mipmap.ico_pitchon);
        } else {
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).equals(mTypeMsg)) {
                    mAdapter.isSelect(i);
                }
            }
        }

        lvUnusual.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setState(Config.getCheckErrorDesc(TYPES[position]));
            }
        });
        llNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setState(Config.getCheckErrorDesc(Config.check_Error_Type));
            }
        });

        //设置初始选中状态
//        new InitType().start();
    }

    /**
     * 设置货物状态
     */
    public void setState(String type) {
        if (("正常").equals(type)) {
            mAdapter.isAllFalse();
            ivNormal.setImageResource(R.mipmap.ico_pitchon);
        } else {
            ivNormal.setImageResource(R.mipmap.ico_choice);
            for (int i = 0; i < TYPES.length; i++) {
                if ((TYPES[i] + "").equals(Config.getCheckErrorTypes(type))) {
                    mAdapter.isSelect(i);
                    break;
                }
            }
        }
//        mType = type;
//        int inttype = Integer.parseInt(type);
        mTypeMsg = type;
    }

    /**
     * 初始化type状态
     */
//    class InitType extends Thread {
//        @Override
//        public void run() {
//
//            try {
//                Inventory unbound = DbSupport.getInstance().findFirst(Selector.from(Inventory.class).where("taskCode", "=", taskCode).and("packNum", "=", mPackNum));
//                if (unbound != null) {
//                    if (StringUtils.isEmpty(unbound.getCheckErrorDesc())) {
//                        HandlerUtils.sendMsg(handler, SETSTATE_ERROR, "正常");
////                        setState("正常");
//                    } else {
//                        HandlerUtils.sendMsg(handler, SETSTATE_ERROR, unbound.getCheckErrorDesc());
//                    }
//                }
//            } catch (DbException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    /**
     * 保存type状态
     */
    class SaveType extends Thread {
        @Override
        public void run() {
            try {
                Inventory unbound = DbSupport.getInstance().findFirst(Selector.from(Inventory.class).where("taskCode", "=", taskCode).and("packNum", "=", mPackNum));
                if (unbound != null) {
                    unbound.setCheckErrorDesc(mTypeMsg);
                    unbound.setStatus(Config.UN_UPLOAD);
                    if (flags) {
                        unbound.setPackImage(imgstr);
                    }
                    DbSupport.getInstance().update(unbound);
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
            InventoryDetailsActivity.this.finish();
        }
    }

    @OnClick(R.id.ll_sure_details)
    public void onSure(View view) {

        if (StringUtils.isNotEmpty(mTypeMsg)) {
            Intent mIntent = new Intent();
//            mIntent.putExtra("taskCode", taskCode);
            mIntent.putExtra("packNum", mPackNum);
            mIntent.putExtra("billNo", taskCode);
            mIntent.putExtra("checkErrorDesc", mTypeMsg);
            setResult(DETAIL_REQCODE, mIntent);
            new SaveType().start();
//            UnboundDetailsActivity.this.finish();
        } else {
            showToast("请先选择货物状态");
        }
    }

    @OnClick(R.id.ll_back_details)
    public void back(View view) {
        onBackPressed();
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.button_photo)
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
        photoName = mPackNum + dateFormat.format(date) + ".jpg";
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
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {  //【拍照
                case ACTION_PHONE_CAMERA:
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                        //判断sd卡是否挂载
                        return;
                    }
                    button_photo.setVisibility(View.INVISIBLE);
                    image_photo.setVisibility(View.VISIBLE);
                    String filepath = filename.getAbsolutePath();
                    Log.e("filepath-------->", filepath);
                    Bitmap bitmap1 = BitmapUtil.getSmallBitmap(filename.getAbsolutePath());
                    Bitmap bitmap2 = BitmapUtil.compressImage(bitmap1);
                    Matrix matrix = BitmapUtil.getMatrix(filepath);
                    bitmap2 = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix, true);
                    bitmap1 = BitmapUtil.compressImage(bitmap2);
                    image_photo.setImageBitmap(bitmap1);
                    byte[] by = BitmapUtil.outstream.toByteArray();
                    Log.e("拍照bytes.lenght--------", by.length + "");
                    imgstr = Base64.encodeToString(by, Base64.DEFAULT);
                    bytes = by;
                    if (filename.exists()) {
                        filename.delete();
                    }
//                    try {
//                        Inventory unbound = DbSupport.getInstance().findFirst(Selector.from(Inventory.class).where("taskCode", "=", taskCode).and("packNum", "=", mPackNum));
//                        unbound.setPackImage(imgstr);
//                        DbSupport.getInstance().update(unbound);
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                    }
                    flags = true;
                    break;
            }
        } else {
            return;
        }

    }


    @OnClick(R.id.image_photo)
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        new AlertDialog.Builder(this).setTitle("提示").setMessage("图片操作").setPositiveButton("查看大图", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(InventoryDetailsActivity.this, ShowImage.class);
                intent.putExtra("bitmap", bytes);
                startActivity(intent);
            }
        }).setNegativeButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                imgstr = "";
                flags = true;
                image_photo.setImageBitmap(null);
                image_photo.setVisibility(View.INVISIBLE);
                button_photo.setVisibility(View.VISIBLE);
            }
        }).setNeutralButton("重拍", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                getPhotoCamera();
            }
        }).create().show();
    }
}
