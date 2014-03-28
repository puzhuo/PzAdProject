package com.pzad.concurrency;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class PzExecutorFactory {
	
	private static ExecutorService singleThreadPool;
	private static ExecutorService imageLoadThreadPool;
	
	public static ExecutorService getSingleThreadPool(){
		if(singleThreadPool == null){
			singleThreadPool = Executors.newFixedThreadPool(1);
		}
		
		return singleThreadPool;
	}

	public static ExecutorService getImageLoadThreadPool(){
		if(imageLoadThreadPool == null){
			//imageLoadThreadPool = Executors.newFixedThreadPool(15);
			imageLoadThreadPool = Executors.newCachedThreadPool();
		}
		
		return imageLoadThreadPool;
	}
	
}
