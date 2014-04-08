package com.pzad;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.pzad.category.BaseAdsCategory;
import com.pzad.category.bannerads.BannerAdsCategory;
import com.pzad.category.floatads.FloatAdsCategory;
import com.pzad.category.tuiads.TuiAdsCategory;


public class PzManager {
	
	public static final String CATEGORY_FLOAT = ".floatads.FloatAdsCategory";
	//public static final String CATEGORY_INTERCEPT = ".interceptads.InterceptAdsCategory";
	public static final String CATEGORY_POPUP_ON_EXIT = ".tuiads.TuiAdsCategory";
	public static final String CATEGORY_BANNER = ".bannerads.BannerAdsCategory";
	public static final String CATEGORY_PUSH = ".pushads.PushAdsCategory";
	
	private static PzManager pzManager;
    private Context context;
	
    private List<BannerAdsCategory> bannerAdsContainer;
    private FloatAdsCategory floatAdsInstance;
    private TuiAdsCategory tuiAdsInstance;
    
	public PzManager(Context context){
		this.context = context;
		bannerAdsContainer = new ArrayList<BannerAdsCategory>();
	}
	
	public static PzManager getInstance(Context context){
		if(pzManager == null){
			pzManager = new PzManager(context);
		}
		
		return pzManager;
	}
	
	public BaseAdsCategory build(String category){
		
		if(category.equals(CATEGORY_FLOAT) && floatAdsInstance != null) return floatAdsInstance;
		if(category.equals(CATEGORY_POPUP_ON_EXIT) && tuiAdsInstance != null) return tuiAdsInstance;
		
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
		
		if(categoryInstance instanceof BannerAdsCategory){
			bannerAdsContainer.add((BannerAdsCategory) categoryInstance);
		}
		
		if(categoryInstance instanceof TuiAdsCategory){
			tuiAdsInstance = (TuiAdsCategory) categoryInstance;
		}
		
		if(categoryInstance instanceof FloatAdsCategory){
			floatAdsInstance = (FloatAdsCategory) categoryInstance;
		}
		
		return categoryInstance;
	}
	
	public void destroy(){
		pzManager = null;
	}
}
