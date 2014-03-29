package com.pzad;

import java.lang.reflect.InvocationTargetException;

import com.pzad.category.BaseAdsCategory;

import android.content.Context;


public class PzManager {
	
	public static final String CATEGORY_FLOAT = ".floatads.FloatAdsCategory";
	//public static final String CATEGORY_INTERCEPT = ".interceptads.InterceptAdsCategory";
	public static final String CATEGORY_POPUP_ON_EXIT = ".tuiads.TuiAdsCategory";
	public static final String CATEGORY_BANNER = ".bannerads.BannerAdsCategory";
	
	private static PzManager pzManager;
    private Context context;
	
	public PzManager(Context context){
		this.context = context;
	}
	
	public static PzManager getInstance(Context context){
		if(pzManager == null){
			pzManager = new PzManager(context);
		}
		
		return pzManager;
	}
	
	public BaseAdsCategory build(String category){
		
		BaseAdsCategory categoryInstance = null;
		
		try{
			Class<?> clazz = Class.forName("com.pzad.category" + category);
			
			categoryInstance = (BaseAdsCategory) clazz.getConstructors()[0].newInstance(context);
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return categoryInstance;
	}
	
	public void destroy(){
		pzManager = null;
	}
}
