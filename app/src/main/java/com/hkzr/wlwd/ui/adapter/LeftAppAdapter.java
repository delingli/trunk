package com.hkzr.wlwd.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hkzr.wlwd.R;
import com.hkzr.wlwd.model.LeftGroupBean;

import java.util.List;

/**
 * 左侧分组adapter
 */

public class LeftAppAdapter extends BaseAdapter {
    Context context;
    List<LeftGroupBean.GroupsBean.ListBeanX> list;

    public LeftAppAdapter(Context context, List<LeftGroupBean.GroupsBean.ListBeanX> groupsList) {
        this.context = context;
        this.list = groupsList;
    }

    public List<LeftGroupBean.GroupsBean.ListBeanX> getList() {
        return list;
    }

    public void setList(List<LeftGroupBean.GroupsBean.ListBeanX> list) {
        this.list = list;
        this.notifyDataSetChanged();
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_left_app, null);
            viewHolder.iv_dian = (ImageView) convertView.findViewById(R.id.iv_dian);
            viewHolder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(list.get(position).getFunName());
        String Stamp = list.get(position).getStamp();
        if (!TextUtils.isEmpty(Stamp)) {
            if ("0".equals(Stamp)) {
                viewHolder.iv_dian.setVisibility(View.VISIBLE);
                viewHolder.tv_count.setVisibility(View.GONE);
            } else {
                viewHolder.iv_dian.setVisibility(View.GONE);
                viewHolder.tv_count.setVisibility(View.VISIBLE);
                if (Stamp.length() > 2) {
                    viewHolder.tv_count.setText("...");
                } else {
                    viewHolder.tv_count.setText(Stamp);
                }
            }
        } else {
            viewHolder.iv_dian.setVisibility(View.GONE);
            viewHolder.tv_count.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_name,tv_count;
        ImageView iv_dian;
    }
}
