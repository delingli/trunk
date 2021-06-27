package com.hkzr.wlwd.ui.productwarehouse;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.productlist.Product;
import com.hkzr.wlwd.ui.productlist.ProductListActivity;
import com.hkzr.wlwd.ui.productlist.ProductListAdaptersz;
import com.hkzr.wlwd.ui.productlist.UploadProductParams;
import com.hkzr.wlwd.ui.utils.LogUtil;
import com.hkzr.wlwd.ui.utils.ToastUtil;
import com.hkzr.wlwd.zxing.android.CaptureActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductWareHouseActivity extends BaseActivity {

    private TextView tv_lib_value, tv_lib_code, tv_title, tv_date;
    private RecyclerView mRecyclerView;
    private HouseData mHouseData;
    private List<UploadProductParams> mProductList;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_productwarehouse);
        initViewBind();
    }

    public void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tv_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    private static int REQUEST_HOUSE = 11;
    private static int REQUEST_CPRK = 12;
    private String jOdrId = "";
    private ProductListAdaptersz mRecycleAdaptersz;

    private void setCurrentDate() {
        Calendar cale = Calendar.getInstance();
        int year = cale.get(Calendar.YEAR);
        int month = cale.get(Calendar.MONTH) + 1;
        int day = cale.get(Calendar.DATE);
        tv_date.setText(year + "-" + month + "-" + day);
    }

    private void initViewBind() {
        tv_title = findViewById(R.id.tv_title);
        tv_date = findViewById(R.id.tv_date);
        mRecyclerView = findViewById(R.id.recyclerView);
        tv_lib_value = findViewById(R.id.tv_lib_value);
        tv_lib_code = findViewById(R.id.tv_lib_code);
        tv_title.setText("成品入库");
        setCurrentDate();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mRecycleAdaptersz = new ProductListAdaptersz(this, null));
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickDlg();
            }
        });
        findViewById(R.id.tv_sacn_lib).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductWareHouseActivity.this, CaptureActivity.class);
                intent.putExtra(CaptureActivity.tag, CaptureActivity.kufang);
                startActivityForResult(intent, REQUEST_HOUSE);

            }
        });

        findViewById(R.id.tv_scan_product).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductWareHouseActivity.this, CaptureActivity.class);
                intent.putExtra(CaptureActivity.tag, CaptureActivity.cprk);
                intent.putExtra(CaptureActivity.JOrderID, jOdrId);
                startActivityForResult(intent, REQUEST_CPRK);
            }
        });
        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSubmit();
            }
        });
        findViewById(R.id.iv_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void toSubmit() {
//        {"action":"cprk_saves","tokenId":"","data":{"chkdate":"2021-06-27","kfid":"03a282a6-f15b-401b-96ca-de5be591f2ce","productlist":[
//            {"id":"761f301b-6921-8f26-6a5c-5bfbbc5b8b01","producttype":"1"},{"id":"761f301b-6921-8f26-6a5c-5bfbbc5b8b01","producttype":"2"}]}}
        if (mProductList == null || mProductList.isEmpty()) {
            ToastUtil.show(this, "请先扫码添加入库产品");
            return;
        }
        if (mHouseData == null) {
            ToastUtil.show(this, "请先扫描库房码");
            return;
        }
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

        Map<String, Object> mParams = new HashMap<String, Object>();
        Map<String, Object> mParam2 = new HashMap<String, Object>();
        mParam2.put("chkdate", tv_date.getText().toString());
        mParam2.put("kfid", mHouseData.ID);
        mParam2.put("productlist", mProductList);

        mParams.put("action", "cprk_saves");
        mParams.put("tokenId", "");
        mParams.put("data", mParam2);


        JSONObject object = new JSONObject(mParams);
        String ss = object.toString();
        LogUtil.d("ABC", ss);
        VolleyFactory.instance().xcbfPost(ProductWareHouseActivity.this, object, new VolleyFactory.AbsBaseRequest() {
            @Override
            public void requestFailed(String msg) {
                if (null != msg) {
                    ToastUtil.show(ProductWareHouseActivity.this, msg);
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
                        ToastUtil.show(ProductWareHouseActivity.this, "入库成功");
                    } else {
                        if (!TextUtils.isEmpty(Message)) {
                            ToastUtil.show(ProductWareHouseActivity.this, Message);
                        }
                    }
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_HOUSE == requestCode && resultCode == RESULT_OK) {
            if (data != null) {
                mHouseData = data.getParcelableExtra("house");
                fillData(mHouseData);
            }
        }
        if (REQUEST_CPRK == requestCode && requestCode == RESULT_OK) {
            if (null != mRecycleAdaptersz) {
                Product product = data.getParcelableExtra("product");
                mRecycleAdaptersz.addList(product);
                if (TextUtils.isEmpty(jOdrId)) {
                    jOdrId = product.JOdrId;
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void fillData(HouseData mHouseData) {
        if (mHouseData != null) {
            tv_lib_value.setText(mHouseData.Name);
            tv_lib_code.setText(mHouseData.Code);
        }
    }
}
