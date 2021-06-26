package com.hkzr.wlwd.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hkzr.wlwd.R;
import com.hkzr.wlwd.ui.base.BaseActivity;


/**
 * 新消息设置
 */

public class NewMessageActivity extends BaseActivity {
    ImageView ivLeft;
    TextView tvLeft;
    TextView tvTitle;
    ImageView ivRight;
    TextView tvRight;
    LinearLayout rightLL;
    LinearLayout leftLL;
    RelativeLayout rlTitle;
    CheckBox cb1;
    CheckBox cb2;
    CheckBox cb3;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initViewBind();
        setContentView(R.layout.activity_new_message);
        initView();
    }

    private void initViewBind() {
        ivLeft = findViewById(R.id.iv_left);
        tvLeft = findViewById(R.id.tv_left);
        leftLL = findViewById(R.id.left_LL);
        tvTitle = findViewById(R.id.tv_title);
        ivRight = findViewById(R.id.iv_right);
        tvRight = findViewById(R.id.tv_right);
        rightLL = findViewById(R.id.right_LL);
        rlTitle = findViewById(R.id.rl_title);
        cb1 = findViewById(R.id.cb_1);
        cb2 = findViewById(R.id.cb_2);
        cb3 = findViewById(R.id.cb_3);
        findViewById(R.id.cb_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
    }

    private void initView() {
        tvTitle.setText(R.string.xxxtxsz);

    }


}
