package com.pzad.net;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.pzad.concurrency.PzExecutorFactory;
import com.pzad.entities.Statistic;
import com.pzad.net.api.Downloadable;
import com.pzad.services.FloatWindowService;
import com.pzad.utils.PLog;
import com.pzad.utils.StatUtil;

public class ApkDownloadProvider {
	
	private static final ApkDownloadProvider INSTANCE = new ApkDownloadProvider();
	
	private Context context;
	
	private HashMap<String, List<Downloadable>> downloadablesMap;
	
	private FileLoader runningTask;
	
	private int tmpUrlFileLength;
	
	private boolean isRunning;
	
	private String currentDownloadLink;
	private float currentDownloadProgress = -1;
	
	private ApkDownloadProvider(){
		downloadablesMap = new HashMap<String, List<Downloadable>>();
		isRunning = false;
	}
	
	public static ApkDownloadProvider getInstance(Context context){
		if(INSTANCE.getContext() == null){
			INSTANCE.setContext(context);
		}
		
		return INSTANCE;
	}
	
	public void runNewTask(final String url, final String name){
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
			protected void onFinish(File result, boolean fileLoadFromLocal){
				boolean fileComplete = false;
				if(isRunning && tmpUrlFileLength != 0){
					fileComplete = tmpUrlFileLength == result.length();
					tmpUrlFileLength = 0;
				}
				
				if(!fileLoadFromLocal && fileComplete){
					
					StatUtil.sendStatistics(context, new Statistic().setName(name, Statistic.TYPE_APP).setDownloadCount(1));
				}
				
				finish(url, fileComplete, result, name);
			}
		};
		
		runningTask.executeOnExecutor(PzExecutorFactory.getApkLoadThreadPool());
		isRunning = true;
	}
	
	private void preExecute(String url){
		currentDownloadLink = url;
		if(downloadablesMap != null && downloadablesMap.containsKey(url)){
			List<Downloadable> loop = downloadablesMap.get(url);
			if(loop != null && loop.size() > 0){
				for(Downloadable d : loop){
					if(d != null) d.onDownloadStart(url);
				}
			}
		}
	}
	
	private void progress(String downloadLink, float progress){
		currentDownloadProgress = progress;
		if(downloadablesMap != null && downloadablesMap.containsKey(downloadLink)){
			List<Downloadable> loop = downloadablesMap.get(downloadLink);
		    if(loop != null && loop.size() > 0){
		    	for(Downloadable d : loop){
		    		PLog.d("progress", progress + "");
		    		if(d != null) d.refreshProgress(downloadLink, progress);
		    	}
		    	PLog.d("loop", "loop");
		    }
		}
	}
	
	private void finish(String downloadLink, boolean isFileComplete, File resultFile, String appName){
		currentDownloadLink = null;
		currentDownloadProgress = -1;
		if(downloadablesMap != null && downloadablesMap.containsKey(downloadLink)){
			List<Downloadable> loop = downloadablesMap.get(downloadLink);
			if(loop != null && loop.size() > 0){
				for(Downloadable d : loop){
					if(d != null) d.onDownloadComplete(downloadLink, isFileComplete, resultFile.getPath());
				}
			}
		}
		
		PLog.d("file", resultFile.toString());
		
		Intent hideIntent = new Intent(FloatWindowService.ACTION_HIDE_FLOAT_DETAIL);
		hideIntent.setData(new Uri.Builder().scheme("package").build());
		getContext().sendBroadcast(hideIntent);
		
		PackageManager pm = getContext().getPackageManager();
		PackageInfo pkgInfo = pm.getPackageArchiveInfo(resultFile.getAbsolutePath(), PackageManager.GET_ACTIVITIES);
		if(pkgInfo != null){
			ApplicationInfo applicationInfo = pkgInfo.applicationInfo;
			applicationInfo.sourceDir = resultFile.getAbsolutePath();
			applicationInfo.publicSourceDir = applicationInfo.sourceDir;
			
			Intent installationIntent = new Intent(FloatWindowService.ACTION_INSTALLATION_PROCESS);
			installationIntent.putExtra("package_name", applicationInfo.packageName);
			installationIntent.putExtra("app_name", appName);
			installationIntent.setData(new Uri.Builder().scheme("package").build());
			
			getContext().sendBroadcast(installationIntent);
		}
		
		Intent installIntent = new Intent(Intent.ACTION_VIEW);
		installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		installIntent.setDataAndType(Uri.fromFile(resultFile), "application/vnd.android.package-archive");
		getContext().startActivity(installIntent);
	}
	
	public void setContext(Context context){
		this.context = context;
	}
	
	public Context getContext(){
		return context;
	}
	
	public void registerDownloadable(Downloadable downloadable){
		
		if(downloadable != null){
			String link = downloadable.getDownloadLink();
			if(link != null && downloadablesMap != null){
			    if(downloadablesMap.containsKey(link)){
				    downloadablesMap.get(link).add(downloadable);
			    }else{
				    List<Downloadable> d = new ArrayList<Downloadable>();
				    d.add(downloadable);
				
				    downloadablesMap.put(link, d);
			    }
			}
			
		}
	}
	
	public void removeDownloadable(Downloadable downloadable){
		if(downloadable != null){
			String link = downloadable.getDownloadLink();
			
			if(link != null && downloadablesMap != null && downloadablesMap.containsKey(link)){
				List<Downloadable> d = downloadablesMap.get(link);
				if(d != null && d.contains(downloadable)){
					d.remove(downloadable);
					if(d.size() == 0){
						downloadablesMap.remove(d);
						d = null;
					}
				}
			}
			
		}
	}
	
	public void killAllTasks(){
		isRunning = false;
	}
	
	public String getCurrentDownloadLink(){
		return currentDownloadLink;
	}
	
	public float getCurrentDownloadProgress(){
		return currentDownloadProgress;
	}
}
