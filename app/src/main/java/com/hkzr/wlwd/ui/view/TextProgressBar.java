package com.hkzr.wlwd.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;

public class TextProgressBar extends ProgressBar {

    private Paint mPaint;
    private String text;
    private float rate;

    public TextProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        initView();
    }

    public TextProgressBar(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(Color.BLACK);
        this.mPaint.setTextSize(30);
    }

    @Override
    public synchronized void setProgress(int progress) {
//        setText(progress);
        super.setProgress(progress);
    }

    public synchronized  void setPro(String progress){
        setProgress((int)Float.parseFloat(progress));
        setText(progress);
    }

    private void setText(String progress) {
//        rate = progress * 1.0f / this.getMax();
//        float i = rate * 100;
        this.text = progress + "%";
        Log.e("text+++----++-+-+-+-", text);

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Rect rect = new Rect();
        this.mPaint.getTextBounds(text, 0, this.text.length(), rect);
//        int x = (int) (getWidth() *rate);//让文字跟随进度条
//        if (x == getWidth()) {
//            // 如果为百分之百则在左边绘制。
//            x = getWidth() - rect.right;
//        }

        int x = (int)(getWidth()*0.8);// 让现实的字体处于总宽度80%的位置;;
        int y = (getHeight() / 2) - rect.centerY();// 让显示的字体处于中心位置;;
        canvas.drawText(this.text, x, y, this.mPaint);
    }

}