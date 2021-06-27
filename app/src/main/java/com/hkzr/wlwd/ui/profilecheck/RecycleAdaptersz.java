package com.hkzr.wlwd.ui.profilecheck;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hkzr.wlwd.R;

import java.util.ArrayList;
import java.util.List;

public class RecycleAdaptersz extends RecyclerView.Adapter<RecycleHolder> {
    private List<ProfileCheckActivity.CheckItemBean> list;
    private Context context;

    public RecycleAdaptersz(Context context, List<ProfileCheckActivity.CheckItemBean> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecycleHolder onCreateViewHolder(ViewGroup parent, int i) {
     View v=   LayoutInflater.from(context).inflate(R.layout.item_recycle_check,parent,false);
//        View v=  View.inflate(context,R.layout.item_recycle_check,null);
        RecycleHolder myViewHolder = new RecycleHolder(v);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(RecycleHolder recycleHolder, int position) {
        if (list != null && list.size() > 0) {
            ProfileCheckActivity.CheckItemBean itemBean = list.get(position);
            recycleHolder.tc_checkMethod.setText(itemBean.ChkMethod);
            recycleHolder.tv_checkItemName.setText(itemBean.ItemName);
            recycleHolder.tv_checkProgram.setText(itemBean.Stand);
        }

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void addList(List<ProfileCheckActivity.CheckItemBean> ll) {
        if (list == null) {
            list = new ArrayList<>();
        }
        if (ll != null) {
            list.addAll(ll);
        }
        notifyDataSetChanged();
    }
}
