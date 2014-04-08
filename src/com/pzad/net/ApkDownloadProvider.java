package com.pzad.net;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.pzad.concurrency.PzExecutorFactory;
import com.pzad.net.api.Downloadable;

public class ApkDownloadProvider {
	
	private static final ApkDownloadProvider INSTANCE = new ApkDownloadProvider();
	
	private Context context;
	
	private List<Downloadable> downloadables;
	
	private FileLoader runningTask;
	
	private int tmpUrlFileLength;
	
	private ApkDownloadProvider(){
		downloadables = new ArrayList<Downloadable>();
	}
	
	public static ApkDownloadProvider getInstance(Context context){
		if(INSTANCE.getContext() == null){
			INSTANCE.setContext(context);
		}
		
		return INSTANCE;
	}
	
	public void runNewTask(String url, String name){
		if(runningTask != null && runningTask.isRunning()) return;
		runningTask = new FileLoader(context, url, name){
			@Override
			protected void onPreExecute(){
				preExecute(url);
			}
			
			@Override
			protected void onFileLengthGot(int length){
				tmpUrlFileLength = length;
			}
			
			@Override
			protected void onProgress(float progress){
				progress(url, progress);
			}
			
			@Override
			protected void onFinish(File result){
				boolean fileComplete = false;
				if(tmpUrlFileLength != 0){
					fileComplete = tmpUrlFileLength == result.length();
					tmpUrlFileLength = 0;
				}
				
				finish(url, fileComplete, result);
			}
		};
		
		runningTask.executeOnExecutor(PzExecutorFactory.getApkLoadThreadPool());
	}
	
	private void preExecute(String url){
		if(downloadables != null && downloadables.size() > 0){
			for(Downloadable d : downloadables){
				d.onDownloadStart(url);
			}
		}
	}
	
	private void progress(String downloadLink, float progress){
		if(downloadables != null && downloadables.size() > 0){
			for(Downloadable d : downloadables){
				d.refreshProgress(downloadLink, progress);
			}
		}
	}
	
	private void finish(String downloadLink, boolean isFileComplete, File resultFile){
		if(downloadables != null && downloadables.size() > 0){
			for(Downloadable d : downloadables){
				d.onDownloadComplete(downloadLink, isFileComplete, resultFile.getPath());
			}
		}
	}
	
	public void setContext(Context context){
		this.context = context;
	}
	
	public Context getContext(){
		return context;
	}
	
	public void registerDownloadable(Downloadable downloadable){
		if(!downloadables.contains(downloadable)){
			downloadables.add(downloadable);
		}
	}
}
