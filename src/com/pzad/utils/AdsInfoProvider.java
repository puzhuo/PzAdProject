package com.pzad.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.pzad.concurrency.PzExecutorFactory;
import com.pzad.concurrency.PzThread;
import com.pzad.dao.PzPreferenceManager;
import com.pzad.entities.AppInfo;
import com.pzad.entities.BannerInfo;

public class AdsInfoProvider {
	
	private Context context;
	
	public static AdsInfoProvider instance;
	
	private List<AppInfo> appInfos;
	private List<BannerInfo> bannerInfos;
	
	private static final String PZAD_URL = "http://appad.aawap.net/browser_admin/ad_list";
	private static final String APP_ARRAY_NAME = "app_list";
	private static final String BANNER_ARRAY_NAME = "banner_list";
	
	private static final String APP_NAME = "name";
	private static final String APP_SIZE = "size";
	private static final String APP_ICON = "icon";
	private static final String APP_DESCRIPTION = "description";
	private static final String APP_DETAIL_LINK = "detail_link";
	private static final String APP_DOWNLOAD_LINK = "download_link";
	
	private static final String BANNER_NAME = "name";
	private static final String BANNER_PIC = "pic";
	private static final String BANNER_LINK = "link";
	
	private static final String DEVICE_ID = "device_id";
	private static final String TELEPHONE_NUM = "telephone_num";
	private static final String ANDROID_VER = "android_ver";
	private static final String RESOLUTION = "resolution";
	
	public interface OnAdsGotListener{
		public void onAdsGot(List<AppInfo> appInfos, List<BannerInfo> bannerInfos);
	}
	
	private List<OnAdsGotListener> onAdsGotListeners;
	
	private AdsInfoProvider(Context context){
		this.context = context;
		appInfos = new ArrayList<AppInfo>();
		bannerInfos = new ArrayList<BannerInfo>();
		
		onAdsGotListeners = new ArrayList<OnAdsGotListener>();
	}
	
	public static AdsInfoProvider getInstance(Context context){
		if(instance == null){
			instance = new AdsInfoProvider(context);
		}
		
		return instance;
	}
	
	private List<BasicNameValuePair> getUserInfo(){
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		
		params.add(new BasicNameValuePair(DEVICE_ID, PzPreferenceManager.getInstance(context).getDeviceId()));
		params.add(new BasicNameValuePair(TELEPHONE_NUM, PzPreferenceManager.getInstance(context).getTelNumber()));
		params.add(new BasicNameValuePair(ANDROID_VER, android.os.Build.VERSION.RELEASE));
		
		int[] resolution = SystemMeasurementUtil.getScreenMeasurement(context);
		params.add(new BasicNameValuePair(RESOLUTION, resolution[0] + "*" + resolution[1]));
		
		return params;
	}
	
	private void getAdInfo(){
		appInfos.clear();
		bannerInfos.clear();
		new PzThread<Void>(){

			@Override
			public Void run() {
				try {
					String jsonString = new PzHttpClient().httpGet(PZAD_URL, getUserInfo());
					
					JSONObject object = new JSONObject(jsonString);
					JSONArray appArray = object.getJSONArray(APP_ARRAY_NAME);
					if(appArray != null){
						JSONObject jsonObject = null;
						AppInfo appInfo = null;
						for(int i = 0; i < appArray.length(); i++){
							jsonObject = appArray.getJSONObject(i);
							appInfo = new AppInfo();
							appInfo.setName(jsonObject.optString(APP_NAME));
							appInfo.setIcon(jsonObject.optString(APP_ICON));
							appInfo.setSize(jsonObject.optString(APP_SIZE));
							appInfo.setDescription(jsonObject.optString(APP_DESCRIPTION));
							appInfo.setDetailLink(jsonObject.optString(APP_DETAIL_LINK));
							appInfo.setDownloadLink(jsonObject.optString(APP_DOWNLOAD_LINK));
							appInfos.add(appInfo);
						}
						
					}
					
					JSONArray bannerArray = object.getJSONArray(BANNER_ARRAY_NAME);
					if(bannerArray != null){
						JSONObject jsonObject = null;
						BannerInfo bannerInfo = null;
						for(int i = 0; i < bannerArray.length(); i++){
							jsonObject = bannerArray.getJSONObject(i);
							bannerInfo = new BannerInfo();
							bannerInfo.setName(jsonObject.optString(BANNER_NAME));
							bannerInfo.setPicture(jsonObject.optString(BANNER_PIC));
							bannerInfo.setLink(jsonObject.optString(BANNER_LINK));
							bannerInfos.add(bannerInfo);
						}
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return null;
			}

			@Override
			public void onFinish(Void result) {
				onAdsGot();
			}
			
			
		}.executeOnExecutor(PzExecutorFactory.getSingleThreadPool());
	}
	
	public boolean isAdsDataAvailable(){
		return appInfos != null && bannerInfos != null && appInfos.size() > 0 && bannerInfos.size() > 0;
	}
	
	public void registerAdsGotListener(OnAdsGotListener listener){
		onAdsGotListeners.add(listener);
	}
	
	private void onAdsGot(){
		if(onAdsGotListeners != null && onAdsGotListeners.size() > 0){
			for(OnAdsGotListener listener : onAdsGotListeners){
				if(listener != null) listener.onAdsGot(appInfos, bannerInfos);
			}
		}
	}
	
	public void requireDataRefresh(){
		getAdInfo();
	}
	
	public List<AppInfo> obtainAppInfo(){
		return appInfos;
	}
	
	public List<BannerInfo> obtainBannerInfo(){
		return bannerInfos;
	}
	
	public void obtainData(){
		if(bannerInfos.size() == 0 || appInfos.size() == 0){
			getAdInfo();
		}else{
			onAdsGot();
		}
	}
}
