package com.pzad.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.pzad.category.floatads.FloatAdsCategory;
import com.pzad.category.floatads.FloatCircleView;
import com.pzad.category.floatads.FloatDetailView;
import com.pzad.utils.PLog;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;

public class FloatWindowService extends Service{
	
	private boolean isOtherFloatWindowRunning;

	private FloatCircleView floatCircleView;
	private FloatDetailView floatDetailView;
	
	private Handler handler;
	private Timer timer;
	private TimerTask timerTask = new TimerTask(){

		@Override
		public void run() {
			
			if(!isOtherFloatWindowRunning && floatCircleView == null && floatDetailView == null){
				
				PLog.d("window", "window");
				//floatCircleView = FloatAdsCategory.getInstance(getApplicationContext()).createFloatCircleWindow(FloatWindowService.this.getApplicationContext());
			}
		}
		
	};
	
	public interface OnFloatViewEventListener{
		public void onCircleViewTap();
		public void onCircleViewCloseTap();
		public void onCircleViewLongTap();
		public void onDetailViewClose();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate(){
		PLog.d("service", "started");
        isOtherFloatWindowRunning = false;
		
		ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningProcesses = activityManager.getRunningAppProcesses();
		for(RunningAppProcessInfo processInfo : runningProcesses){
			if(processInfo.processName.contains("process.services.floatwindow.pzad") && !processInfo.processName.contains(getApplicationContext().getPackageName())){
				isOtherFloatWindowRunning = true;
				break;
			}
		}
		
		if(!isOtherFloatWindowRunning){
			floatCircleView = FloatAdsCategory.getInstance(getApplicationContext()).createFloatCircleWindow(FloatWindowService.this.getApplicationContext());
			floatCircleView.setOnFloatViewEventListener(new OnFloatViewEventListener(){

				@Override
				public void onCircleViewTap() {
					// TODO Auto-generated method stub
					if(floatDetailView == null){
						floatDetailView = FloatAdsCategory.getInstance(getApplicationContext()).createFloatDetailWindow(FloatWindowService.this.getApplicationContext());
					}
				}

				@Override
				public void onCircleViewCloseTap() {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onCircleViewLongTap() {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onDetailViewClose() {
					// TODO Auto-generated method stub
					
				}
				
			});
		}else{
			
		}
	}
	
	@Override
	public void onStart(Intent intent, int flags){
		super.onStart(intent, flags);
		
		handler = new Handler();
		if(timer == null){
			timer = new Timer();
			timer.schedule(timerTask, 0, 500);
		}
		
	}
	
	private boolean isHome(){
		ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
		return getHome().contains(runningTaskInfos.get(0).topActivity.getPackageName());
	}
	
	private List<String> getHome(){
		List<String> names = new ArrayList<String>();
		PackageManager packageManager = getPackageManager();
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		for(ResolveInfo resolveInfo : resolveInfos){
			names.add(resolveInfo.activityInfo.packageName);
		}
		
		return names;
	}
	
}
