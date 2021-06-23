package com.hkzr.wlwd.ui.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hkzr.wlwd.ui.utils.TimeUtil;

/**
 * Created by admin on 2016/10/7.
 */

/***
 * 自定义textview 用于更新聊天室时间
 */
public class UpdateTime extends TextView {
    private long countDownInterval = 5000L;
    private TimeCount timeCount;
    private long sentTime;

    public UpdateTime(Context context) {
        super(context);
        init();
    }

    public UpdateTime(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UpdateTime(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        // 暂时没什么可以初始化的
    }



    /**
     * 放入時間信息
     *
     * @param sentTime
     */
    public void setTime(long sentTime) {
        this.sentTime = sentTime;
        setText(TimeUtil.getDescriptionTime(sentTime));
        if (timeCount != null) {
            timeCount.cancel();
            timeCount = null;
        }
        if (timeCount == null) {
            timeCount = new TimeCount(sentTime, countDownInterval);
        }
        timeCount.start();
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
        }

        @Override
        public void onTick(long millisUntilFinished) {
            setText(TimeUtil.getDescriptionTime(sentTime));
        }
    }

}
