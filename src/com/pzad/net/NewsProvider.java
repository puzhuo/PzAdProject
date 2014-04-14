package com.pzad.net;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;

import com.pzad.concurrency.PzExecutorFactory;
import com.pzad.concurrency.PzThread;
import com.pzad.entities.NewsInfo;

public class NewsProvider {
	
	private Context context;
	private static NewsProvider instance;
	
	private static final String newsUrl = "http://wapi.90fan.cn/redian.html";
	
    private static final String newsList = "hot";
    private static final String title = "word";
    private static final String url = "url";
    
    private List<NewsInfo> newsInfos;
    
    private  List<OnNewsGotListener> listeners;
    
    public interface OnNewsGotListener{
    	public void onNewsGot(List<NewsInfo> newsInfos);
    }
    
    private NewsProvider(Context context){
    	newsInfos = new ArrayList<NewsInfo>();
    	listeners = new ArrayList<OnNewsGotListener>();
    }
    
    public static NewsProvider getInstance(Context context){
    	if(instance == null){
    		instance = new NewsProvider(context);
    	}
    	
    	return instance;
    }
    
    public void registerNewsGotListener(OnNewsGotListener listener){
    	listeners.add(listener);
    }
    
    private void getNewsInfo(){
    	newsInfos.clear();
    	new PzThread<Void>(){

			@Override
			protected Void run() {
				try{
					String jsonString = new PzHttpClient().httpGet(newsUrl, "");
					if(jsonString != null){
						JSONObject hotJson = new JSONObject(jsonString).getJSONObject(newsList);
						for(int i = 1; i <= 50; i++){
							if(hotJson.has(i + "")){
								NewsInfo newsInfo = new NewsInfo();
								JSONObject itemJson = hotJson.getJSONObject(i + "");
								newsInfo.setTitle(i + "." + itemJson.getString(title));
								newsInfo.setUrl(itemJson.getString("url"));
								
								newsInfos.add(newsInfo);
							}
						}
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onFinish(Void result) {
				onNewsGot(newsInfos);
			}
    		
    	}.executeOnExecutor(PzExecutorFactory.getSingleThreadPool());
    }
    
    private void onNewsGot(List<NewsInfo> newsInfos){
    	if(listeners != null && listeners.size() > 0){
    		for(OnNewsGotListener listener : listeners){
    			listener.onNewsGot(newsInfos);
    		}
    	}
    }
    
    public void requireDataRefresh(){
    	getNewsInfo();
    }
    
    public void obtainNewsInfo(){
    	if(newsInfos == null || newsInfos.size() == 0){
    		getNewsInfo();
    	}else{
    		onNewsGot(newsInfos);
    	}
    }
}
