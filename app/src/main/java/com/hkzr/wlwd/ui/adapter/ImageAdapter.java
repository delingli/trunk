package com.hkzr.wlwd.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hkzr.wlwd.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/6/28.
 */

public class ImageAdapter extends BaseAdapter {

    List<Map<String, String>> list;
    Context context;
    OnDelListener onDelListener;

    public ImageAdapter(List<Map<String, String>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size() > 3 ? 3 : list.size();
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
        convertView = View.inflate(context, R.layout.item_image, null);
        ImageView iv = (ImageView) convertView.findViewById(R.id.iv);
        ImageView iv_del = (ImageView) convertView.findViewById(R.id.iv_del);
        Map<String, String> map = list.get(position);
        if (map.containsKey("bitmap")) {
            Picasso.with(context).load(new File(map.get("bitmap"))).resize(200, 200).into(iv);
         iv_del.setVisibility(View.VISIBLE);
        } else {
            iv.setImageResource(R.drawable.xiangji);
            iv_del.setVisibility(View.GONE);
        }
        iv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDelListener != null) {
                    onDelListener.onDel(position);
                }
            }
        });

        return convertView;
    }


    public interface OnDelListener {
        public void onDel(int position);
    }

    public OnDelListener getOnDelListener() {
        return onDelListener;
    }

    public void setOnDelListener(OnDelListener onDelListener) {
        this.onDelListener = onDelListener;
    }
}
