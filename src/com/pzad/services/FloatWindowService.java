package com.pzad.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.pzad.category.floatads.FloatAdsCategory;
import com.pzad.category.floatads.FloatCircleView;
import com.pzad.entities.AppInfo;
import com.pzad.entities.BannerInfo;
import com.pzad.utils.AdsInfoProvider;
import com.pzad.utils.AdsInfoProvider.OnAdsGotListener;
import com.pzad.utils.CalculationUtil;
import com.pzad.utils.PLog;
import com.pzad.widget.FloatDetailView;
import com.pzad.widget.PzCloseButton;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.RelativeLayout;

public class FloatWindowService extends Service{
	
	private static final int HANDLE_REFRESH_MEMORY_PERCENTAGE = 0;
	private static final int HANDLE_CREATE_FLOAT_VIEW = 1;
	private static final int HANDLE_DESTROY_FLOAT_VIEW = 2;
	private static final int HANDLE_RECEIVE_DETAIL_DATA = 3;
	
	private boolean isOtherFloatWindowRunning;
	
	private long delayMillis = 500L;

	private FloatCircleView floatCircleView;
	private FloatDetailView floatDetailView;
	
	private WindowManager windowManager;
	
	private FloatHandler handler;
	private Timer timer;
	private TimerTask timerTask = new TimerTask(){

		@Override
		public void run() {
			
			if(!isOtherFloatWindowRunning && floatCircleView == null && floatDetailView == null && isHome()){
				Message msg = handler.obtainMessage(HANDLE_CREATE_FLOAT_VIEW);
				msg.sendToTarget();
			}
			
			if(!isHome() && (floatCircleView != null || floatDetailView != null)){
				Message msg = handler.obtainMessage(HANDLE_DESTROY_FLOAT_VIEW);
				msg.sendToTarget();
			}
			
			if(isHome() && floatCircleView != null){
				Message msg = handler.obtainMessage(HANDLE_REFRESH_MEMORY_PERCENTAGE);
				msg.sendToTarget();
			}
			
			if(isHome() && floatDetailView != null){
				Message msg = handler.obtainMessage(HANDLE_RECEIVE_DETAIL_DATA);
				msg.sendToTarget();
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
        
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		
		ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningProcesses = activityManager.getRunningAppProcesses();
		for(RunningAppProcessInfo processInfo : runningProcesses){
			if(processInfo.processName.contains("process.services.floatwindow.pzad") && !processInfo.processName.contains(getApplicationContext().getPackageName())){
				isOtherFloatWindowRunning = true;
				break;
			}
		}
		
	}
	
	@Override
	public void onStart(Intent intent, int flags){
		super.onStart(intent, flags);
		
		if(intent.getExtras() != null && intent.getExtras().containsKey("SERVICE_DELAY_TIME")){
			delayMillis = intent.getExtras().getLong("SERVICE_DELAY_TIME");
			PLog.d(delayMillis + "", "adsf");
		}
		
		handler = new FloatHandler(this);
		if(timer == null){
			timer = new Timer();
			timer.schedule(timerTask, 0, delayMillis);
		}
		
	}
	
	private static class FloatHandler extends Handler{
		WeakReference<Service> serviceReference;
		int floatCircleViewStateX;
		int floatCircleViewStateY;
		
		public FloatHandler(Service service){
			serviceReference = new WeakReference<Service>(service);
		}
		
		@Override
		public void handleMessage(Message msg){
			final FloatWindowService service = (FloatWindowService) serviceReference.get();
			switch(msg.what){
			case HANDLE_CREATE_FLOAT_VIEW:
				service.setFloatCircleView(FloatAdsCategory.getInstance(service.getApplicationContext()).createFloatCircleWindow(service.getApplicationContext(), floatCircleViewStateX, floatCircleViewStateY));
				service.getFloatCircleView().setOnFloatViewEventListener(new OnFloatViewEventListener(){

					@Override
					public void onCircleViewTap() {
						// TODO Auto-generated method stub
						if(service.getFloatDetailView() == null){
							WindowManager.LayoutParams params = (LayoutParams) service.getFloatCircleView().getLayoutParams();
							floatCircleViewStateX = params.x;
							floatCircleViewStateY = params.y;
							
							service.setFloatDetailView(FloatAdsCategory.getInstance(service.getApplicationContext()).createFloatDetailWindow(service.getApplicationContext(), params.x, params.y));
							service.getFloatDetailView().setOnFloatViewEventListener(this);
							
							service.getWindowManager().removeView(service.getFloatCircleView());
							service.setFloatCircleView(null);
							
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
						if(service.getFloatCircleView() == null){
							service.setFloatCircleView(FloatAdsCategory.getInstance(service.getApplicationContext()).createFloatCircleWindow(service.getApplicationContext(), floatCircleViewStateX, floatCircleViewStateY));
							service.getFloatCircleView().setOnFloatViewEventListener(this);
							service.getWindowManager().removeView(service.getFloatDetailView());
							service.setFloatDetailView(null);
						}
					}
					
				});
				break;
			case HANDLE_DESTROY_FLOAT_VIEW:
				if(service.getFloatCircleView() != null){
					WindowManager.LayoutParams params = (LayoutParams) service.getFloatCircleView().getLayoutParams();
					floatCircleViewStateX = params.x;
					floatCircleViewStateY = params.y;
					service.getWindowManager().removeView(service.getFloatCircleView());
					service.setFloatCircleView(null);
				}
				break;
			case HANDLE_RECEIVE_DETAIL_DATA:
				if(service.getFloatDetailView() != null && (service.getFloatDetailView().getAppInfos() == null || service.getFloatDetailView().getAppInfos().size() == 0)){
					if(AdsInfoProvider.getInstance(service).isAdsDataAvailable()){
						service.getFloatDetailView().setAppInfos(AdsInfoProvider.getInstance(service).obtainAppInfo());
					}else{
						AdsInfoProvider.getInstance(service).requireDataRefresh();
						AdsInfoProvider.getInstance(service).registerAdsGotListener(new OnAdsGotListener(){

							@Override
							public void onAdsGot(List<AppInfo> appInfos, List<BannerInfo> bannerInfos) {
								service.getFloatDetailView().setAppInfos(appInfos);
							}
							
						});
					}
				}
				break;
			case HANDLE_REFRESH_MEMORY_PERCENTAGE:
				if(service.getFloatCircleView() != null){
					service.getFloatCircleView().setPercentage(service.getMemoryPercentage());
				}
				break;
			}
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
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		for(ResolveInfo resolveInfo : resolveInfos){
			names.add(resolveInfo.activityInfo.packageName);
		}
		
		return names;
	}
	
	private int getMemoryPercentage(){
		String dir = "/proc/meminfo";
		try{
			FileReader fr = new FileReader(dir);
			BufferedReader br = new BufferedReader(fr, 2048);
			String memoryLine = br.readLine();
			String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
			br.close();
			long totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll("\\D+", ""));
			long availableSize = getAvailableMemory(this) /1024;
			int percent = (int) ((totalMemorySize - availableSize) / (float) totalMemorySize * 100);
			return percent;
		}catch(IOException e){
			e.printStackTrace();
		}
		return 0;
	}
	
	private long getAvailableMemory(Context context){
		ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(mi);;
		
		return mi.availMem;
	}
	
	public void setFloatCircleView(FloatCircleView floatCircleView){
		this.floatCircleView = floatCircleView;
	}
	
	public void setFloatDetailView(FloatDetailView floatDetailView){
		this.floatDetailView = floatDetailView;
	}
	
	public FloatCircleView getFloatCircleView(){
		return floatCircleView;
	}
	
	public FloatDetailView getFloatDetailView(){
		return floatDetailView;
	}
	
	public WindowManager getWindowManager(){
		return windowManager;
	}
	
}
