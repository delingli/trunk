package com.hkzr.wlwd.ui.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.hkzr.wlwd.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * 放大图片
 */
public class LookBigActivity extends Activity implements PhotoViewAttacher.OnPhotoTapListener {
    private HackyViewPager viewPager;
    private ArrayList<View> pageViews;
    private ImageView imageView;
    private ImageView[] imageViews;
    // 包裹小圆点的LinearLayout
    private Intent intent;
    private ArrayList<String> image_list;
    TextView tv;
    private GuidePageAdapter adapter;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.look_big_image);
        initView();
    }

    @SuppressWarnings({"deprecation", "unchecked"})
    private void initView() {
        LayoutParams params = new LayoutParams(10, 10);
        params.setMargins(3, 0, 3, 0);
        intent = getIntent();
        image_list = intent.getStringArrayListExtra("image_list");
        i = intent.getIntExtra("i", 0);
        tv = (TextView) findViewById(R.id.tv);
        viewPager = (HackyViewPager) findViewById(R.id.guidePages);
        pageViews = new ArrayList<View>();
        for (int i = 0; i < image_list.size(); i++) {
            LinearLayout layout = new LinearLayout(this);
            LayoutParams ltp = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            final PhotoView imageView = new PhotoView(this);
            imageView.setOnPhotoTapListener(this);
            imageView.setScaleType(ScaleType.FIT_CENTER);
            String picUrl = image_list.get(i);
            Picasso.with(this).load(new File(picUrl)).resize(300, 400).into(imageView);
            layout.addView(imageView, ltp);
            pageViews.add(layout);
        }
        tv.setText(i + 1 + "/" + image_list.size());
        adapter = new GuidePageAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new GuidePageChangeListener());
        viewPager.setCurrentItem(i);
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LookBigActivity.this.finish();
            }
        });
    }

    // 指引页面数据适配器
    class GuidePageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pageViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {

            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {

            ((ViewPager) arg0).removeView(pageViews.get(arg1));
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {

            ((ViewPager) arg0).addView(pageViews.get(arg1));
            return pageViews.get(arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {

            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public void finishUpdate(View arg0) {

        }
    }

    // 指引页面更改事件监听器
    class GuidePageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            tv.setText(arg0 + 1 + "/" + image_list.size());
//            for (int i = 0; i < imageViews.length; i++) {
//                imageViews[arg0].setBackgroundResource(R.drawable.yuandian_on);
//
//                if (arg0 != i) {
//                    imageViews[i]
//                            .setBackgroundResource(R.drawable.yuandian);
//                }
//            }
        }
    }

    @Override
    public void onPhotoTap(View view, float x, float y) {
        this.finish();
    }
}
