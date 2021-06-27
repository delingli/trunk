package com.hkzr.wlwd.ui.productlist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hkzr.wlwd.R;

public class ProductListHolder extends RecyclerView.ViewHolder {
    TextView tv_checkProgram, tv_checkItemName, tc_checkMethod;

    public ProductListHolder(View itemView) {
        super(itemView);
        tv_checkProgram = (TextView) itemView.findViewById(R.id.tv_checkProgram);
        tv_checkItemName = (TextView) itemView.findViewById(R.id.tv_checkItemName);
        tc_checkMethod = (TextView) itemView.findViewById(R.id.tc_checkMethod);
    }
}
