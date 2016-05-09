package com.baosaas.supervise.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baosaas.supervise.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 货物详情页的异常信息适配器
 * Created by jb on 2015/12/15.
 */
public class UnusualMsgAdapter extends BaseAdapter {


    /**
     * 异常选中状态
     */
    private List<Boolean> isSelect;

    /**
     * 异常信息集合
     */
    private List<String> mUnusualMsg;

    private Context context;

    public UnusualMsgAdapter(Context context, List<String> msg) {
        this.context = context;
        mUnusualMsg = msg;
        isSelect = new ArrayList<>(msg.size());
        for (int i = 0; i < msg.size(); i++) {
            isSelect.add(false);
        }
    }

    /**
     * 全部取消选中
     */
    public void isAllFalse() {
        for (int i = 0; i < isSelect.size(); i++) {
            isSelect.set(i, false);
        }
        this.notifyDataSetInvalidated();
    }

    /**
     * 选中position条目
     */
    public void isSelect(int position) {
        for (int i = 0; i < isSelect.size(); i++) {
            isSelect.set(i, i == position);
        }
        this.notifyDataSetInvalidated();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_details_item,null);
            holder = new ViewHolder();
            holder.tvUnusualMsg = (TextView) convertView.findViewById(R.id.tv_unusual_msg);
            holder.ivStateUnusual = (ImageView) convertView.findViewById(R.id.iv_unusual);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvUnusualMsg.setText(getItem(position));
        holder.ivStateUnusual.setImageResource(isSelect.get(position) ? R.mipmap.ico_pitchon : R.mipmap.ico_choice);

        return convertView;
    }

    @Override
    public int getCount() {
        return mUnusualMsg == null ? 0 : mUnusualMsg.size();
    }

    @Override
    public String getItem(int position) {
        return mUnusualMsg.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView tvUnusualMsg;
        ImageView ivStateUnusual;
    }
}
