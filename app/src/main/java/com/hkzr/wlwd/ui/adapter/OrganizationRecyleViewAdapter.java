package com.hkzr.wlwd.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hkzr.wlwd.R;

import java.util.List;

/**
 * Created by admin on 2017/5/25.
 */

public class OrganizationRecyleViewAdapter extends RecyclerView.Adapter<OrganizationRecyleViewAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    MyItemClickListener myItemClickListener;
    private List<String> title;
    private Context context;

    public OrganizationRecyleViewAdapter(List<String> title, Context context) {
        this.title = title;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    //填充onCreateViewHolder方法返回的holder中的控件
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (position == 0) {
            holder.iv_next.setVisibility(View.GONE);
        } else {
            holder.iv_next.setVisibility(View.VISIBLE);
        }
        holder.tv_group.setText(title.get(position));
        if (myItemClickListener != null) {
            holder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myItemClickListener.onItemClick(v, position);
                }
            });
        }
    }

    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_organ_title, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_group;
        ImageView iv_next;
        View rootView;

        public MyViewHolder(View view) {
            super(view);
            rootView = view;
            tv_group = (TextView) view.findViewById(R.id.tv_group);
            iv_next = (ImageView) view.findViewById(R.id.iv_next);
        }

    }

    public MyItemClickListener getMyItemClickListener() {
        return myItemClickListener;
    }

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public interface MyItemClickListener {
        public void onItemClick(View view, int postion);
    }
}
