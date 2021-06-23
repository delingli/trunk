package com.hkzr.wlwd.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hkzr.wlwd.R;
import com.hkzr.wlwd.model.ApplicationEntity;
import com.hkzr.wlwd.ui.utils.JumpSelect;
import com.hkzr.wlwd.ui.view.MyGridView;

import java.util.List;

/**
 * 应用列表
 */

public class AppExpandableListAdapter extends BaseExpandableListAdapter {
    Context context;
    List<ApplicationEntity.GroupsBean> list;
    OnChildClickListener onChildClickListener;


    public AppExpandableListAdapter(Context context, List<ApplicationEntity.GroupsBean> list) {
        this.context = context;
        this.list = list;
    }


    /**
     * 获取组的个数
     *
     * @return
     * @see android.widget.ExpandableListAdapter#getGroupCount()
     */
    @Override
    public int getGroupCount() {
        return list.size();
    }

    /**
     * 获取指定组中的子元素个数
     *
     * @param groupPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    /**
     * 获取指定组中的数据
     *
     * @param groupPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getGroup(int)
     */
    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    /**
     * 获取指定组中的指定子元素数据。
     *
     * @param groupPosition
     * @param childPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getChild(int, int)
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getList().get(childPosition);
    }

    /**
     * 获取指定组的ID，这个组ID必须是唯一的
     *
     * @param groupPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getGroupId(int)
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * 获取指定组中的指定子元素ID
     *
     * @param groupPosition
     * @param childPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getChildId(int, int)
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * 组和子元素是否持有稳定的ID,也就是底层数据的改变不会影响到它们。
     *
     * @return
     * @see android.widget.ExpandableListAdapter#hasStableIds()
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 获取显示指定组的视图对象
     *
     * @param groupPosition 组位置
     * @param isExpanded    该组是展开状态还是伸缩状态
     * @param convertView   重用已有的视图对象
     * @param parent        返回的视图对象始终依附于的视图组
     * @return
     * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_expendlist_group, null);
            groupHolder = new GroupHolder();
            groupHolder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_group_name);
            groupHolder.iv_group = (ImageView) convertView.findViewById(R.id.iv_group);
            groupHolder.iv_more = (ImageView) convertView.findViewById(R.id.iv_more);
            groupHolder.view = (View) convertView.findViewById(R.id.view);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        groupHolder.view.setVisibility(groupPosition == 0 ? View.GONE : View.VISIBLE);
        if (!isExpanded) {
            groupHolder.iv_group.setImageResource(R.drawable.down);
        } else {
            groupHolder.iv_group.setImageResource(R.drawable.up);
        }
        if (!TextUtils.isEmpty(list.get(groupPosition).getMoreUrl())) {
            groupHolder.iv_more.setVisibility(View.VISIBLE);
            groupHolder.iv_group.setVisibility(View.GONE);
        } else {
            groupHolder.iv_group.setVisibility(View.VISIBLE);
            groupHolder.iv_more.setVisibility(View.GONE);
        }
        groupHolder.tv_group_name.setText(list.get(groupPosition).getGroupTitle());
        groupHolder.iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpSelect.jump(context, "url", list.get(groupPosition).getMoreUrl());
            }
        });
        return convertView;
    }

    /**
     * 获取一个视图对象，显示指定组中的指定子元素数据。
     *
     * @param groupPosition 组位置
     * @param childPosition 子元素位置
     * @param isLastChild   子元素是否处于组中的最后一个
     * @param convertView   重用已有的视图(View)对象
     * @param parent        返回的视图(View)对象始终依附于的视图组
     * @return
     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.layout_group_view, null);
        }
        MyGridView gridView = (MyGridView) convertView.findViewById(R.id.gridview);
        GroupGridViewAdapter adapter = new GroupGridViewAdapter(context, list.get(groupPosition).getList());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onChildClickListener != null) {
                    onChildClickListener.onChildClick(groupPosition, position);
                }
            }
        });
        gridView.setAdapter(adapter);// Adapter
        return convertView;
    }

    /**
     * 是否选中指定位置上的子元素。
     *
     * @param groupPosition
     * @param childPosition
     * @return
     * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    class GroupHolder {
        public TextView tv_group_name;
        public ImageView iv_group;
        public ImageView iv_more;
        public View view;
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
}
