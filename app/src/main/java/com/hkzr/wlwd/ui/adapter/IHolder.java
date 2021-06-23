package com.hkzr.wlwd.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 77295 on 2016/5/3.
 */
@SuppressWarnings("unchecked")
public interface IHolder<Model> {

    View createConvertView(LayoutInflater inflater, ViewGroup parent);

    void bindModel(int position, Model model);

}
