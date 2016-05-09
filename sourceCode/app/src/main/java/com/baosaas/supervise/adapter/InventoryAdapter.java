package com.baosaas.supervise.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baosaas.supervise.R;
import com.baosaas.supervise.bean.InventoryBill;
import com.baosaas.supervise.util.RedirectUtil;

import java.util.List;

/**
 * Created by ZhangZhiCheng on 2015/12/15.
 * 盘库窗口盘库单列表适配器
 */
public class InventoryAdapter extends BaseAdapter {
    private Context context;
    private List<InventoryBill> dataList;

    public InventoryAdapter(Context context, List<InventoryBill> dataList)
    {
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
            view = LayoutInflater.from(context).inflate(R.layout.list_inventory_item, null);
            viewTag = new ViewTag();
            viewTag.mCheckType = (TextView) view.findViewById(R.id.checkType);
            viewTag.mStoreName = (TextView) view.findViewById(R.id.storeName);
            viewTag.mBillNo = (TextView) view.findViewById(R.id.billNo);
            viewTag.mCheckNum = (TextView) view.findViewById(R.id.checkNum);
            viewTag.mCheckDate = (TextView) view.findViewById(R.id.checkDate);
            viewTag.mWeight=(TextView)view.findViewById(R.id.tv_weight);
//            viewTag.mSystemNo = (TextView) view.findViewById(R.id.systemNo);
//            viewTag.mAccept = (Button) view.findViewById(R.id.acceptTaskButton);
            viewTag.mItemClick = (LinearLayout) view.findViewById(R.id.ln_inven_click);
        } else {
            viewTag = (ViewTag) view.getTag();
        }
        final InventoryBill bill = (InventoryBill)getItem(i);
        viewTag.mCheckType.setText(bill.getTypeDesc());
        if (bill.getTypeDesc().equals("全盘")){
            viewTag.mCheckType.setBackgroundResource(R.color.deep_blue);//蓝色
        }else if (bill.getTypeDesc().equals("抽盘")){
            viewTag.mCheckType.setBackgroundResource(R.color.cyan);//青色
        }else {       //质押
            viewTag.mCheckType.setBackgroundResource(R.color.backred);//红色
        }
        viewTag.mBillNo.setText(bill.getTaskCode());
        viewTag.mCheckDate.setText(bill.getCreateDate());
        viewTag.mCheckNum.setText(Integer.toString(bill.getNums()));
        viewTag.mWeight.setText(bill.getWeight()+"");
//        viewTag.mSystemNo.setText(bill.getSysId());
        viewTag.mStoreName.setText(bill.getFinancialwarehouse());
//        viewTag.mAccept.setOnClickListener(new AcceptButtonListener(context, bill.getTaskCode()));
        viewTag.mItemClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RedirectUtil.go2InventryDetail(context, bill.getTaskCode());
            }
        });

        return view;
    }

    class ViewTag
    {
        TextView mCheckType;
        TextView mStoreName;
        TextView mBillNo;
        TextView mCheckNum;
        TextView mCheckDate;
        TextView mWeight;
//        TextView mSystemNo;
//        Button mAccept;
        LinearLayout mItemClick;
    }

//    class AcceptButtonListener implements View.OnClickListener {
//        private String billNo;
//        private Context context;
//        AcceptButtonListener(Context context,String billNo) {
//            this.context = context;
//            this.billNo = billNo;
//        }
//
//        @Override
//        public void onClick(View v) {
//            Intent intent = new Intent(context, InventoryActivity.class);
//            intent.putExtra("billNo",billNo );
//            context.startActivity(intent);
//        }
//    }
}

