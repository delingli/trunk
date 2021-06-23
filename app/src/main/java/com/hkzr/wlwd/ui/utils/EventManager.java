package com.hkzr.wlwd.ui.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * Created by @seek email:951882080@qq.com
 *a simple eventManager easy to use
 *
 *
 * USAGE:
 *
 * step 1:
 * EventManager.getDefualt().register(listener);
 *then you have to override onEvent(String tag,Object data) for your way handle
 *step 2:
 * send an event in anywhere you want:
 * EventManager.getDefualt().post(tag,eventObj); or EventManager.getDefualt().post(eventObj);
 *
 * step 3:
 * remember to release the reference:
 * EventManager.getDefualt().unRegister(listener);
 */
public class EventManager {

    private static final int EVENT_MSG = 0x0000031;

    private static final String EVENT_TAG = "EventManager_TAG";

    private static InnerHandler sHandler;

    private static EventManager eventManager;

    private static LinkedHashSet<EventListener> mEventListener;

    private EventManager() {
        sHandler = new InnerHandler(Looper.getMainLooper());
        mEventListener = new LinkedHashSet<EventListener>();
    }

    private void addEventListener(EventListener listener) {
        mEventListener.add(listener);
    }

    /**
     * get the instance of EventManager by single
     * @return
     */
    public static EventManager getDefualt() {
        synchronized (EventManager.class) {
            if (eventManager == null) {
                eventManager = new EventManager();
            }
        }
        return eventManager;
    }

    /**
     * add a subscriber
     *
     *and then this subscriber will receive the event data
     * @param eventListener
     */
    public void register(EventListener eventListener) {
        addEventListener(eventListener);
    }

    /**
     * remove the subscriber
     *
     * release the reference of subscriber
     * @param eventListener
     */
    public void unRegister(EventListener eventListener) {
        removeEventListener(eventListener);
    }

    private void removeEventListener(EventListener eventListener) {
        mEventListener.remove(eventListener);
    }

    /**
     * dispatch an object
     * @param eventObj
     */
    public void post(Object eventObj) {
        makeEvent(null, eventObj);
    }

    /**
     * dispatch an Obj data with a tag
     * @param tag
     * @param eventObj
     */
    public void post(String tag,Object eventObj) {
        makeEvent(tag, eventObj);
    }


    /**
     * create an event and post
     * @param tag
     * @param obj
     */
    private void makeEvent(String tag, Object obj) {
        Message message = sHandler.obtainMessage();
        message.what = EVENT_MSG;
        message.obj = obj;
        if (!TextUtils.isEmpty(tag)) {
            Bundle bundle = new Bundle();
            bundle.putString(EVENT_TAG, tag);
            message.setData(bundle);
        }
        message.sendToTarget();
    }



    public interface EventListener {
        /**
         * the data post by @link post(String tag,Object eventObj) or post(Object eventObj)
         * will dispatch to here
         * @param tag
         * @param data
         */
        void onEvent(String tag, Object data);
    }

    private static class InnerHandler extends Handler {
        public InnerHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == EVENT_MSG) {
                Iterator<EventListener> iterator = mEventListener.iterator();
                while (iterator.hasNext()) {
                    EventListener listener = iterator.next();
                    Bundle bundle = msg.getData();
                    String tag = bundle.getString(EVENT_TAG);
                    listener.onEvent(tag,msg.obj);
                }
            }
        }
    }
}
