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


/**
 * 意见反馈
 */
public class FeedBackActivity extends BaseActivity implements View.OnClickListener{


    ImageView ivLeft;
    TextView tvLeft;
    LinearLayout leftLL;
    TextView tvTitle;
    ImageView ivRight;
    TextView tvRight;
    LinearLayout rightLL;
    RelativeLayout rlTitle;
    EditText etFeedback;
    TextView tvTjfk;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_feedback);
        initViewBind();
        initViewDatas();
    }
    private void initViewBind(){
        ivLeft=findViewById(R.id.iv_left);
        tvLeft=findViewById(R.id.tv_left);
        leftLL=findViewById(R.id.left_LL);
        tvTitle=findViewById(R.id.tv_title);
        ivRight=findViewById(R.id.iv_right);
        tvRight=findViewById(R.id.tv_right);
        rightLL=findViewById(R.id.right_LL);
        rlTitle=findViewById(R.id.rl_title);
        etFeedback=findViewById(R.id.et_feedback);
        tvTjfk=findViewById(R.id.tv_tjfk);
        findViewById(R.id.left_LL).setOnClickListener(this);
        findViewById(R.id.tv_tjfk).setOnClickListener(this);
    }

    private void initViewDatas() {
        tvTitle.setText(R.string.mine_yjfk);

    }


    @Override
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
