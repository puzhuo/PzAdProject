package com.pzad.utils;

import android.content.Context;

public class FileUtil {
	public static String getExternalPath(Context context){
		if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
			String[] dirNameArray = context.getPackageName().split("\\.");
			String dirName = "";
			for(String item : dirNameArray){
				dirName += item + "_";
			}
			
			return android.os.Environment.getExternalStorageDirectory() + "/" + dirName + "cache/data";
		}else{
			return context.getCacheDir().getPath() + "/data";
		}
	}
	
	public static String getCommonPath(Context context){
		if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
			
			return android.os.Environment.getExternalStorageDirectory() + "/pzad_float_config/cache/data";
		}else{
			return context.getCacheDir().getPath() + "/data";
		}
	}
}
