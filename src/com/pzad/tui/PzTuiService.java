
package com.pzad.tui;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class PzTuiService extends Service {

	private boolean mOnTop;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	private Handler mHandler = new Handler() {

	};
}
