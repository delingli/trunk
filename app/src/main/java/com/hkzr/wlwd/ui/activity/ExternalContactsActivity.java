package com.hkzr.wlwd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.SDK_WebView;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.model.ExternalContactEntity;
import com.hkzr.wlwd.ui.adapter.SortGroupExtalMemberAdapter;
import com.hkzr.wlwd.ui.app.App;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.utils.CharacterParser;
import com.hkzr.wlwd.ui.utils.LogUtils;
import com.hkzr.wlwd.ui.utils.PinyinExterComparator;
import com.hkzr.wlwd.ui.utils.ToastUtil;
import com.hkzr.wlwd.ui.view.ClearEditText;
import com.hkzr.wlwd.ui.widget.MoreExterPopWindow;
import com.hkzr.wlwd.ui.widget.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by admin on 2017/6/24.
 * 外部联系人
 */
public class ExternalContactsActivity extends BaseActivity implements MoreExterPopWindow.Go {

    ImageView ivLeft;
    TextView tvLeft;
    LinearLayout leftLL;
    TextView tvTitle;
    TextView tvRight;
    LinearLayout rightLL;
    RelativeLayout rlTitle;
    ClearEditText filterEdit;
    ListView countryLvcountry;
    TextView titleLayoutNoFriends;
    TextView titleLayoutCatalog;
    LinearLayout titleLayout;
    ImageView iv_right;
    TextView dialog;
    SideBar sidrbar;
    private SortGroupExtalMemberAdapter adapter;
    /**
     * 上次第一个可见元素，用于滚动时记录标识。
     */
    private int lastFirstVisibleItem = -1;
    private int type = -1;

