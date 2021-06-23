package com.hkzr.wlwd.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hkzr.wlwd.R;
import com.hkzr.wlwd.ui.view.MyListView;

import java.util.List;

/**
 * 日程
 */

public class CalendarListAdapter extends RecyclerView.Adapter<CalendarListAdapter.MyViewHolder> {
    Context context;
    List<List<String>> list;
    OnChildClickListener onChildClickListener;


    public CalendarListAdapter(Context context, List<List<String>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.layout_calendar_expendlist_group, parent,
                false));
        return holder;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv_name.setText(list.get(position).get(0));
        holder.iv_group.setImageResource(R.drawable.up);
        holder.ll_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.lv.getVisibility() == View.VISIBLE) {
                    holder.lv.setVisibility(View.GONE);
                    holder.iv_group.setImageResource(R.drawable.down);
                } else {
                    holder.lv.setVisibility(View.VISIBLE);
                    holder.iv_group.setImageResource(R.drawable.up);
                }
                CalendarListAdapter.this.notifyDataSetChanged();
            }
        });
        MyAdapter myAdapter = new MyAdapter(list.get(position), context);
        holder.lv.setAdapter(myAdapter);
        holder.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int childposition, long id) {
                if (onChildClickListener != null) {
                    onChildClickListener.onChildClick(position, childposition);
                }
            }
        });
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public ImageView iv_group;
        public MyListView lv;
        public LinearLayout ll_top;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            iv_group = (ImageView) itemView.findViewById(R.id.iv_group);
            lv = (MyListView) itemView.findViewById(R.id.lv);
            ll_top = (LinearLayout) itemView.findViewById(R.id.ll_top);
        }
    }

    class ChildHolder {
        public TextView tv_name;
        public CheckBox cb;
        public TextView tv_time;
    }

    public interface OnChildClickListener {
        /**
         * @param groupPosition 组position
         * @param childPosition gridview的postion
         */
        public void onChildClick(int groupPosition, int childPosition);
    }

    public OnChildClickListener getOnChildClickListener() {
        return onChildClickListener;
    }

    public void setOnChildClickListener(OnChildClickListener onChildClickListener) {
        this.onChildClickListener = onChildClickListener;
    }


    class MyAdapter extends BaseAdapter {
        List<String> list;
        Context context;

        public MyAdapter(List<String> list, Context context) {
            this.list = list;
            this.context = context;
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
            ChildHolder childHolder = null;
            if (convertView == null) {
                childHolder = new ChildHolder();
                convertView = View.inflate(context, R.layout.layout_calendar_view, null);
                childHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                childHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                childHolder.cb = (CheckBox) convertView.findViewById(R.id.cb);
                convertView.setTag(childHolder);
            } else {
                childHolder = (ChildHolder) convertView.getTag();
            }
            childHolder.tv_name.setText(list.get(position));
            childHolder.tv_time.setText(list.get(position));
            return convertView;
        }
    }
}
