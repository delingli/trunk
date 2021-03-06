package com.hkzr.wlwd.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.model.MineList;
import com.hkzr.wlwd.model.ServiceEntity;
import com.hkzr.wlwd.ui.activity.LoginActivity;
import com.hkzr.wlwd.ui.activity.MineInfoActivity;
import com.hkzr.wlwd.ui.activity.SettingActivity;
import com.hkzr.wlwd.ui.adapter.IHolder;
import com.hkzr.wlwd.ui.adapter.OpenAdapter;
import com.hkzr.wlwd.ui.app.User;
import com.hkzr.wlwd.ui.base.BaseFragment;
import com.hkzr.wlwd.ui.utils.AppManager;
import com.hkzr.wlwd.ui.utils.DialogUtil;
import com.hkzr.wlwd.ui.utils.JumpSelect;
import com.hkzr.wlwd.ui.utils.LogUtils;
import com.hkzr.wlwd.ui.utils.SPUtil;
import com.hkzr.wlwd.ui.utils.ToastUtil;
import com.hkzr.wlwd.ui.view.MyListView;
import com.hkzr.wlwd.ui.widget.XCRoundRectImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * mine page
 *
 * @author zl
 */
public class PersonalFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @Bind(R.id.rl_head)
    RelativeLayout rlhead;
    @Bind(R.id.iv_head)
    XCRoundRectImageView ivHead;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.lv_mine)
    MyListView lv_mine;
    @Bind(R.id.tv_xtsz)
    TextView tvXtsz;
    @Bind(R.id.tv_tcdl)
    TextView tvTcdl;
    OpenAdapter mMyAdapter;
    List<MineList> list;

    @Override
    public int getViewId() {
        return R.layout.layout_mine;
    }

    @Override
    public void initWidget(View parent) {
        findView(parent);
        list = new ArrayList<>();
        mMyAdapter = new OpenAdapter(list) {
            @Override
            public IHolder createHolder(int position) {
                return new MineHolder();
            }
        };
        lv_mine.setAdapter(mMyAdapter);
        lv_mine.setOnItemClickListener(this);
        initDatas();
    }

    @Override
    public void onResume() {
        super.onResume();
        initViewDatas();


    }

    private void initViewDatas() {
        if (mUserInfoCache.getUser() != null) {
            tvName.setText(mUserInfoCache.getUser().getUserCn());
            tvPhone.setText(mUserInfoCache.getUser().getMobilePhone());
            Picasso.with(getActivity()).invalidate(mUserInfoCache.getUser().getPhotoUrl());
            Picasso.with(getActivity()).load(mUserInfoCache.getUser().getPhotoUrl())/*.placeholder(R.drawable.morentouxiang)*/.error(R.drawable.morentouxiang).into(ivHead);
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.rl_head,/* R.id.tv_xgmm, R.id.tv_xtbz, R.id.tv_gywm, R.id.tv_yjfk,*/ R.id.tv_xtsz, R.id.tv_tcdl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_head:
                startActivityForResult(new Intent(getActivity(), MineInfoActivity.class), 233);
                break;
//            case R.id.tv_pyq:
//                jump(FriendCircleActivity.class);
//                
//                break;
//            case R.id.tv_wdrc:
//                bundle = new Bundle();
//                bundle.putString(Constant.Type, Constant.DAYARRAY);
//                jump(SDK_WebView.class, bundle);
//                break;
//            case R.id.tv_xgmm:
//                jump(UpdataPwdActivity.class);
//                break;
//            case R.id.tv_xtbz:
//                intent.putExtra("title", "????????????");
//                intent.putExtra("url", App.getInstance().getH5Url() + ReqUrl.SysHelp);
//                startActivity(intent);
//                break;
//            case R.id.tv_gywm:
//                intent.putExtra("title", "????????????");
//                intent.putExtra("url", App.getInstance().getH5Url() + ReqUrl.AboutUs);
//                startActivity(intent);
//                break;
//            case R.id.tv_yjfk:
//                intent.putExtra("title", "????????????");
//                intent.putExtra("url", App.getInstance().getH5Url() + ReqUrl.Advice);
//                startActivity(intent);
//                break;
            case R.id.tv_xtsz:
                jump(SettingActivity.class);
                break;
            case R.id.tv_tcdl:
                Dialog dialog = DialogUtil.showDialogConfirm(getActivity(), "??????", "????????????????????????????", "??????", null, "??????", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SPUtil.write(getActivity(), "app", "login", "");
                        jump(LoginActivity.class);
                        JPushInterface.deleteAlias(getActivity(), 111);
                        AppManager.getAppManager().finishOthersActivity(LoginActivity.class);
                        getActivity().finish();
                    }
                }, View.VISIBLE);
                dialog.show();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JumpSelect.jump(getActivity(), list.get(position).getFunType(), list.get(position).getFunLink());
    }

    class MineHolder implements IHolder<MineList> {
        TextView tv, tv_count;
        ImageView iv_dian;

        @Override
        public View createConvertView(LayoutInflater inflater, ViewGroup parent) {
            View convertview = inflater.inflate(R.layout.item_mine, parent, false);
            tv = (TextView) convertview.findViewById(R.id.tv_name);
            iv_dian = (ImageView) convertview.findViewById(R.id.iv_dian);
            tv_count = (TextView) convertview.findViewById(R.id.tv_count);
            return convertview;
        }

        @Override
        public void bindModel(int position, final MineList bean) {
            tv.setText(bean.getFunName());
            String Stamp = list.get(position).getStamp();
            if (!TextUtils.isEmpty(Stamp)) {
                if ("0".equals(Stamp)) {
                    iv_dian.setVisibility(View.VISIBLE);
                    tv_count.setVisibility(View.GONE);
                } else {
                    iv_dian.setVisibility(View.GONE);
                    tv_count.setVisibility(View.VISIBLE);
                    if (Stamp.length() > 2) {
                        tv_count.setText("...");
                    } else {
                        tv_count.setText(Stamp);
                    }
                }
            } else {
                iv_dian.setVisibility(View.GONE);
                tv_count.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 233) {
            getPersonInfo();
        }
    }

    private void initDatas() {
        final Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "extlist");
        VolleyFactory.instance().post(getActivity(), mParams, ServiceEntity.class, new VolleyFactory.BaseRequest<ServiceEntity>() {
            @Override
            public void requestSucceed(String object) {
                list = JSON.parseArray(object.toString(), MineList.class);
                mMyAdapter.setModels(list);
                mMyAdapter.notifyDataSetChanged();
            }

            @Override
            public void requestFailed() {
//                ToastUtil.t("??????????????????");
            }
        }, true, false);
    }

    /**
     * ??????????????????   ??????????????????
     */
    private void getPersonInfo() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "userinfo");
        mParams.put("userid", mUserInfoCache.getUserid());
        mParams.put("TokenId", mUserInfoCache.getTokenid());
        VolleyFactory.instance().post(getActivity(), mParams, User.class, new VolleyFactory.BaseRequest<User>() {
            @Override
            public void requestSucceed(String object) {
                LogUtils.e(object.toString());
                User entity = JSON.parseObject(object.toString(), User.class);
                mUserInfoCache.setUser(entity);
            }

            @Override
            public void requestFailed() {
                ToastUtil.t("??????????????????");
            }
        }, false, false);
    }
}
