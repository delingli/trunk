package com.hkzr.wlwd.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hkzr.wlwd.R;
import com.hkzr.wlwd.model.CalendarDayEventBean;

import java.util.List;

/**
 * 应用列表
 */

public class CalendarExpandableListAdapter extends BaseExpandableListAdapter {
    Context context;
    List<CalendarDayEventBean.GroupsBean> list;
    OnChildClickListener onChildClickListener;


    public CalendarExpandableListAdapter(Context context, List<CalendarDayEventBean.GroupsBean> list) {
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
        return list.get(groupPosition).getList().size();
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
     * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean, View,
     * ViewGroup)
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_calendar_expendlist_group, null);
            groupHolder = new GroupHolder();
            groupHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            groupHolder.view = convertView.findViewById(R.id.view);
            groupHolder.iv_group = (ImageView) convertView.findViewById(R.id.iv_group);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
//        convertView.setBackgroundColor(Color.parseColor(list.get(groupPosition).));
        if (!isExpanded) {
            groupHolder.iv_group.setImageResource(R.drawable.down);
        } else {
            groupHolder.iv_group.setImageResource(R.drawable.up);
        }
        groupHolder.tv_name.setText(list.get(groupPosition).getGroupTitle());
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
     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, View,
     * ViewGroup)
     */
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_calendar_view, null);
            childHolder = new ChildHolder();
            childHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            childHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            childHolder.cb = (ImageView) convertView.findViewById(R.id.cb);
            childHolder.ll_cb = (LinearLayout) convertView.findViewById(R.id.ll_cb);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        final CalendarDayEventBean.GroupsBean.ListBean listBeen = list.get(groupPosition).getList().get(childPosition);
        childHolder.tv_name.setText(listBeen.getSubject());
        childHolder.tv_time.setText(listBeen.getSubTitle());
        String checked = listBeen.getChecked();
        if (TextUtils.isEmpty(checked)) {
            childHolder.cb.setVisibility(View.INVISIBLE);
        } else if ("0".equals(checked)) {
            childHolder.cb.setVisibility(View.VISIBLE);
            childHolder.cb.setSelected(false);
        } else if ("1".equals(checked)) {
            childHolder.cb.setVisibility(View.VISIBLE);
            childHolder.cb.setSelected(true);
        }
        childHolder.ll_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onChildClickListener != null) {
                    onChildClickListener.onCheckChildClick(groupPosition, childPosition);
                }
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onChildClickListener != null) {
                    onChildClickListener.onChildClick(groupPosition, childPosition);
                }
            }
        });
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

    class ChildHolder {
        public TextView tv_name;
        public ImageView cb;
        public TextView tv_time;
        public LinearLayout ll_cb;
    }


    class GroupHolder {
        public TextView tv_name;
        public ImageView iv_group;
        public View view;
    }

    public interface OnChildClickListener {
        /**
         * @param groupPosition 组position
         * @param childPosition gridview的postion
         */
        public void onChildClick(int groupPosition, int childPosition);

        public void onCheckChildClick(int groupPosition, int childPosition);

    }

    public OnChildClickListener getOnChildClickListener() {
        return onChildClickListener;
    }

    public void setOnChildClickListener(OnChildClickListener onChildClickListener) {
        this.onChildClickListener = onChildClickListener;
    }
}
