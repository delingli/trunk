package com.hkzr.wlwd.ui.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.hkzr.wlwd.R;

public class FooterListView extends ListView implements OnScrollListener {

	private View footerView;

	private boolean loaded = true;

	private int footerResId = 0;

	private FooterListViewListener listener;

	private Animation animIn;

	private boolean loadEnable = true;

	public boolean isLoadEnable() {
		return loadEnable;
	}

	public void setLoadEnable(boolean loadEnable) {
		this.loadEnable = loadEnable;
	}

	public void setListener(FooterListViewListener listener) {
		this.listener = listener;
	}

	public interface FooterListViewListener {
		void getMoreData();
	}

	public FooterListView(Context context) {
		super(context);
		init(context);
	}

	public FooterListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public FooterListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	@SuppressLint("InflateParams")
	private void init(Context context) {

		setOnScrollListener(this);
		if (footerResId == 0)
			footerView = LayoutInflater.from(getContext()).inflate(
					R.layout.pull_to_load_footer, null);
		else
			footerView = LayoutInflater.from(getContext()).inflate(footerResId,
					null);
		animIn = AnimationUtils.loadAnimation(context, R.anim.popup_enter);
	}

	public void setFooterResId(int footerResId) {
		footerView = LayoutInflater.from(getContext()).inflate(footerResId,
				null);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		int lastItemId = getLastVisiblePosition();
		if ((lastItemId + 1) == totalItemCount) {
			if (totalItemCount > 0 && loaded && loadEnable) {
				if (listener!=null) {
					loaded = false;
					addFooterView(footerView);
					footerView.startAnimation(animIn);
					listener.getMoreData();
				}
			}
		}
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		addFooterView(footerView);
		super.setAdapter(adapter);
		removeFooterView(footerView);
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public void loadComplete() {
		if (getFooterViewsCount() > 0) {
			removeFooterView(footerView);
		}
		setLoaded(true);
	}

    @Override
    public void setOnItemLongClickListener(
    		OnItemLongClickListener listener) {
    
    	super.setOnItemLongClickListener(listener);
    }

}
