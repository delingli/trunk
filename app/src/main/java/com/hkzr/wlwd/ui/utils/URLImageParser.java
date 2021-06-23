package com.hkzr.wlwd.ui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by admin on 2017/6/20.
 */

public class URLImageParser implements Html.ImageGetter {
    TextView mTextView;
    Context context;

    public URLImageParser(Context context, TextView textView) {
        this.mTextView = textView;
        this.context = context;
    }

    @Override
    public Drawable getDrawable(String source) {
        final URLDrawable urlDrawable = new URLDrawable();
        Picasso.with(context)
                .load(source)
                .resize(400, 400)
                .centerInside()
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Bitmap bit = toMatrix(bitmap);
                        urlDrawable.bitmap = bit;
                        urlDrawable.setBounds(0, 0, bit.getWidth(), bit.getHeight());
                        mTextView.invalidate();
                        mTextView.setText(mTextView.getText()); // 解决图文重叠
                    }

                    @Override
                    public void onBitmapFailed(Drawable drawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable drawable) {

                    }
                });
        return urlDrawable;
    }

    public class URLDrawable extends BitmapDrawable {
        protected Bitmap bitmap;

        @Override
        public void draw(Canvas canvas) {
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, 0, 0, getPaint());
            }
        }
    }

    private Bitmap toMatrix(Bitmap bm) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        float newWidth = DensityUtil.dip2px(context, 230f);
        float newHeight = newWidth / width * bm.getHeight();
        // 计算缩放比例
        float scaleWidth = newWidth / width;
        float scaleHeight = newHeight / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        return newbm;
    }
}