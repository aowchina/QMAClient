package com.minfo.quanmei.utils;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

public class AppManager {
	
	private static Stack<Activity> activityStack;
	private static AppManager appManager;
	
	private AppManager(){}
	/**
	 * 单一实例
	 */
	public static AppManager getAppManager(){
		if(appManager==null){
			synchronized (AppManager.class) {
				if(appManager==null) {
					appManager = new AppManager();
				}
			}
		}
		return appManager;
	}
	/**
	 * 添加Activity到堆栈中
	 */
	public void addActivity(Activity activity){
		if(activityStack==null){
			activityStack=new Stack<Activity>();
		}
		activityStack.add(activity);
	}
	/**
	 * 获取当前Activity（堆栈中最后压入的）
	 */
	public Activity currentActivity(){
		Activity activity=activityStack.lastElement();
		return activity;
	}
	/**
	 * 结束当前Activity（堆栈中最后压入的）
	 */
	public void finishActivity(){
		Activity activity=activityStack.lastElement();
		finishActivity(activity);
	}
	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity){
		if(activity!=null){
			activityStack.remove(activity);
			activity.finish();
			activity= null;
		}
	}
	/**
	 * 结束指定类名的Activity
	 */
	public synchronized void finishActivity(Class<?> cls){
		for (Activity activity : activityStack) {
			if(activity.getClass().equals(cls) ){
				finishActivity(activity);
			}
		}
	}
	/**
	 * 结束指定类名的Activity以外的其他Activity
	 */
	public void finishOthersActivity(Class<?> cls){
		for (Activity activity : activityStack) {
			if(!activity.getClass().equals(cls) ){
				finishActivity(activity);
			}
		}
	}
	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity(){
		for (int i = 0, size = activityStack.size(); i < size; i++){
            if (null != activityStack.get(i)){
            	activityStack.get(i).finish();
            }
	    }
		activityStack.clear();
	}
	/**
	 * 彻底退出应用程序，安全退出
	 */
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);
			android.os.Process.killProcess(android.os.Process.myPid());
		} catch (Exception e) {	}
	}
}