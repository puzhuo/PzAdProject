package com.pzad.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.util.EncodingUtils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.pzad.broadcast.StatisticReceiver;
import com.pzad.category.floatads.FloatAdsCategory;
import com.pzad.category.floatads.FloatCircleView;
import com.pzad.entities.AppInfo;
import com.pzad.entities.BannerInfo;
import com.pzad.entities.Statistic;
import com.pzad.net.AdsInfoProvider;
import com.pzad.net.AdsInfoProvider.OnAdsGotListener;
import com.pzad.net.FileLoader;
import com.pzad.utils.FileUtil;
import com.pzad.utils.PLog;
import com.pzad.widget.FloatDetailView;

public class FloatWindowService extends Service{
	
	private static final int HANDLE_REFRESH_MEMORY_PERCENTAGE = 0;
	private static final int HANDLE_CREATE_FLOAT_VIEW = 1;
	private static final int HANDLE_DESTROY_FLOAT_CIRCLE_VIEW = 2;
	private static final int HANDLE_RECEIVE_DETAIL_DATA = 3;
	private static final int HANDLE_HIDE_FLOAT_DETAIL_VIEW = 4;
	private static final int HANDLE_RESTORE_FLOAT_DETAIL_VIEW = 5;
	
	public static final String ACTION_HIDE_FLOAT_DETAIL = "com.pzad.floatdetailview.HIDE";
	public static final String ACTION_INSTALLATION_PROCESS = "com.pzad.service.INSTALLATION_PROCESS";
	
	private boolean isOtherFloatWindowRunning;
	
	private boolean isInDetailMode;
	
	private long delayMillis = 500L;

	private FloatCircleView floatCircleView;
	private FloatDetailView floatDetailView;
	
	private Parcelable floatDetailSavedState;
	
	private WindowManager windowManager;
	
	private FloatEventReceiver floatEventReceiver;
	
	private String installationPackageName;
	private String installationName;
	
