package com.pzad.concurrency;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PzExecutorFactory {
	
	private static ThreadPoolExecutor singleThreadPool;
	private static ThreadPoolExecutor imageLoadThreadPool;
	
	public static ThreadPoolExecutor getSingleThreadPool(){
		if(singleThreadPool == null){
			singleThreadPool = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1));
		}
		
		return singleThreadPool;
	}

	public static ThreadPoolExecutor getImageLoadThreadPool(){
		if(imageLoadThreadPool == null){
			imageLoadThreadPool = new ThreadPoolExecutor(10, 15, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(15));
		}
		
		return imageLoadThreadPool;
	}
}
