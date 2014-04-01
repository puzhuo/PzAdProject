package com.pzad.broadcast;

import java.io.Serializable;

import com.pzad.entities.Statistic;
import com.pzad.utils.PLog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StatisticReceiver extends BroadcastReceiver{
	
	public static final String ACTION_RECEIVE_STATISTIC = "com.pzad.statistic.RECEIVE_STATISTIC";
	public static final String NAME = "statistic";

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(ACTION_RECEIVE_STATISTIC)){
			Serializable extra = intent.getSerializableExtra(NAME);
			if(extra instanceof Statistic){
				PLog.d(context.getPackageName(), ((Statistic) extra).toString());
			}
		}
	}

}
