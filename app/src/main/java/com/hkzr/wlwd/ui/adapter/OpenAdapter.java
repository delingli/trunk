package com.hkzr.wlwd.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by 77295 on 2016/5/3.
 */
@SuppressWarnings("unchecked")
public abstract class OpenAdapter extends BaseAdapter {

    private List models;
    private LayoutInflater mInflater;

    public OpenAdapter(List models) {
        this.models = models;
    }

    public void setModels(List models) {
        this.models = models;
    }

    /**
     * 添加数据集的方法
     *
     * @param models 新加的数据集对象
     */
    public void addModels(List models) {
        if (null != this.models && null != models) {
            this.models.addAll(models);
        }
        notifyDataSetChanged();
    }

    /**
     * 清空数据集，但不马上刷新
     */
    public void clearModels() {
        clearModels(false);
    }

    /**
     * 获取数据集的方法
     *
     * @return 数据集对象
     */
    public List getModels() {
        return models;
    }

    /**
     * 获取对应position下的实体bean
     *
     * @param position 位置
     * @return 实体bean对象
     */
    public Object getPositionModel(int position) {
        if (null != models && position < models.size()) {
            return models.get(position);
        } else {
            return null;
        }
    }

    /**
     * 清空数据集的方法
     *
     * @param refreshAtOnce 是否立即刷新
     */
    public void clearModels(boolean refreshAtOnce) {
        if (null != models) {
            models.clear();
        }
        if (refreshAtOnce) {
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        if (null != models && 0 != models.size()) {
            return models.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != models) {
            return models.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IHolder holder;
        int type = getItemViewType(position);
        if (null == convertView) {
            holder = createHolder(position);
            if (null == mInflater) {
                mInflater = LayoutInflater.from(parent.getContext());
            }
            convertView = holder.createConvertView(mInflater, parent);
            convertView.setTag(holder);
        } else {
            holder = (IHolder) convertView.getTag();
        }
        Object model = models.get(position);
        holder.bindModel(position, model);
        return convertView;
    }

    public abstract IHolder createHolder(int position);
}
