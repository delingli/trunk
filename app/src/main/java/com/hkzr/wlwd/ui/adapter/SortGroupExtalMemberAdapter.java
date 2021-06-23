package com.hkzr.wlwd.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.hkzr.wlwd.R;
import com.hkzr.wlwd.model.ExternalContactEntity;
import com.hkzr.wlwd.ui.widget.XCRoundRectImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by admin on 2017/6/26.
 */

public class SortGroupExtalMemberAdapter extends BaseAdapter implements SectionIndexer {
    private List<ExternalContactEntity> list = null;
    private Context mContext;

    public SortGroupExtalMemberAdapter(Context mContext, List<ExternalContactEntity> list) {
        this.mContext = mContext;
        this.list = list;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<ExternalContactEntity> list) {
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
        final ExternalContactEntity mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.activity_group_exter_member_item, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            viewHolder.iv_icon = (XCRoundRectImageView) view.findViewById(R.id.iv_icon);
            viewHolder.tv_code = (TextView) view.findViewById(R.id.tv_code);
            viewHolder.company = (TextView) view.findViewById(R.id.company);
            viewHolder.tv_fuze = (TextView) view.findViewById(R.id.tv_fuze);
            viewHolder.tv_1 = (TextView) view.findViewById(R.id.tv_1);
            viewHolder.tv_2 = (TextView) view.findViewById(R.id.tv_2);
            viewHolder.tv_3 = (TextView) view.findViewById(R.id.tv_3);
            viewHolder.tv_4 = (TextView) view.findViewById(R.id.tv_4);
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
        ExternalContactEntity sugarEntity = list.get(position); //每个row一个bean
        viewHolder.tv_fuze.setText(sugarEntity.getManager());
        if (!TextUtils.isEmpty(sugarEntity.getPhoto())) {
            Picasso.with(mContext).load(sugarEntity.getPhoto())/*.placeholder(R.drawable.morentouxiang)*/.error(R.drawable.morentouxiang).into(viewHolder.iv_icon);
        }
        viewHolder.company.setText(this.list.get(position).getCorpName());
        viewHolder.tvTitle.setText(this.list.get(position).getLinkName());
        if (!TextUtils.isEmpty(sugarEntity.getRemark())) {
            String[] str = sugarEntity.getRemark().split(",");
            for (int i = 0; i < str.length; i++) {
                switch (i) {
                    case 0:
                        viewHolder.tv_1.setText(str[0]);
                        viewHolder.tv_1.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        viewHolder.tv_2.setText(str[1]);
                        viewHolder.tv_2.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        viewHolder.tv_3.setText(str[2]);
                        viewHolder.tv_3.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        viewHolder.tv_4.setText(str[3]);
                        viewHolder.tv_4.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }
        return view;
    }

    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
        XCRoundRectImageView iv_icon;
        TextView tv_code;
        TextView company;
        TextView tv_fuze, tv_1, tv_2, tv_3, tv_4;
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
