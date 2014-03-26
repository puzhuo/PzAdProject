package com.pzad.dao;

import com.pzad.utils.SystemMeasurementUtil;

import android.content.Context;
import android.content.SharedPreferences;

public class PzPreferenceManager {
	
	private static final String pzPreferenceName = "pz_data";
	
	private Context context;
	
	private static PzPreferenceManager instance;
	private SharedPreferences sharedPreferences;
	
	private static final String TEL_NUMBER = "tel_number";
	private static final String DEVICE_ID = "device_id";
	private static final String SYS_VERSION = "sys_version";
	
	private PzPreferenceManager(Context context){
		this.context = context;
		sharedPreferences = context.getSharedPreferences(pzPreferenceName, Context.MODE_PRIVATE);
	}
	
	public static PzPreferenceManager getInstance(Context context){
		if(instance == null){
			instance = new PzPreferenceManager(context);
		}
		
		return instance;
	}
	
	public void setTelNumber(String number){
		sharedPreferences.edit().putString(TEL_NUMBER, number).commit();
	}
	
	public String getTelNumber(){
		if(sharedPreferences.getString(TEL_NUMBER, null) == null){
			String number = SystemMeasurementUtil.getTelNumber(context);
			setTelNumber(number);
			return number;
		}
		return sharedPreferences.getString(TEL_NUMBER, null);
	}
	
	public void setDeviceId(String deviceId){
		sharedPreferences.edit().putString(DEVICE_ID, deviceId).commit();
	}
	
	public String getDeviceId(){
		if(sharedPreferences.getString(DEVICE_ID, null) == null){
			
		}
		return sharedPreferences.getString(DEVICE_ID, null);
	}
	
	public void setSystemVersion(String version){
		sharedPreferences.edit().putString(SYS_VERSION, version).commit();
	}
	
	public String getSystemVersion(){
		return sharedPreferences.getString(SYS_VERSION, null);
	}
}
