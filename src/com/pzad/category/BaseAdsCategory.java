package com.pzad.category;

import android.content.Context;

import com.pzad.category.api.AdsCycleAPI;

public abstract class BaseAdsCategory implements AdsCycleAPI{
	
	public static final int CATEGORY_FLOAT = 0x1;
	public static final int CATEGORY_BANNER = 0x2;
	public static final int CATEGORY_POPUP_ON_RUN = 0x4;
	public static final int CATEGORY_POPUP_ON_EXIT = 0x8;
	public static final int CATEGORY_INTERCEPT = 0x10;
	public static final int CATEGORY_PUSH = 0x20;
	
	private Context context;
	
	public BaseAdsCategory(Context context){
		this.context= context;
	}
	
	protected Context getContext(){
		return context;
	}
	
}
