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

import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.utils.LogUtil;
import com.hkzr.wlwd.ui.utils.ToastUtil;
import com.hkzr.wlwd.zxing.android.CaptureActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductListActivity extends BaseActivity {

    private TextView tv_title;
    private RecyclerView mRecyclerView;
    private ProductListAdaptersz mRecycleAdaptersz;
    private Button mBtnSubmit;
    private TextView tv_right;
    private static int REQUEST_CODE = 101;
    private TextView mTv_date;
    private List<UploadProductParams> mProductList;

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
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    public void toSubmit() {
//        {"action":"chk_saves","tokenId":"","data":[{"id":"761f301b-6921-8f26-6a5c-5bfbbc5b8b01","producttype":"1"}]}
        if (mRecycleAdaptersz != null && mRecycleAdaptersz.getList() != null && !mRecycleAdaptersz.getList().isEmpty()) {
            mProductList = new ArrayList<>();
            List<Product> list = mRecycleAdaptersz.getList();
            UploadProductParams uploadProductParams = new UploadProductParams();
            for (Product pp : list) {
                uploadProductParams.id = pp.id;
                uploadProductParams.producttype = pp.producttype;
                mProductList.add(uploadProductParams);
            }
        }
        if (mProductList == null || mProductList.isEmpty()) {
            ToastUtil.show(this, "请先扫码添加检测产品");
            return;
        }
        Map<String, Object> mParams = new HashMap<String, Object>();
        Map<String, Object> mParam2 = new HashMap<String, Object>();
        mParam2.put("chkdate", mTv_date.getText().toString());
        mParam2.put("productlist", mProductList);

        mParams.put("action", "chk_saves");
        mParams.put("tokenId", "");
        mParams.put("data", mParam2);


        JSONObject object = new JSONObject(mParams);
        String ss = object.toString();
        LogUtil.d("ABC", ss);
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
    }
}