	private FloatHandler handler;
	private Timer timer;
	private TimerTask timerTask = new TimerTask(){

		@Override
		public void run() {
			
			if(!isOtherFloatWindowRunning && floatCircleView == null && floatDetailView == null && isHome() && !isInDetailMode){
				Message msg = handler.obtainMessage(HANDLE_CREATE_FLOAT_VIEW);
				msg.sendToTarget();
			}
			
			if(!isHome() && (floatCircleView != null || floatDetailView != null) && !isInDetailMode){
				Message msg = handler.obtainMessage(HANDLE_DESTROY_FLOAT_CIRCLE_VIEW);
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
			
			if(isHome() && floatDetailView == null && isInDetailMode && !isOtherFloatWindowRunning){
				Message msg = handler.obtainMessage(HANDLE_RESTORE_FLOAT_DETAIL_VIEW);
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
        isOtherFloatWindowRunning = false;
        isInDetailMode = false;
        
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		
		/*
		ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningProcesses = activityManager.getRunningAppProcesses();
		for(RunningAppProcessInfo processInfo : runningProcesses){
			if(processInfo.processName.contains("process.services.floatwindow.pzad") && !processInfo.processName.contains(getApplicationContext().getPackageName())){
				isOtherFloatWindowRunning = true;
				break;
			}
		}
		 */
        
        if(checkOtherFloatWindowExists()){
        	isOtherFloatWindowRunning = true;
        }
		
		floatEventReceiver = new FloatEventReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_HIDE_FLOAT_DETAIL);
		intentFilter.addAction(ACTION_INSTALLATION_PROCESS);
		intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
		intentFilter.addDataScheme("package");
		
		registerReceiver(floatEventReceiver, intentFilter);
		
	}
	
	@Override
	public void onStart(Intent intent, int startId){
		super.onStart(intent, startId);
		
		if(intent.getExtras() != null && intent.getExtras().containsKey("SERVICE_DELAY_TIME")){
			delayMillis = intent.getExtras().getLong("SERVICE_DELAY_TIME");
		}
		
		handler = new FloatHandler(this);
		if(timer == null){
			timer = new Timer();
			timer.schedule(timerTask, 0, delayMillis);
		}
		
	}
	
	@Override
	public void onDestroy(){
		File configFile = new File(FileUtil.getExternalPath(this) + "/config/floatwindow.cfg");
		if(configFile.exists()){
			configFile.delete();
		}
		unregisterReceiver(floatEventReceiver);
		startService(new Intent(this, FloatWindowService.class));
	}
	
	private static class FloatHandler extends Handler{
		WeakReference<Service> serviceReference;
		FloatWindowService service;
		
		int floatCircleViewStateX;
		int floatCircleViewStateY;
		
		OnFloatViewEventListener onFloatViewEventListener;
		
		public FloatHandler(Service service){
			serviceReference = new WeakReference<Service>(service);
			this.service = (FloatWindowService) serviceReference.get();
			
			final FloatWindowService service_tmp = this.service;
			
			onFloatViewEventListener = new OnFloatViewEventListener(){

				@Override
				public void onCircleViewTap() {
					if(service_tmp.getFloatDetailView() == null){
						WindowManager.LayoutParams params = (LayoutParams) service_tmp.getFloatCircleView().getLayoutParams();
						floatCircleViewStateX = params.x;
						floatCircleViewStateY = params.y;
						
						service_tmp.setFloatDetailView(FloatAdsCategory.getInstance(service_tmp.getApplicationContext()).createFloatDetailWindow(service_tmp.getApplicationContext(), params.x, params.y));
						service_tmp.getFloatDetailView().setOnFloatViewEventListener(this);
						
						service_tmp.getWindowManager().removeView(service_tmp.getFloatCircleView());
						service_tmp.setFloatCircleView(null);
						
					}
				}

				@Override
				public void onCircleViewCloseTap() {}

				@Override
				public void onCircleViewLongTap() {}

				@Override
				public void onDetailViewClose() {
					if(service_tmp.getFloatCircleView() == null){
						service_tmp.setFloatCircleView(FloatAdsCategory.getInstance(service_tmp.getApplicationContext()).createFloatCircleWindow(service_tmp.getApplicationContext(), floatCircleViewStateX, floatCircleViewStateY));
						service_tmp.getFloatCircleView().setOnFloatViewEventListener(this);
						service_tmp.getWindowManager().removeView(service_tmp.getFloatDetailView());
						service_tmp.setFloatDetailView(null);
					}
				}
				
			};
		}
		
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case HANDLE_CREATE_FLOAT_VIEW:
				service.setFloatCircleView(FloatAdsCategory.getInstance(service.getApplicationContext()).createFloatCircleWindow(service.getApplicationContext(), floatCircleViewStateX, floatCircleViewStateY));
				service.getFloatCircleView().setOnFloatViewEventListener(onFloatViewEventListener);
				break;
			case HANDLE_DESTROY_FLOAT_CIRCLE_VIEW:
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
								if(appInfos != null && appInfos.size() > 0) service.getFloatDetailView().setAppInfos(appInfos);
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
			case HANDLE_HIDE_FLOAT_DETAIL_VIEW:
				if(service.getFloatDetailView() != null){
					Parcelable state = service.getFloatDetailView().getViewPager().getAdapter().saveState();
					if(state instanceof Bundle){
						((Bundle) state).putInt("currentItem", service.getFloatDetailView().getViewPager().getCurrentItem());
					}
					
					service.setFloatDetailSaveState(state);
					
					service.getWindowManager().removeView(service.getFloatDetailView());
					service.setFloatDetailView(null);
					service.setIsInDetailMode(true);
				}
				break;
			case HANDLE_RESTORE_FLOAT_DETAIL_VIEW:
				if(service.getFloatDetailView() == null){
					service.setFloatDetailView(FloatAdsCategory.getInstance(service).createFloatDetailWindow(service));
					service.getFloatDetailView().setOnFloatViewEventListener(onFloatViewEventListener);
					service.setIsInDetailMode(false);
					
					//service.getFloatDetailView().getViewPager().getAdapter().restoreState(service.getFloatDetailSaveState(), null);
					service.getFloatDetailView().getViewPager().setCurrentItem(((Bundle) service.getFloatDetailSaveState()).getInt("currentItem"));
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
		activityManager.getMemoryInfo(mi);
		
		return mi.availMem;
	}
	
	private boolean checkOtherFloatWindowExists(){
		File configFile = new File(FileUtil.getExternalPath(this) + "/config/floatwindow.cfg");
		if(configFile.exists()){
			try {
				InputStream is = new FileInputStream(configFile);
				int length = is.available();
				byte[] buffer = new byte[length];
				is.read(buffer);
				String result = EncodingUtils.getString(buffer, "UTF-8");
				if(result != null && !result.equals(getPackageName())){
					ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
					List<RunningServiceInfo> runningServices = activityManager.getRunningServices(65536);
					for(RunningServiceInfo s : runningServices){
						if(s.service.getPackageName().equals(result)){
							is.close();
							return true;
						}
					}
				}
				is.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		File configDirectory = new File(FileUtil.getExternalPath(this) + "/config");
		configDirectory.mkdirs();
		try {
			configFile.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(configFile));
			String input = getPackageName();
			bw.write(input);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
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
	
	public void setFloatDetailSaveState(Parcelable state){
		floatDetailSavedState = state;
	}
	
	public Parcelable getFloatDetailSaveState(){
		return floatDetailSavedState;
	}
	
	public void setIsInDetailMode(boolean isInDetailMode){
		this.isInDetailMode = isInDetailMode;
	}
	
	public class FloatEventReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(ACTION_HIDE_FLOAT_DETAIL)){
				Message msg = handler.obtainMessage(FloatWindowService.HANDLE_HIDE_FLOAT_DETAIL_VIEW);
				msg.sendToTarget();
			}
			if(intent.getAction().equals(ACTION_INSTALLATION_PROCESS)){
				installationName = intent.getStringExtra("app_name");
				installationPackageName = intent.getStringExtra("package_name");
			}
			if(intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED) && installationPackageName != null){
				PLog.d("package_added", installationPackageName);
				Statistic s = new Statistic();
				s.setName(installationName, Statistic.TYPE_APP);
				s.setInstallationCount(1);
				
				Intent sIntent = new Intent(StatisticReceiver.ACTION_RECEIVE_STATISTIC);
				sIntent.putExtra(StatisticReceiver.NAME, s);
				FloatWindowService.this.sendBroadcast(sIntent);
				
				File deleteFile = new File(FileLoader.getCacheFilePath(FloatWindowService.this), installationName + ".apk");
				if(deleteFile.exists()) deleteFile.delete();
				
				installationPackageName = null;
				installationName = null;
				
			}
		}
		
	}
}
