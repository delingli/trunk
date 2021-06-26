package com.hkzr.wlwd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.model.ContactEntity;
import com.hkzr.wlwd.model.InsidContactEntity;
import com.hkzr.wlwd.ui.adapter.SortGroupMemberAdapter;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.utils.CharacterParser;
import com.hkzr.wlwd.ui.utils.LogUtils;
import com.hkzr.wlwd.ui.utils.PinyinComparator;
import com.hkzr.wlwd.ui.utils.ToastUtil;
import com.hkzr.wlwd.ui.view.ClearEditText;
import com.hkzr.wlwd.ui.widget.SideBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by born on 2017/5/25.
 * 内部联系人通讯录
 */
public class InsidContactActivity extends BaseActivity implements SectionIndexer ,View.OnClickListener{
    TextView tv_title;
    ClearEditText mClearEditText;
    ListView sortListView;
    List<InsidContactEntity> list;
    TextView tvNofriends;
    LinearLayout titleLayout;
    TextView title;
    TextView dialog;
    SideBar sidrbar;
    /**
     * 上次第一个可见元素，用于滚动时记录标识。
     */
    private int lastFirstVisibleItem = -1;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<InsidContactEntity> SourceDateList;
    private SortGroupMemberAdapter adapter;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
private void initViewBind(){
    tv_title=findViewById(R.id.tv_title);
    mClearEditText=findViewById(R.id.filter_edit);
    sortListView=findViewById(R.id.country_lvcountry);
    tvNofriends=findViewById(R.id.title_layout_no_friends);
    titleLayout=findViewById(R.id.title_layout);
    title=findViewById(R.id.title_layout_catalog);
    dialog=findViewById(R.id.dialog);
    sidrbar=findViewById(R.id.sidrbar);
    findViewById(R.id.left_LL).setOnClickListener(this);
    findViewById(R.id.tv_search).setOnClickListener(this);
}
    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.layout_insidecontact);
        initViewBind();
        tv_title.setText("内部联系人");
        list = new ArrayList<>();
        initListener();
        initdatas("");
    }

    private void initListener() {
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        sidrbar.setTextView(dialog);

        // 设置右侧触摸监听
        sidrbar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });

        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 这里要利用adapter.getItem(position)来获取当前position所对应的对象
                InsidContactEntity insidContactEntity = (InsidContactEntity) adapter.getItem(position);
                Intent intent = new Intent(InsidContactActivity.this, FriendsInfoActivity.class);
                intent.putExtra("userid", insidContactEntity.getUserId());
                startActivity(intent);
            }
        });
        mClearEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(InsidContactActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    search();
                }
                return false;
            }
        });
    }

    private void search() {
        String text = mClearEditText.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            toast("请输入姓名或手机号");
        } else if (TextUtils.isDigitsOnly(text) && text.length() < 4) {
            toast("请最少输入4位数字");
        } else {
            initdatas(text);
        }
    }

    private void initdatas(String query) {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "userlist");
        mParams.put("orgid", "");
        mParams.put("query", query);
        LogUtils.e(mParams.toString());
        VolleyFactory.instance().post(InsidContactActivity.this, mParams, ContactEntity.class, new VolleyFactory.BaseRequest<ContactEntity>() {
            @Override
            public void requestSucceed(String object) {
//                DataSupport.deleteAll(ContactDB.class);
                LogUtils.e(object.toString());
                SourceDateList = JSON.parseArray(object.toString(), InsidContactEntity.class);
                if (SourceDateList != null && SourceDateList.size() > 0) {
                    SourceDateList = filledData(SourceDateList);
                    // 根据a-z进行排序源数据
                    Collections.sort(SourceDateList, pinyinComparator);
                    adapter = new SortGroupMemberAdapter(InsidContactActivity.this, SourceDateList);
                    sortListView.setAdapter(adapter);
                    sortListView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                                    title.setText(SourceDateList.get(
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
                                title.setText(SourceDateList.get(
                                        getPositionForSection(section)).getSortLetters());
                            }
                        }
                    });
                } else {
                    sortListView.setOnScrollListener(null);
                    SourceDateList = new ArrayList<InsidContactEntity>();
                    adapter = new SortGroupMemberAdapter(InsidContactActivity.this, SourceDateList);
                    sortListView.setAdapter(adapter);
                    title.setText("无匹配数据");
                }
//                // 根据输入框输入值的改变来过滤搜索
//                mClearEditText.addTextChangedListener(new TextWatcher() {
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before,
//                                              int count) {
//                        // 这个时候不需要挤压效果 就把他隐藏掉
//                        titleLayout.setVisibility(View.GONE);
//                        // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
//                        filterData(s.toString());
//                    }
//
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count,
//                                                  int after) {
//
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                    }
//                });

            }

            @Override
            public void requestFailed() {
                ToastUtil.t("接口请求失败");
            }
        }, true, false);
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return SourceDateList.get(position).getSortLetters().charAt(0);
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < SourceDateList.size(); i++) {
            String sortStr = SourceDateList.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<InsidContactEntity> filterDateList = new ArrayList<InsidContactEntity>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
            tvNofriends.setVisibility(View.GONE);
        } else {
            filterDateList.clear();
            for (InsidContactEntity sortModel : SourceDateList) {
                String name = sortModel.getUserCn();
                String mobile = sortModel.getMobilePhone();
                if (name.indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(name).startsWith(
                        filterStr.toString()) || (!TextUtils.isEmpty(mobile) && mobile.indexOf(filterStr.toString()) != -1)) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
        if (filterDateList.size() == 0) {
            tvNofriends.setVisibility(View.VISIBLE);
        } else {
            tvNofriends.setVisibility(View.GONE);
        }
    }


    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<InsidContactEntity> filledData(List<InsidContactEntity> date) {
        for (int i = 0; i < date.size(); i++) {
            InsidContactEntity sortModel = date.get(i);
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(sortModel.getUserCn());
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_LL:
                this.finish();
                break;
            case R.id.tv_search:
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(InsidContactActivity.this.getCurrentFocus()
                                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                search();
                break;
        }
    }

    private Myadapter myadapter;

    private class Myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder tag;
            if (view == null) {
                view = View.inflate(InsidContactActivity.this, R.layout.item_friends, null);
                tag = new ViewHolder(view);
                view.setTag(tag);
            } else {
                tag = (ViewHolder) view.getTag();
            }
            InsidContactEntity sugarEntity = list.get(i); //每个row一个bean
            tag.tv_name.setText(sugarEntity.getUserCn());
            tag.tv_code.setText(sugarEntity.getMobilePhone());
            if (TextUtils.isEmpty(sugarEntity.getPhotoUrl())) {
                Picasso.with(InsidContactActivity.this).load(sugarEntity.getPhotoUrl()).into(tag.iv_icon);
            }
            if (TextUtils.isEmpty(sugarEntity.getSex())) {
                if ("男".equalsIgnoreCase(sugarEntity.getSex())) {
                    tag.iv_sex.setImageDrawable(getResources().getDrawable(R.drawable.men));
                } else if ("女".equalsIgnoreCase(sugarEntity.getSex())) {
                    tag.iv_sex.setImageDrawable(getResources().getDrawable(R.drawable.women));
                }

            }
            return view;
        }
    }

    class ViewHolder {
        TextView tv_name;
        TextView tv_code;
        ImageView iv_icon;
        ImageView iv_sex;

        public ViewHolder(View view) {
            tv_name=   view.findViewById(R.id.tv_name);
            tv_code=   view.findViewById(R.id.tv_code);
            iv_icon=   view.findViewById(R.id.iv_icon);
            iv_sex=   view.findViewById(R.id.iv_sex);
        }
    }
}
