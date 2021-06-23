package com.hkzr.wlwd.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hkzr.wlwd.R;
import com.hkzr.wlwd.model.SignInfoBean;

import java.util.List;

/**
 * 考勤组位置
 */

public class SignSettingLocationAdapter extends BaseAdapter {
    Context context;
    List<SignInfoBean.LocationInfo> list;
    OnDelListener onDelListener;

    public SignSettingLocationAdapter(Context context, List<SignInfoBean.LocationInfo> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_sign_info, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.tv_wifi = (TextView) convertView.findViewById(R.id.tv_wifi);
            viewHolder.iv_del = (ImageView) convertView.findViewById(R.id.iv_del);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SignInfoBean.LocationInfo wifiInfo = list.get(position);
        viewHolder.tv_name.setText(wifiInfo.getLocName());
        viewHolder.tv_content.setText(wifiInfo.getLocAddr());
        viewHolder.tv_wifi.setVisibility(View.GONE);
        viewHolder.iv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDelListener != null) {
                    onDelListener.onDel(position);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_name, tv_content, tv_wifi;
        ImageView iv_del;
    }

    public OnDelListener getOnDelListener() {
        return onDelListener;
    }

    public void setOnDelListener(OnDelListener onDelListener) {
        this.onDelListener = onDelListener;
    }

    public interface OnDelListener {
        public void onDel(int position);
    }
}
