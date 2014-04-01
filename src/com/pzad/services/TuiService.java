package com.pzad.services;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import com.pzad.category.tuiads.TuiAdsCategory;
import com.pzad.services.FloatWindowService.OnFloatViewEventListener;
import com.pzad.utils.PLog;
import com.pzad.widget.FloatDetailView;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Toast;

public class TuiService extends Service {

	private boolean mOnTop;
	private ActivityManager mActivityManager;
	private String mPackageName;
	
	private FloatDetailView floatDetailView;

	private static final int CHECK_ID = 1087;
	private static final long CHECK_TIME = 5000;
	
	private long delayMillis = 1000L;
	
	private TuiHandler mHandler;
	
	private Timer timer;
	private TimerTask task = new TimerTask(){

		@Override
		public void run() {
			PLog.d("millis", System.currentTimeMillis() + "");
			if(!checkOnTop() && mOnTop){
				
				Message msg = mHandler.obtainMessage(CHECK_ID);
				msg.sendToTarget();
				mOnTop = false;
			}
		}
		
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mActivityManager = (ActivityManager) getSystemService(android.content.Context.ACTIVITY_SERVICE);
		mPackageName = getPackageName();
		
	}
	
	@Override
	public void onStart(Intent intent, int flags){
		super.onStart(intent, flags);
		if(intent.getExtras() != null && intent.getExtras().containsKey("SERVICE_DELAY_TIME")){
			delayMillis = intent.getExtras().getLong("SERVICE_DELAY_TIME");
		}
		if(timer == null){
			mHandler = new TuiHandler(this);
			timer = new Timer();
			timer.schedule(task, 0, delayMillis);
		}
		
	}

	private static class TuiHandler extends Handler {
		WeakReference<Service> serviceReference;
		
		public TuiHandler(Service service){
			serviceReference = new WeakReference<Service>(service);
		}

		public void handleMessage(android.os.Message msg) {
			final TuiService service = (TuiService) serviceReference.get();
			switch (msg.what) {
			case CHECK_ID:
				//Toast.makeText(serviceReference.get(), "1111111111", Toast.LENGTH_SHORT).show();
				if(service.getFloatDetailView() == null){
					service.setFloatDetailView(new TuiAdsCategory(service).createFloatDetailWindow(service));
					service.getFloatDetailView().setOnFloatViewEventListener(new OnFloatViewEventListener(){

						@Override
						public void onCircleViewTap() {
							// TODO Auto-generated method stub
							
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
							if(service.getFloatDetailView() != null){
								WindowManager windowManager = (WindowManager) service.getSystemService(Context.WINDOW_SERVICE);
								windowManager.removeView(service.getFloatDetailView());
								service.setFloatDetailView(null);
								service.stopSelf();
							}
						}
						
					});
				}
				break;
			}
		};
	};

	private boolean checkOnTop() {
		if (mActivityManager == null) {
			mActivityManager = (ActivityManager) getSystemService(android.content.Context.ACTIVITY_SERVICE);
		}
		RunningTaskInfo runningTaskInfo = mActivityManager.getRunningTasks(1).get(0);
		PLog.d(mPackageName, runningTaskInfo.topActivity.getPackageName());
		if (TextUtils.equals(mPackageName, runningTaskInfo.topActivity.getPackageName())) {
			mOnTop = true;
			return true;
		}
		return false;
	}
	
	public void setFloatDetailView(FloatDetailView floatDetailView){
		this.floatDetailView = floatDetailView;
	}
	
	public FloatDetailView getFloatDetailView(){
		return floatDetailView;
	}
}
