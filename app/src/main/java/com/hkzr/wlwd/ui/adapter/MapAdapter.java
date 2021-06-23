package com.hkzr.wlwd.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.hkzr.wlwd.R;

import java.util.List;

/**
 * Created by admin on 2017/6/8.
 */

public class MapAdapter extends BaseAdapter {
    List<PoiInfo> dataList;
    int selectIndex;
    Context context;

    public MapAdapter(List<PoiInfo> dataList, int selectIndex, Context context) {
        this.dataList = dataList;
        this.selectIndex = selectIndex;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
        this.notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_map, null);
            viewHolder = new ViewHolder();
            viewHolder.iv_select = (ImageView) convertView.findViewById(R.id.iv_select);
            viewHolder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position == selectIndex) {
            viewHolder.iv_select.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iv_select.setVisibility(View.GONE);
        }
        viewHolder.tv_name.setText(dataList.get(position).name);
        viewHolder.tv_address.setText(dataList.get(position).address);
        return convertView;
    }

    static class ViewHolder {
        ImageView iv_select;
        TextView tv_name, tv_address;
    }
}
