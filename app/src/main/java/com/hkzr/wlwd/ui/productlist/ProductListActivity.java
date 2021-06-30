package com.hkzr.wlwd.ui.productlist;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.profilecheck.ProfileCheckActivity;
import com.hkzr.wlwd.ui.utils.LogUtil;
import com.hkzr.wlwd.ui.utils.ToastUtil;
import com.hkzr.wlwd.zxing.android.CaptureActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductListActivity extends BaseActivity {
    private String jOrdername = "";
    private String jOrderId = "";
    private TextView tv_title;
    private RecyclerView mRecyclerView;
    private ProductListAdaptersz mRecycleAdaptersz;
    private Button mBtnSubmit;
    private TextView tv_right;
    private static int REQUEST_CODE = 101;
    private TextView mTv_date, tv_jOrderName;
    private List<UploadProductParams> mProductList;
    private TextView mTvToLook;

    public void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mTv_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Product mProduct = data.getParcelableExtra("product");
                if (mProduct != null) {
                    //todo 去重
                    mRecycleAdaptersz.addList(mProduct);
                    if (TextUtils.isEmpty(jOrdername)) {
                        jOrdername = mProduct.JOdrName;
                        jOrderId = mProduct.JOdrId;
                        tv_jOrderName.setText(jOrdername);
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_productlist);
        initViewBind();

    }

    private void setCurrentDate() {
        Calendar cale = Calendar.getInstance();
        int year = cale.get(Calendar.YEAR);
        int month = cale.get(Calendar.MONTH) + 1;
        int day = cale.get(Calendar.DATE);
        mTv_date.setText(year + "-" + month + "-" + day);
    }

    private void initViewBind() {
        mTv_date = findViewById(R.id.tv_date);
        tv_jOrderName = findViewById(R.id.tv_jOrderName);
        mTvToLook = findViewById(R.id.tv_toLook);

        mRecyclerView = findViewById(R.id.recyclerView);
        tv_right = findViewById(R.id.tv_right);
        mBtnSubmit = findViewById(R.id.btn_submit);
        findViewById(R.id.iv_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("检查品项列表");
        tv_right.setText("去扫码");
        setCurrentDate();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mRecycleAdaptersz = new ProductListAdaptersz(this, null));
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSubmit();
            }
        });
        mTv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickDlg();
            }
        });
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductListActivity.this, CaptureActivity.class);
                intent.putExtra(CaptureActivity.tag, CaptureActivity.zljc);

                if (!TextUtils.isEmpty(jOrderId)) {
                    intent.putExtra(CaptureActivity.JOrderID, jOrderId);
                }
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        mTvToLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductListActivity.this, ProfileCheckActivity.class);
                startActivity(intent);
            }
        });
    }

    public void toSubmit() {
//        {"action":"chk_saves","tokenId":"","data":[{"id":"761f301b-6921-8f26-6a5c-5bfbbc5b8b01","producttype":"1"}]}
        if (mRecycleAdaptersz == null || mRecycleAdaptersz.getList() == null || mRecycleAdaptersz.getList().isEmpty()) {
            ToastUtil.show(this, "请先扫码添加检测产品");
            return;
        }
        if (mRecycleAdaptersz != null && mRecycleAdaptersz.getList() != null && !mRecycleAdaptersz.getList().isEmpty()) {
            mProductList = new ArrayList<>();
            List<Product> list = mRecycleAdaptersz.getList();
            UploadProductParams uploadProductParams;
            for (Product pp : list) {
                uploadProductParams = new UploadProductParams();
                uploadProductParams.id = pp.id;
                uploadProductParams.producttype = pp.producttype;
                mProductList.add(uploadProductParams);
            }
        }

        try {
            SaveProductParams saveProductParams = new SaveProductParams();
            SaveProductParams.Databean databean = new SaveProductParams.Databean();
            databean.chkdate = mTv_date.getText().toString();
            databean.productlist = mProductList;
            saveProductParams.action = "chk_saves";
            saveProductParams.tokenId = "";
            saveProductParams.data = databean;
            Gson gson = new Gson();
            String ss = gson.toJson(saveProductParams);
            LogUtil.d("ABC", ss);
            JSONObject object = new JSONObject(ss);
            VolleyFactory.instance().xcbfPost(ProductListActivity.this, object, new VolleyFactory.AbsBaseRequest() {
                @Override
                public void requestFailed(String msg) {
                    if (null != msg) {
                        ToastUtil.show(ProductListActivity.this, msg);
                    }
                    finish();
                }

                @Override
                public void requestSucceed(String str) {
                    LogUtil.d("ldlPP", str);
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        String Message = jsonObject.optString("Message");

                        if (jsonObject.optBoolean("Success")) {
                            ToastUtil.show(ProductListActivity.this, "提交成功");
                            finish();
                        } else {
                            if (!TextUtils.isEmpty(Message)) {
                                ToastUtil.show(ProductListActivity.this, Message);
                            }
                        }
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
