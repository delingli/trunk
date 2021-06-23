package com.hkzr.wlwd.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hkzr.wlwd.R;
import com.hkzr.wlwd.model.LeftGroupBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 左侧分组adapter
 */

public class LeftGrpupAdapter extends BaseAdapter {
    Context context;
    List<LeftGroupBean.GroupsBean> groupsList;
    int mCurrent;

    public LeftGrpupAdapter(Context context, List<LeftGroupBean.GroupsBean> groupsList, int mCurrent) {
        this.context = context;
        this.mCurrent = mCurrent;
        this.groupsList = groupsList;
    }

    public int getmCurrent() {
        return mCurrent;
    }

    public void setmCurrent(int mCurrent) {
        this.mCurrent = mCurrent;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return groupsList.size();
    }

    @Override
    public Object getItem(int position) {
        return groupsList.get(position);
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
            convertView = View.inflate(context, R.layout.item_left_group, null);
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.ll_fat = (LinearLayout) convertView.findViewById(R.id.ll_fat);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.view_current = convertView.findViewById(R.id.view_current);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position == mCurrent) {
            viewHolder.view_current.setVisibility(View.VISIBLE);
            viewHolder.ll_fat.setBackgroundResource(R.color.color_c9ddee);
        } else {
            viewHolder.view_current.setVisibility(View.INVISIBLE);
            viewHolder.ll_fat.setBackgroundResource(R.color.white_ffffff);
        }
        if (!TextUtils.isEmpty(groupsList.get(position).getGroupIcon())) {
            Picasso.with(context).load(groupsList.get(position).getGroupIcon()).error(R.drawable.coffee_blue).into(viewHolder.iv_icon);
        }
        viewHolder.tv_name.setText(groupsList.get(position).getGroupTitle());

        return convertView;
    }

    class ViewHolder {
        LinearLayout ll_fat;
        TextView tv_name;
        ImageView iv_icon;
        View view_current;
    }
}
