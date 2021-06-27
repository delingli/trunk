package com.hkzr.wlwd.ui.profilecheck;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hkzr.wlwd.R;

public class RecycleHolder extends RecyclerView.ViewHolder {
    TextView tv_checkProgram, tv_checkItemName, tv_wh, tc_checkMethod;

    public RecycleHolder(View itemView) {
        super(itemView);
        tv_wh = (TextView) itemView.findViewById(R.id.tv_wh);
        tv_checkProgram = (TextView) itemView.findViewById(R.id.tv_checkProgram);
        tv_checkItemName = (TextView) itemView.findViewById(R.id.tv_checkItemName);
        tc_checkMethod = (TextView) itemView.findViewById(R.id.tc_checkMethod);
        tv_wh.setVisibility(View.GONE);
    }
}
