package com.hkzr.wlwd.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hkzr.wlwd.R;
import com.hkzr.wlwd.ui.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 意见反馈
 */
public class FeedBackActivity extends BaseActivity {


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
    @Bind(R.id.et_feedback)
    EditText etFeedback;
    @Bind(R.id.tv_tjfk)
    TextView tvTjfk;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_feedback);
        initViewDatas();
    }

    private void initViewDatas() {
        tvTitle.setText(R.string.mine_yjfk);

    }

  

    @OnClick({R.id.left_LL, R.id.tv_tjfk})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_LL:
                finish();
                break;
            case R.id.tv_tjfk:
                TextUtils.isEmpty("感谢您的反馈");
                finish();
                break;
        }
    }
}
