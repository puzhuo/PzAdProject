package com.pzad.utils.image;

import java.io.File;

import com.pzad.utils.PLog;

import android.content.Context;

public class FileCache {
	public static final long MAX_SIZE = 1024 * 1024 * 100;
	
	private File cacheDir;
	
	public FileCache(Context context){
		if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
			String[] dirNameArray = context.getPackageName().split("\\.");
			String dirName = "";
			for(String item : dirNameArray){
				dirName += item + "_";
			}
			
			cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), dirName + "cache/data/cahce");
		}else{
			cacheDir = context.getCacheDir();
		}
		
		if(!cacheDir.exists()) cacheDir.mkdirs();
	}
	
	public File getFile(String url){
		String filename = String.valueOf(url.hashCode());
		
		File f = new File(cacheDir, filename);
		return f;
	}
	
	public long checkSize(){
		long size = 0;
		File[] files = cacheDir.listFiles();
		if(files == null){
			return 0;
		}
		
		for(File f : files){
			size += f.length();
		}
		
		return size;
	}
	
	public void clear(){
		File[] files = cacheDir.listFiles();
		if(files == null) return;
			
		for(File f : files) f.delete();
	}
}
