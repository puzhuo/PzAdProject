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
	
	private static final String INSTALLATION_PROCESS = "installation_process";
	private static final String INSTALLATION_PACKAGE_NAME = "package_name";
	
	private PzPreferenceManager(Context context){
		this.context = context.getApplicationContext();
		sharedPreferences = context.getSharedPreferences(pzPreferenceName, Context.MODE_PRIVATE);
	}
	
	public static PzPreferenceManager getInstance(Context context){
		if(instance == null){
			instance = new PzPreferenceManager(context);
		}
		
		return instance;
	}
	
	public void setInstallationProcess(boolean isInstallation){
		sharedPreferences.edit().putBoolean(INSTALLATION_PROCESS, isInstallation).commit();
	}
	
	public boolean getInstallationProcess(){
		return sharedPreferences.getBoolean(INSTALLATION_PROCESS, false);
	}
	
	public void setInstallationPackageName(String packageName){
		sharedPreferences.edit().putString(INSTALLATION_PACKAGE_NAME, packageName).commit();
	}
	
	public String getInstallationPackageName(){
		return sharedPreferences.getString(INSTALLATION_PACKAGE_NAME, null);
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
			setDeviceId(SystemMeasurementUtil.getDeviceId(context));
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
