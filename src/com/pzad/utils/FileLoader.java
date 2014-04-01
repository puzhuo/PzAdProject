package com.pzad.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;

import com.pzad.concurrency.PzThread;

public class FileLoader extends PzThread<File> {

	String name;
	String url;
	
	String filePath;

	public FileLoader(Context context, String url, String name) {
		this.url = url;
		this.name = name + ".apk";
		
		File file;
		if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
			String[] dirNameArray = context.getPackageName().split("\\.");
			String dirName = "";
			for(String item : dirNameArray){
				dirName += item + "_";
			}
			
			file = new File(android.os.Environment.getExternalStorageDirectory() + "/" + dirName + "cache/data/apk");
		}else{
			file = context.getCacheDir();
		}
		
		if(!file.exists()) file.mkdirs();
		filePath = file.getPath();
	}

	@Override
	public File run() {
		File result = new File(filePath, name);
		try{
			URL url = new URL(FileLoader.this.url);
			URLConnection urlConnection = url.openConnection();
			urlConnection.connect();
			
			int length = urlConnection.getContentLength();
			int bufferSize = 1024;
			
			InputStream is = urlConnection.getInputStream();
			OutputStream os = new FileOutputStream(result);
			
			byte[] bytes = new byte[bufferSize];
			int count = 0;
			int read = -1;
			read = is.read(bytes);
			os.write(bytes, 0, read);
			count = read;
			sendProgress(count * 1.0F / length);
			while((read = is.read(bytes, 0, bufferSize)) != -1){
				os.write(bytes, 0, read);
				count += read;
				sendProgress(count * 1.0F / length);
			}
			
			is.close();
			os.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public void onFinish(File result) {
		
	}

}