    private void initViewBind() {
        findViewById(R.id.iv_left);
        findViewById(R.id.tv_left);
        findViewById(R.id.left_LL);
        findViewById(R.id.tv_title);
        findViewById(R.id.tv_right);
        findViewById(R.id.right_LL);
        findViewById(R.id.rl_title);
        findViewById(R.id.filter_edit);
        findViewById(R.id.country_lvcountry);
        findViewById(R.id.title_layout_no_friends);
        findViewById(R.id.title_layout_catalog);
        findViewById(R.id.title_layout);
        findViewById(R.id.iv_right);
        findViewById(R.id.dialog);
        findViewById(R.id.sidrbar);
        findViewById(R.id.left_LL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExternalContactsActivity.this.finish();
            }
        });
        findViewById(R.id.iv_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MoreExterPopWindow morePopWindow = new MoreExterPopWindow(ExternalContactsActivity.this);
                morePopWindow.setmGo(ExternalContactsActivity.this);
                morePopWindow.showPopupWindow(iv_right);
            }
        });


    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.layout_external);
        initViewBind();
        tvTitle.setText("外部联系人");
        type = getIntent().getIntExtra("type", 0);
        if (type == 0) {
            iv_right.setImageDrawable(getResources().getDrawable(R.drawable.add_to));
        }
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initdatas();
    }

    private List<ExternalContactEntity> externalContactEntities = new ArrayList<>();

    private void initdatas() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "outlinklist");
        LogUtils.e(mParams.toString());
        VolleyFactory.instance().post(ExternalContactsActivity.this, mParams, ExternalContactEntity.class, new VolleyFactory.BaseRequest<ExternalContactEntity>() {
            @Override
            public void requestSucceed(String object) {
                LogUtils.e(object.toString());
                externalContactEntities = JSON.parseArray(object.toString(), ExternalContactEntity.class);
                if (externalContactEntities != null && externalContactEntities.size() > 0) {
                    externalContactEntities = filledData(externalContactEntities);
                    // 根据a-z进行排序源数据
                    Collections.sort(externalContactEntities, pinyinComparator);
                    adapter = new SortGroupExtalMemberAdapter(ExternalContactsActivity.this, externalContactEntities);
                    countryLvcountry.setAdapter(adapter);
                    countryLvcountry.setOnScrollListener(new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {

                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem,
                                             int visibleItemCount, int totalItemCount) {
                            int section = getSectionForPosition(firstVisibleItem);
                            if (firstVisibleItem > 0) {
                                int nextSection = getSectionForPosition(firstVisibleItem + 1);
                                int nextSecPosition = getPositionForSection(+nextSection);
                                if (firstVisibleItem != lastFirstVisibleItem) {
                                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout
                                            .getLayoutParams();
                                    params.topMargin = 0;
                                    titleLayout.setLayoutParams(params);
                                    titleLayoutCatalog.setText(externalContactEntities.get(
                                            getPositionForSection(section)).getSortLetters());
                                }
                                if (nextSecPosition == firstVisibleItem + 1) {
                                    View childView = view.getChildAt(0);
                                    if (childView != null) {
                                        int titleHeight = titleLayout.getHeight();
                                        int bottom = childView.getBottom();
                                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout
                                                .getLayoutParams();
                                        if (bottom < titleHeight) {
                                            float pushedDistance = bottom - titleHeight;
                                            params.topMargin = (int) pushedDistance;
                                            titleLayout.setLayoutParams(params);
                                        } else {
                                            if (params.topMargin != 0) {
                                                params.topMargin = 0;
                                                titleLayout.setLayoutParams(params);
                                            }
                                        }
                                    }
                                }
                                lastFirstVisibleItem = firstVisibleItem;
                            } else {
                                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout
                                        .getLayoutParams();
                                params.topMargin = 0;
                                titleLayout.setLayoutParams(params);
                                titleLayoutCatalog.setText(externalContactEntities.get(
                                        getPositionForSection(section)).getSortLetters());
                            }
                        }
                    });
                    filterEdit = (ClearEditText) findViewById(R.id.filter_edit);

                    // 根据输入框输入值的改变来过滤搜索
                    filterEdit.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before,
                                                  int count) {
                            // 这个时候不需要挤压效果 就把他隐藏掉
                            titleLayout.setVisibility(View.GONE);
                            // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                            filterData(s.toString());
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                }
            }

            @Override
            public void requestFailed() {
                ToastUtil.t("接口请求失败");
            }
        }, false, false);
    }

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinExterComparator pinyinComparator;

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return externalContactEntities.get(position).getSortLetters().charAt(0);
    }

    /* 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<ExternalContactEntity> filterDateList = new ArrayList<ExternalContactEntity>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = externalContactEntities;
            titleLayoutNoFriends.setVisibility(View.GONE);
        } else {
            filterDateList.clear();
            for (ExternalContactEntity sortModel : externalContactEntities) {
                String name = sortModel.getLinkName();
                String corpName = sortModel.getCorpName();
                if (name.indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(name).startsWith(
                        filterStr.toString()) || corpName.indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(corpName).startsWith(
                        filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
        if (filterDateList.size() == 0) {
            titleLayoutNoFriends.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < externalContactEntities.size(); i++) {
            String sortStr = externalContactEntities.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    private void initListener() {
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinExterComparator();
        sidrbar.setTextView(dialog);

        // 设置右侧触摸监听
        sidrbar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                if (adapter != null && adapter.getCount() > 0) {
                    // 该字母首次出现的位置
                    int position = adapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {
                        countryLvcountry.setSelection(position);
                    }
                }
            }
        });

        countryLvcountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 这里要利用adapter.getItem(position)来获取当前position所对应的对象
//                Intent intent = new Intent(ExternalContactsActivity.this, FriendsInfoActivity.class);
//                intent.putExtra("userid", externalContactEntities.get(position).getUserId());
//                startActivity(intent);
                ExternalContactEntity externalContactEntity = (ExternalContactEntity) adapter.getItem(position);
                if (type == 0) {
                    String url = App.getInstance().getH5Url() + "/Workasp/Contact/ContactInfo.aspx?" + "linkid=" + externalContactEntity.getLinkID() +
                            "&tokenId=" + mUserInfoCache.getTokenid();
                    Intent intent = new Intent(ExternalContactsActivity.this, SDK_WebView.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("LinkID", externalContactEntity.getLinkID());
                    intent.putExtra("LinkName", externalContactEntity.getLinkName());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<ExternalContactEntity> filledData(List<ExternalContactEntity> date) {
        for (int i = 0; i < date.size(); i++) {
            ExternalContactEntity sortModel = date.get(i);
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(sortModel.getLinkName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }
        }
        return date;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }




    @Override
    public void add() {
        String url = App.getInstance().getH5Url() + "/Workasp/Contact/NewContact.aspx?tokenId=" + mUserInfoCache.getTokenid();
        Intent intent = new Intent(ExternalContactsActivity.this, SDK_WebView.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    @Override
    public void search() {
        String url = App.getInstance().getH5Url() + "/Workasp/Contact/ContactList.aspx?tokenId=" + mUserInfoCache.getTokenid();
        Intent intent = new Intent(ExternalContactsActivity.this, SDK_WebView.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}
