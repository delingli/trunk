package com.hkzr.wlwd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.model.ContactEntity;
import com.hkzr.wlwd.ui.adapter.IHolder;
import com.hkzr.wlwd.ui.adapter.OpenAdapter;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.utils.KeyBoardUtils;
import com.hkzr.wlwd.ui.utils.LogUtils;
import com.hkzr.wlwd.ui.widget.XCRoundRectImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 搜索通讯录
 */

public class SearchContactsActivity extends BaseActivity {
    EditText edSearch;
    ListView lv;
    List<ContactEntity.LinklistBean> beanList;
    OpenAdapter mMyAdapter;
    TextView tvTitle;
    TextView tv_no;

    private void initViewBind() {
        edSearch = findViewById(R.id.ed_search);
        lv = findViewById(R.id.lv);
        tvTitle = findViewById(R.id.tv_title);
        tv_no = findViewById(R.id.tv_no);
        findViewById(R.id.left_LL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_search_contact);
        initViewBind();
        tvTitle.setText("搜索");
        KeyBoardUtils.KeyBoard(this, "open");
        initListener();
    }

    private void initListener() {
        beanList = new ArrayList<>();
        mMyAdapter = new OpenAdapter(beanList) {
            @Override
            public IHolder createHolder(int position) {
                return new ContactHolder();
            }
        };
        lv.setAdapter(mMyAdapter);
        edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String s = v.getText().toString();
                    if (!TextUtils.isEmpty(s)) {
                        initData(s);
                        KeyBoardUtils.HideKeyboard(v);
                    }
                    // 当按了搜索之后关闭软键盘
                    return true;
                }

                return false;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchContactsActivity.this, FriendsInfoActivity.class);
                intent.putExtra("userid", beanList.get(position).getUserId());
                startActivity(intent);
            }
        });
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    lv.setVisibility(View.GONE);
                    tv_no.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initData(String s) {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "userlist");
        mParams.put("query", s);
        VolleyFactory.instance().post(this, mParams, ContactEntity.class, new VolleyFactory.BaseRequest<ContactEntity>() {
            @Override
            public void requestSucceed(String object) {
                LogUtils.e(object.toString());
                lv.setVisibility(View.VISIBLE);
                tv_no.setVisibility(View.GONE);
                beanList = JSON.parseArray(object.toString(), ContactEntity.LinklistBean.class);
                if (beanList == null || beanList.size() == 0) {
                    beanList = new ArrayList<>();
                    lv.setVisibility(View.GONE);
                    tv_no.setVisibility(View.VISIBLE);
                }
                mMyAdapter.setModels(beanList);
                mMyAdapter.notifyDataSetChanged();
            }

            @Override
            public void requestFailed() {
//                ToastUtil.t("接口请求失败");
            }
        }, true, false);
    }


    class ContactHolder implements IHolder<ContactEntity.LinklistBean> {
        TextView tv_name, tv_code;
        ImageView iv_sex;
        XCRoundRectImageView iv_icon;

        @Override
        public View createConvertView(LayoutInflater inflater, ViewGroup parent) {
            View convertview = inflater.inflate(R.layout.item_friends, parent, false);
            tv_name = (TextView) convertview.findViewById(R.id.tv_name);
            tv_code = (TextView) convertview.findViewById(R.id.tv_code);
            iv_icon = (XCRoundRectImageView) convertview.findViewById(R.id.iv_icon);
            iv_sex = (ImageView) convertview.findViewById(R.id.iv_sex);
            return convertview;
        }

        @Override
        public void bindModel(int position, final ContactEntity.LinklistBean bean) {
            tv_name.setText(bean.getUserCn());
            tv_code.setText(bean.getMobilePhone());
            if (!TextUtils.isEmpty(bean.getPhotoUrl())) {
                Picasso.with(SearchContactsActivity.this).load(bean.getPhotoUrl()).error(R.drawable.morentouxiang).into(iv_icon);
            }
            if (!TextUtils.isEmpty(bean.getSex())) {
                iv_sex.setVisibility(View.VISIBLE);
                if ("男".equalsIgnoreCase(bean.getSex())) {
                    iv_sex.setImageDrawable(getResources().getDrawable(R.drawable.men));
                } else if ("女".equalsIgnoreCase(bean.getSex())) {
                    iv_sex.setImageDrawable(getResources().getDrawable(R.drawable.women));
                }
            } else {
                iv_sex.setVisibility(View.GONE);
            }
        }
    }

}
