package com.pzad.utils;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;

public class ActivityLoader {

	public static void startOfficialBrowser(Context context, String url){

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		
		PackageManager packageManager = context.getPackageManager();
		List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, 0);
		for(ResolveInfo r : resolveInfos){
			ApplicationInfo ai = null;
			try{
				ai = packageManager.getApplicationInfo(r.activityInfo.packageName, 0);
			}catch(NameNotFoundException e){
				e.printStackTrace();
			}
			
			if(ai != null && (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0){
				Intent launchIntent = packageManager.getLaunchIntentForPackage(ai.packageName);
				launchIntent.setData(Uri.parse(url));
				
				launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(launchIntent);
			}
		}
	}
}
