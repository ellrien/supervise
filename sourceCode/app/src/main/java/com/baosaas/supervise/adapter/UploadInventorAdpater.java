package com.baosaas.supervise.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baosaas.supervise.Model.Inventory;
import com.baosaas.supervise.R;

import java.util.List;

/**
 * 上传解押列表适配器
 */
public class UploadInventorAdpater extends BaseAdapter {
    private Context context;
    private List<Inventory> listbound;

    public UploadInventorAdpater() {

    }

    public UploadInventorAdpater(Context context, List<Inventory> listbound) {
        this.context = context;
        this.listbound = listbound;
    }

    @Override
    public int getCount() {
        return listbound.size();
    }

    @Override
    public Inventory getItem(int position) {
        return listbound.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null || convertView.getTag() == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_upunbind_item, null);
            viewHolder = new ViewHolder();
            viewHolder.linear_background = (LinearLayout) convertView.findViewById(R.id.linear_background);
            viewHolder.barCode = (TextView) convertView.findViewById(R.id.barCode);
            viewHolder.mPackNum = (TextView) convertView.findViewById(R.id.tv_packNum);
            viewHolder.mCount = (TextView) convertView.findViewById(R.id.tv_unbound_count);
            viewHolder.mWeight = (TextView) convertView.findViewById(R.id.tv_unbound_weight);
            viewHolder.mPosition = (TextView) convertView.findViewById(R.id.tv_unbound_position);
            viewHolder.lnItemclick = (LinearLayout) convertView.findViewById(R.id.ln_itemclick);
            viewHolder.but_error = (Button) convertView.findViewById(R.id.but_error);
//            viewHolder.tvUnbind = (TextView) convertView.findViewById(R.id.tv_unbind);
            viewHolder.mTagId = (TextView) convertView.findViewById(R.id.tv_unbound_tagId);
//            viewHolder.unboundCount = (TextView)convertView.findViewById(R.id.tv_boundcount);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Inventory listOut = getItem(position);
        viewHolder.mPackNum.setText(listOut.getPackNum());
        viewHolder.mWeight.setText(listOut.getWeight() + "T");
        viewHolder.mPosition.setText(listOut.getPosition());
        viewHolder.mTagId.setText(listOut.getTag());
        viewHolder.barCode.setText(listOut.getBarCode());
        if (listOut.getStatus() == 1) {
            if (listOut.getCheckErrorDesc() != null) {
                if (!"正常".equals(listOut.getCheckErrorDesc())) {
                    viewHolder.linear_background.setBackgroundResource(R.color.yellow_new);
                    viewHolder.but_error.setText(listOut.getCheckErrorDesc());
                    viewHolder.but_error.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.but_error.setVisibility(View.GONE);
                    viewHolder.linear_background.setBackgroundResource(R.color.white);
                }
            }
        }
//        viewHolder.lnItemclick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                RedirectUtil.go2UnbounddetailVis(context,listOut.getPackNum());
//                Intent intent = new Intent(context, DetailsActivity.class);
//                intent.putExtra("billNo",listOut.getTaskCode());
//                intent.putExtra("packNum", listOut.getPackNum());
//                context.startActivity(intent);
//            }
//        });

        return convertView;
    }


    static class ViewHolder {
        private LinearLayout linear_background;
        private LinearLayout lnItemclick;
        private TextView mCount;//件数
        private TextView mPosition;//
        private TextView mWeight;//重量
        private TextView mPackNum;//
        private Button but_error;
        private TextView barCode;
        //        private TextView tvUnbind;//状态
        private TextView mTagId;//标签号
    }
}