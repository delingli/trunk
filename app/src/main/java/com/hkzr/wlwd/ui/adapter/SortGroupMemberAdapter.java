package com.hkzr.wlwd.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.hkzr.wlwd.R;
import com.hkzr.wlwd.model.InsidContactEntity;
import com.hkzr.wlwd.ui.widget.XCRoundRectImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SortGroupMemberAdapter extends BaseAdapter implements SectionIndexer {
    private List<InsidContactEntity> list = null;
    private Context mContext;

    public SortGroupMemberAdapter(Context mContext, List<InsidContactEntity> list) {
        this.mContext = mContext;
        this.list = list;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<InsidContactEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final InsidContactEntity mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.activity_group_member_item, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            viewHolder.iv_icon = (XCRoundRectImageView) view.findViewById(R.id.iv_icon);
            viewHolder.tv_code = (TextView) view.findViewById(R.id.tv_code);
            viewHolder.iv_sex = (ImageView) view.findViewById(R.id.iv_sex);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        // 根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getSortLetters());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }
        InsidContactEntity sugarEntity = list.get(position); //每个row一个bean

        viewHolder.tv_code.setText(sugarEntity.getMobilePhone());
        if (!TextUtils.isEmpty(sugarEntity.getPhotoUrl())) {
            Picasso.with(mContext).load(sugarEntity.getPhotoUrl())/*.placeholder(R.drawable.morentouxiang)*/.error(R.drawable.morentouxiang).into(viewHolder.iv_icon);
        }
        if (!TextUtils.isEmpty(sugarEntity.getSex())) {
            if ("男".equalsIgnoreCase(sugarEntity.getSex())) {
                viewHolder.iv_sex.setImageResource(R.drawable.men);
            } else if ("女".equalsIgnoreCase(sugarEntity.getSex())) {
                viewHolder.iv_sex.setImageResource(R.drawable.women);
            }

        }
        viewHolder.tvTitle.setText(this.list.get(position).getUserCn());
        return view;
    }

    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
        XCRoundRectImageView iv_icon;
        TextView tv_code;
        ImageView iv_sex;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}