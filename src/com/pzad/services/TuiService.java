package com.pzad.services;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.Toast;

public class TuiService extends Service {

	private boolean mOnTop;
	private ActivityManager mActivityManager;
	private String mPackageName;

	private static final int CHECK_ID = 1087;
	private static final long CHECK_TIME = 5000;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mActivityManager = (ActivityManager) getSystemService(android.content.Context.ACTIVITY_SERVICE);
		mPackageName = getPackageName();
		mHandler.sendEmptyMessage(CHECK_ID);
	}

	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CHECK_ID:
				if (!checkOnTop() && mOnTop) {
					// show activity
					Toast.makeText(TuiService.this, "1111111111", Toast.LENGTH_SHORT).show();
					mOnTop = false;
				} else {
					mHandler.sendEmptyMessageDelayed(CHECK_ID, CHECK_TIME);
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
		if (TextUtils.equals(mPackageName, runningTaskInfo.topActivity.getPackageName())) {
			mOnTop = true;
			return true;
		}
		return false;
	}
}
