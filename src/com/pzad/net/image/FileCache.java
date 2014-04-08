package com.pzad.net.image;

import java.io.File;

import android.content.Context;

import com.pzad.utils.FileUtil;

public class FileCache {
	public static final long MAX_SIZE = 1024 * 1024 * 100;
	
	private File cacheDir;
	
	public FileCache(Context context){
		
		cacheDir = new File(FileUtil.getExternalPath(context) + "/image");
		
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
