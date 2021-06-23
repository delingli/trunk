package com.hkzr.wlwd.ui.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.widget.BaseAdapter;

public abstract class DbaseAdapter<T> extends BaseAdapter {
	protected List<T> mList;
	protected Context context;
	public DbaseAdapter(List<T> mList,Context context) {
		super();
		this.mList = mList;
		this.context =context;
	}

	@Override
	public int getCount() {
		return mList == null?0:mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	public void jump(Context context, Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(context, cls);
		context.startActivity(intent);
	}
}
