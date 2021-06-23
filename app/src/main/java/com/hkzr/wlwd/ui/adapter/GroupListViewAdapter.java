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
import com.hkzr.wlwd.model.ApplicationEntity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by admin on 2017/5/12.
 */

public class GroupListViewAdapter extends BaseAdapter {
    Context context;
    List<ApplicationEntity.GroupsBean.ListBeanXX> list;

    public GroupListViewAdapter(Context context, List<ApplicationEntity.GroupsBean.ListBeanXX> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_group_list_item, null);
            groupHolder = new GridHolder();
            groupHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            groupHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            groupHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            groupHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            groupHolder.iv_dian = (ImageView) convertView.findViewById(R.id.iv_dian);
            groupHolder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GridHolder) convertView.getTag();
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
        if (!TextUtils.isEmpty(list.get(position).getFunIcon())) {
            Picasso.with(context).load(list.get(position).getFunIcon()).error(R.drawable.coffee_blue).into(groupHolder.iv_icon);
        }
        groupHolder.tv_title.setText(list.get(position).getFunName());
        groupHolder.tv_content.setText(list.get(position).getSubTitle());
        groupHolder.tv_time.setText(list.get(position).getToTime());
        return convertView;
    }

    class GridHolder {
        public TextView tv_title, tv_count, tv_content, tv_time;
        public ImageView iv_icon, iv_dian;
    }
}
