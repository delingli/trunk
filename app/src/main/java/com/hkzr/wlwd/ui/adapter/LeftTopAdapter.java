package com.hkzr.wlwd.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hkzr.wlwd.R;
import com.hkzr.wlwd.model.LeftGroupBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by admin on 2017/6/13.
 */

public class LeftTopAdapter extends BaseAdapter {
    Context context;
    List<LeftGroupBean.TopbarBean.ListBean> list;

    public LeftTopAdapter(Context context, List<LeftGroupBean.TopbarBean.ListBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list != null) {
            if (list.size() > 4) {
                return 4;
            } else {
                return list.size();
            }
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridHolder groupHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_app_top, null);
            groupHolder = new GridHolder();
            groupHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            groupHolder.iv_path = (ImageView) convertView.findViewById(R.id.iv_path);
            groupHolder.iv_dian = (ImageView) convertView.findViewById(R.id.iv_dian);
            groupHolder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GridHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(list.get(position).getFunIcon())) {
            Picasso.with(context).load(list.get(position).getFunIcon()).error(R.drawable.coffee_blue).into(groupHolder.iv_path);
        }
        String Stamp = list.get(position).getStamp();
        if (!TextUtils.isEmpty(Stamp)) {
            if ("0".equals(Stamp)) {
                groupHolder.iv_dian.setVisibility(View.VISIBLE);
                groupHolder.tv_count.setVisibility(View.GONE);
            } else {
                groupHolder.iv_dian.setVisibility(View.GONE);
                groupHolder.tv_count.setVisibility(View.VISIBLE);
                if (Stamp.length() > 2) {
                    groupHolder.tv_count.setText("...");
                } else {
                    groupHolder.tv_count.setText(Stamp);
                }
            }
        } else {
            groupHolder.iv_dian.setVisibility(View.GONE);
            groupHolder.tv_count.setVisibility(View.GONE);
        }
        groupHolder.tv_name.setText(list.get(position).getFunName());
        return convertView;
    }

    class GridHolder {
        public TextView tv_name, tv_count;
        public ImageView iv_path, iv_dian;
    }
}
