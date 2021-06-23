package com.hkzr.wlwd.ui.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import java.util.ArrayDeque;
import java.util.Iterator;

public class AppManager {
	
	private static ArrayDeque<Activity> activityStack;
	private static AppManager instance;

	private AppManager() {
	}

	public static AppManager getAppManager() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new ArrayDeque<Activity>();
		}
		activityStack.push(activity);
	}

	public Activity currentActivity() {
		if (activityStack == null || activityStack.isEmpty()) {
			return null;
		}
		Activity activity = activityStack.peek();
		return activity;
	}

	public Activity findActivity(Class<?> cls) {
		Activity activity = null;
		Iterator<Activity> iterator = activityStack.iterator();
		if (iterator.hasNext()) {
			activity = iterator.next();
			if (activity.getClass().equals(cls)) {
				return activity;
			}
		}
		return activity;
	}

	public void finishActivity() {
		Activity activity = activityStack.pop();
		activity.finish();
	}

	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
		}
	}

	public void finishActivity(Class<?> cls) {
		Iterator<Activity> iterator = activityStack.iterator();
		while(iterator.hasNext()) {
			Activity activity = iterator.next();
			if (activity.getClass().equals(cls)) {
				iterator.remove();
				activity.finish();
			}
		}
	}

	public void finishOthersActivity(Class<?> cls) {
		Iterator<Activity> iterator = activityStack.iterator();
		while(iterator.hasNext()) {
			Activity activity = iterator.next();
			if (!activity.getClass().equals(cls)) {
				iterator.remove();
				activity.finish();
			}
		}
	}

	public void finishAllActivity() {
		Iterator<Activity> iterator = activityStack.iterator();
		while(iterator.hasNext()) {
			Activity activity = iterator.next();
			iterator.remove();
			activity.finish();
		}
		activityStack.clear();
	}

	/**
	 * exit this app
	 * 
	 * @param context
	 */
	public void appExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.killBackgroundProcesses(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {
			System.exit(0);
		}
	}
}