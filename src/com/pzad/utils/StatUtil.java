package com.pzad.utils;

import android.content.Context;
import android.content.Intent;

import com.pzad.broadcast.StatisticReceiver;
import com.pzad.entities.Statistic;

public class StatUtil {
	
	public static void sendStatistics(Context context, Statistic s){
		Intent intent = new Intent(StatisticReceiver.ACTION_RECEIVE_STATISTIC);
		intent.putExtra(StatisticReceiver.NAME, s);
		
		context.sendBroadcast(intent);
	}
}
