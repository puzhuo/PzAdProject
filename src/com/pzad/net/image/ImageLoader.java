package com.pzad.net.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageLoader {
	
	private static MemoryCache memoryCache;
	private static FileCache fileCache;

	public static Bitmap getBitmap(Context context, String url){
		if(memoryCache == null) memoryCache = new MemoryCache();
		if(fileCache == null) fileCache = new FileCache(context);
		
		Bitmap result = null;
		
		if((result = memoryCache.get(url)) == null){
			File bitmapFile = fileCache.getFile(url);
			
			if((result = decodeBitmapFromFile(bitmapFile)) == null){
				try{
				    URL imageUrl = new URL(url);
				    URLConnection connection =  imageUrl.openConnection();
				    connection.setConnectTimeout(30000);
				    connection.setReadTimeout(30000);
				    connection.connect();
				
				    InputStream is = connection.getInputStream();
				    BitmapFactory.Options options = new BitmapFactory.Options();
				    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				    result = BitmapFactory.decodeStream(is, null, options);
				    memoryCache.put(url, result);
				    OutputStream os = new FileOutputStream(bitmapFile);
				    result.compress(Bitmap.CompressFormat.PNG, 100, os);
				    //StreamUtil.copyStream(is, os, connection.getContentLength());
				    os.close();
				    //result = decodeBitmapFromFile(bitmapFile);
				    
				}catch(Throwable e){
				    e.printStackTrace();
				    if(e instanceof OutOfMemoryError){
				        memoryCache.clear();
				    }
				}
			}
		}
		
		return result;
	}
	
	private static Bitmap decodeBitmapFromFile(File file){
		Bitmap bitmap = null;
		try{
			/*
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			FileInputStream stream = new FileInputStream(file);
			BitmapFactory.decodeStream(stream, null, options);
			stream.close();
			 */
			
			FileInputStream fis = new FileInputStream(file);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			bitmap = BitmapFactory.decodeStream(fis, null, options);
			fis.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return bitmap;
	}
	
	public static void clearCache(){
		fileCache.clear();
		memoryCache.clear();
	}
	
	public long checkSize(){
		return fileCache.checkSize();
	}
}
