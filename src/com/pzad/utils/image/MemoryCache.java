package com.pzad.utils.image;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Bitmap;
import android.util.Log;

public class MemoryCache {
	private Map<String, Bitmap> cache = Collections.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5F, true));
	
	private long size = 0;
	private long limit = 1000000;
	
	public MemoryCache(){
		setLimit(Runtime.getRuntime().maxMemory() / 4);
	}
	
	public void setLimit(long newLimit){
		limit = newLimit;
	}
	
	public Bitmap get(String id){
		try{
			if(!cache.containsKey(id)) return null;
			return cache.get(id);
		}catch(NullPointerException e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void put(String id, Bitmap bitmap){
		try{
			if(cache.containsKey(id)) size -= getSizeInBytes(cache.get(id));
			cache.put(id, bitmap);
			size += getSizeInBytes(bitmap);
		}catch(Throwable th){
			th.printStackTrace();
		}
	}
	
	private void checkSize(){
		if(size > limit){
			Iterator<Entry<String, Bitmap>> iterator = cache.entrySet().iterator();
			
			while(iterator.hasNext()){
				Entry<String, Bitmap> entry = iterator.next();
				size -= getSizeInBytes(entry.getValue());
				iterator.remove();
				if(size <= limit) break;
			}
		}
	}
	
	public void clear(){
		try{
			cache.clear();
			size = 0;
		}catch(NullPointerException e){
			e.printStackTrace();
		}
	}
	
	private long getSizeInBytes(Bitmap bitmap){
		if(bitmap == null) return 0L;
		
		return bitmap.getRowBytes() * bitmap.getHeight();
	}
}
