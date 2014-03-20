package com.pzad;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;


public class PzManager {

	private static final int APP_LOW_LIMIT  = 10;
	private static final int BANNER_LOW_LIMIT  = 2;
	private static final String PZAD_URL = "http://appad.aawap.net/browser_admin/ad_list";
	
	private ArrayList<AppInfo> mAppInfos = new ArrayList<AppInfo>();
	private ArrayList<BannerInfo> mBannerInfos = new ArrayList<BannerInfo>();
	
	private boolean hasInited() {
		return ((mAppInfos != null && mAppInfos.size() >= APP_LOW_LIMIT) && (mBannerInfos != null && mBannerInfos.size() >= BANNER_LOW_LIMIT));
	}
	
	private void init() {
		// to get the ad info and store them in mAppInfos and mBannerInfos 
	}
	
	public static void startTui(Context context) {
		context.startService(new Intent(context, PzTuiService.class));
	}
}
