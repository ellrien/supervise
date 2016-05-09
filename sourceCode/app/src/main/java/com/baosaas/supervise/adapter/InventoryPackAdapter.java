package com.baosaas.supervise.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.baosaas.supervise.R;
import com.baosaas.supervise.bean.InventoryPack;

import java.util.List;

/**
 * Created by ZhangZhiCheng on 2015/12/15.
 * 盘库单明细窗口盘库单明细列表适配器
 */
public class InventoryPackAdapter extends BaseAdapter {
    private Context context;
    private List<InventoryPack> dataList;

    public InventoryPackAdapter(Context context, List<InventoryPack> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewTag viewTag;
        if (view == null || view.getTag() == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_pack_item, null);
            viewTag = new ViewTag();
            viewTag.tvPackNum = (TextView) view.findViewById(R.id.packNum);
            viewTag.tvWeight = (TextView) view.findViewById(R.id.weight);
            viewTag.tvBarcode = (TextView) view.findViewById(R.id.barCode);
            viewTag.tvPosition = (TextView) view.findViewById(R.id.location);
            viewTag.tvStandard = (TextView) view.findViewById(R.id.standard);
            viewTag.tvTag = (TextView) view.findViewById(R.id.tv_tagId);
            viewTag.linear_background = (LinearLayout) view.findViewById(R.id.linear_background);
            viewTag.but_error = (Button) view.findViewById(R.id.but_error);
//            viewTag.item_linear = (LinearLayout) view.findViewById(R.id.item_linear);
            view.setTag(viewTag);
        } else {
            viewTag = (ViewTag) view.getTag();
        }
        InventoryPack bill = (InventoryPack) getItem(i);
        viewTag.tvPackNum.setText(bill.getPackNum());
        viewTag.tvWeight.setText(bill.getWeight() + " T");
        viewTag.tvBarcode.setText(bill.getBarCode());
        viewTag.tvPosition.setText(bill.getPosition());
        viewTag.tvStandard.setText(bill.getStandard());
        viewTag.tvTag.setText(bill.getTag());
//        String checkErrorDesc = bill.getCheckErrorDesc();
        String checkType = bill.getCheckErrorDesc() + "";
        if (bill.getStatus() == 1) {
            if (bill.getCheckErrorDesc() != null) {
                if (!"正常".equals(bill.getCheckErrorDesc())) {
                    viewTag.linear_background.setBackgroundResource(R.color.yellow_new);
                    viewTag.but_error.setText(bill.getCheckErrorDesc());
                    viewTag.but_error.setVisibility(View.VISIBLE);
                } else {
                    viewTag.but_error.setVisibility(View.GONE);
                    viewTag.linear_background.setBackgroundResource(R.color.white);
                }
            }
        }
        return view;
    }

    class ViewTag {
        public TextView tvPackNum;
        public TextView tvWeight;
        public TextView tvBarcode;
        public TextView tvPosition;
        public TextView tvStandard;
        //        public TextView tvResult;
        private LinearLayout linear_background;
        private Button but_error;
        private TextView tvTag;
//        private LinearLayout item_linear;
    }
}
