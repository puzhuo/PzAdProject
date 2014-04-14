package com.pzad.broadcast;

import java.io.Serializable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pzad.entities.Statistic;
import com.pzad.utils.PLog;

public class StatisticReceiver extends BroadcastReceiver{
	
	public static final String ACTION_RECEIVE_STATISTIC = "com.pzad.statistic.RECEIVE_STATISTIC";
	public static final String NAME = "statistic";

	@Override
	public void onReceive(Context context, Intent intent) {
		PLog.d("intent", intent.getAction());
		//PLog.d("intent", intent.getData().toString());
		if(intent.getAction().equals(ACTION_RECEIVE_STATISTIC)){
			Serializable extra = intent.getSerializableExtra(NAME);
			if(extra instanceof Statistic){
				PLog.d(context.getPackageName(), ((Statistic) extra).toString());
			}
			
		}
		
		/*
		String localPackageName = PzPreferenceManager.getInstance(context).getInstallationPackageName();
		if(intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED) && localPackageName != null){
			String remotePackageName = intent.getDataString().substring(intent.getDataString().indexOf(":") + 1);
			
			PLog.d(localPackageName, remotePackageName);
			if(localPackageName.equals(remotePackageName)){
				PLog.d("installed", "asdasd");
			}
			PzPreferenceManager.getInstance(context).setInstallationPackageName(null);
		}
		 */
	}

}
