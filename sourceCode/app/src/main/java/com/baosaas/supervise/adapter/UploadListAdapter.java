package com.baosaas.supervise.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baosaas.supervise.Model.TaskImage;
import com.baosaas.supervise.R;
import com.baosaas.supervise.activity.LoginActivity;
import com.baosaas.supervise.activity.ShowImage;
import com.baosaas.supervise.activity.UploadInventorActivity;
import com.baosaas.supervise.bean.Uploadill;
import com.baosaas.supervise.util.DbSupport;
import com.baosaas.supervise.util.StringUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.List;


public class UploadListAdapter extends BaseAdapter {
    private Context context;
    private List<Uploadill> uploadBeans;
    private Callbacks callbacks;
    public static ImageView imageView;
    public static TextView textView;

    public UploadListAdapter(Context context, List<Uploadill> uploadBeans, Callbacks callbacks) {
        this.context = context;
        this.uploadBeans = uploadBeans;
        this.callbacks = callbacks;
    }

    @Override
    public int getCount() {
        return uploadBeans.size();
    }

    @Override
    public Uploadill getItem(int position) {
        return uploadBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null || convertView.getTag() == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
            viewHolder = new ViewHolder();
            viewHolder.frameLayout = (FrameLayout) convertView.findViewById(R.id.item_frame);
            viewHolder.lnItemclick = (LinearLayout) convertView.findViewById(R.id.item_linear);
            viewHolder.tackImage = (ImageView) convertView.findViewById(R.id.item_frame_image);
            viewHolder.mType = (TextView) convertView.findViewById(R.id.tv_item_Type);
            viewHolder.financialwarehouse = (TextView) convertView.findViewById(R.id.tv_item_position);
            viewHolder.mBusinessBillId = (TextView) convertView.findViewById(R.id.tv_item_businessBillId);
            viewHolder.mNum = (TextView) convertView.findViewById(R.id.tv_item_nums);
            viewHolder.mWeights = (TextView) convertView.findViewById(R.id.tv_item_weights);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Uploadill bill = getItem(position);

        viewHolder.financialwarehouse.setText(bill.getFinancialwarehouse());//金融仓库
        viewHolder.mBusinessBillId.setText(bill.getTaskCode());//工单号
        viewHolder.mNum.setText(bill.getNums() + "件/");//件数
        viewHolder.mWeights.setText(bill.getWeights() + "T");
        viewHolder.lnItemclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到详情页面
                Intent intent = new Intent(context, UploadInventorActivity.class);
                intent.putExtra("taskCode", bill.getTaskCode());
                context.startActivity(intent);
            }
        });
        if (StringUtils.isNotEmpty(bill.getTaskImage())) {
            viewHolder.mType.setVisibility(View.INVISIBLE);
            byte[] bytes = Base64.decode(bill.getTaskImage(), Base64.DEFAULT);
            viewHolder.tackImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        }
        viewHolder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                imageView = viewHolder.tackImage;
                textView = viewHolder.mType;
                if (textView.getVisibility() == View.INVISIBLE) {
                    //有图片时  点击查看跳转ShowImage
//                                  Intent intent = new Intent(context, ShowImage.class);
//                    intent.putExtra("boolean", false);
//                    byte[] bytes = Base64.decode(bill.getTaskImage(), Base64.DEFAULT);
//                    intent.putExtra("bitmap", bytes);
//                    context.startActivity(intent);

                    new AlertDialog.Builder(context).setTitle("提示").setMessage("图片操作").setPositiveButton("查看大图", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(context, ShowImage.class);
                            byte[] bytes = Base64.decode(bill.getTaskImage(), Base64.DEFAULT);
                            intent.putExtra("bitmap", bytes);
                            context.startActivity(intent);
                        }
                    }).setNegativeButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                TaskImage taskImage = DbSupport.getInstance().findFirst(Selector.from(TaskImage.class).where("taskCode", "=", bill.getTaskCode()));
                                if (taskImage != null) {
                                    taskImage.setTaskImage("");
                                    DbSupport.getInstance().update(taskImage);
                                    bill.setTaskImage("");
                                    viewHolder.tackImage.setImageBitmap(null);
                                    viewHolder.tackImage.setVisibility(View.INVISIBLE);
                                    textView.setVisibility(View.VISIBLE);
                                }
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    }).setNeutralButton("重拍", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            if (filename != null && filename.exists()) {
//                                filename.delete();
//                                Log.e("delete---->", "---------OK----------");
//                            }
//                            getPhotoCamera();
                            callbacks.onclick(v, position);
                        }
                    }).create().show();
                    return;
                }
                callbacks.onclick(v, position);

            }
        });

//        viewHolder.tackImage.setImageBitmap(collbacks.onclick());
//        bitmap = UploadLibraryActivity.collbacks.onclick();
//
//        viewHolder.tackImage.setImageBitmap(bitmap);
        return convertView;
    }

    static class ViewHolder {
        private LinearLayout lnItemclick;
        private TextView mBusinessBillId;       //工单号
        private TextView financialwarehouse;    //金融库位
        private TextView mType;                 //添加图片的  Textview
        private TextView mNum;                  //件数
        private TextView mWeights;              //总重
        private ImageView tackImage;
        private FrameLayout frameLayout;
    }

    public interface Callbacks {
        void onclick(View view, int i);
    }
}