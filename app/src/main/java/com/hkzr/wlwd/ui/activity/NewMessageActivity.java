package com.hkzr.wlwd.ui.activity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hkzr.wlwd.R;
import com.hkzr.wlwd.ui.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 新消息设置
 */

public class NewMessageActivity extends BaseActivity {
    @Bind(R.id.iv_left)
    ImageView ivLeft;
    @Bind(R.id.tv_left)
    TextView tvLeft;
    @Bind(R.id.left_LL)
    LinearLayout leftLL;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.right_LL)
    LinearLayout rightLL;
    @Bind(R.id.rl_title)
    RelativeLayout rlTitle;
    @Bind(R.id.cb_1)
    CheckBox cb1;
    @Bind(R.id.cb_2)
    CheckBox cb2;
    @Bind(R.id.cb_3)
    CheckBox cb3;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        initView();
    }

    private void initView() {
        tvTitle.setText(R.string.xxxtxsz);

    }


  

    @OnClick(R.id.left_LL)
    public void onClick() {
        finish();
    }
}
