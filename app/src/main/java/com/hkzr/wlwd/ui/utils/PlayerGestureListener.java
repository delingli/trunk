package com.hkzr.wlwd.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;

/**
 * Created by Moonix on 2016/7/14.
 */
public class PlayerGestureListener extends GestureDetector.SimpleOnGestureListener {
    /**
     * 最大声音
     */
    private int mMaxVolume;
    /**
     * 当前声音
     */
    private int mVolume = -1;
    /**
     * 当前亮度
     */
    private float mBrightness = -1f;
    private float finalBrightness = -1f;

    private int finalVolume = -1;

    private AudioManager mAudioManager;

    private Context mContext;

    public PlayerGestureListener(Context context) {
        this.mContext = context;
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }


    /**
     * 双击
     */
    @Override
    public boolean onDoubleTap(MotionEvent e) {

        return true;
    }

    /**
     * 滑动
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2,
                            float distanceX, float distanceY) {

        float mOldX = e1.getX(), mOldY = e1.getY();
        int y = (int) e2.getRawY();
        Display disp = ((Activity) mContext).getWindowManager().getDefaultDisplay();
        int windowWidth = disp.getWidth();
        int windowHeight = disp.getHeight();

        if (mOldX > windowWidth / 5.0 * 4.0)// 右边滑动
            onVolumeSlide((mOldY - y) / windowHeight);
        else if (mOldX < windowWidth / 5.0)// 左边滑动
            onBrightnessSlide((mOldY - y) / windowHeight);

        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        mVolume = this.finalVolume;
        mBrightness = this.finalBrightness;
        return false;
    }

    /**
     * 滑动改变声音大小
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0)
                mVolume = 0;
        }

        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;

        int showVolume = (int) (((float) mVolume / (float) mMaxVolume * 100) + percent * 100f);
        if (showVolume >= 100) {
            showVolume = 100;
        }
        if (showVolume <= 0) {
            showVolume = 0;
        }
        this.finalVolume = index;
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
    }

    /**
     * 滑动改变亮度
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        if (mBrightness < 0) {
            mBrightness = ((Activity) mContext).getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;
        }

        WindowManager.LayoutParams lpa = ((Activity) mContext).getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        this.finalBrightness = lpa.screenBrightness;
        ((Activity) mContext).getWindow().setAttributes(lpa);
    }
}
