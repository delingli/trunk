package com.hkzr.wlwd.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public abstract class BaseV4Fragment extends Fragment {

	public abstract int getViewId();

	public abstract void initWidget(View parent);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mFragment = getView(inflater, container, savedInstanceState);
		initWidget(mFragment);
		return mFragment;
	}

	protected View getView(LayoutInflater inflater, ViewGroup container,
			Bundle bundle) {
		int id = getViewId();
		if (id == 0) {
			return null;
		}
		return inflater.inflate(id, container, false);
	}

	protected void toast(Object o) {
		if (o instanceof Integer) {
			Toast.makeText(getActivity(), (Integer) o, Toast.LENGTH_SHORT)
					.show();
		} else if (o instanceof CharSequence) {
			Toast.makeText(getActivity(), (CharSequence) o, Toast.LENGTH_SHORT)
					.show();
		}
	}
	
	/**
	 * call this in @link {@link #initWidget(View)}
	 * @param parent
	 */
	protected void findView(View parent){
	}

	/**
	 * jump to target activity without bundle data
	 * @param cls
	 */
	public void jump(Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(getActivity(), cls);
		startActivity(intent);
	}
	/**
	 * with data to trans
	 * @param targetClz
	 * @param data
	 */
	public void jump(Class<?> targetClz,Bundle data) {
		startActivity(new Intent(getActivity(), targetClz).putExtras(data));
	}
}
