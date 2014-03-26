package com.pzad.category.floatads;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.pzad.category.AdsArgs;
import com.pzad.category.BaseAdsCategory;
import com.pzad.category.entities.Statistic;
import com.pzad.dao.PzPreferenceManager;
import com.pzad.services.FloatWindowService;
import com.pzad.utils.PLog;
import com.pzad.utils.SystemMeasurementUtil;

public class FloatAdsCategory extends BaseAdsCategory{
	
	private FloatDetailView floatDetailView;
	private WindowManager.LayoutParams floatDetailLayoutParams;
	
	private WindowManager windowManager;
	
	public static FloatAdsCategory floatAdsCategory;
	
	/**
	 * 必须为Application的Context
	 */
	public static FloatAdsCategory getInstance(Context context){
		if(floatAdsCategory == null){
			floatAdsCategory = new FloatAdsCategory(context);
		}
		
		return floatAdsCategory;
	}
	
	/**
	 * 必须为Application的Context
	 */
	public FloatAdsCategory(Context context){
		super(context);
		
		PLog.d("asdfasdf", "asdfasdf");
		
		windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	}

	@Override
	public void start(AdsArgs... args) {
		
		PLog.d("FloatAdsCategory", "started");
		getContext().startService(new Intent(getContext(), FloatWindowService.class));
	}

	@Override
	public Statistic getStatistic() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
	public FloatCircleView createFloatCircleWindow(Context context){
		FloatCircleView floatCircleView = new FloatCircleView(context);
		WindowManager.LayoutParams floatCircleLayoutParams = new WindowManager.LayoutParams();
		
		floatCircleLayoutParams = new WindowManager.LayoutParams();
		floatCircleLayoutParams.type = LayoutParams.TYPE_PHONE;
		floatCircleLayoutParams.format = PixelFormat.RGBA_8888;
		floatCircleLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		floatCircleLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
		floatCircleLayoutParams.width = 200;
		floatCircleLayoutParams.height = 200;
		
		floatCircleView.setParams(floatCircleLayoutParams);
		windowManager.addView(floatCircleView, floatCircleLayoutParams);
		
		return floatCircleView;
		
	}
	
	public FloatDetailView createFloatDetailWindow(Context context){
		FloatDetailView floatDetailView = new FloatDetailView(context);
		WindowManager.LayoutParams floatCircleLayoutParams = new WindowManager.LayoutParams();
		
		floatCircleLayoutParams = new WindowManager.LayoutParams();
		floatCircleLayoutParams.type = LayoutParams.TYPE_PHONE;
		floatCircleLayoutParams.format = PixelFormat.RGBA_8888;
		floatCircleLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		floatCircleLayoutParams.gravity = Gravity.CENTER;
		floatCircleLayoutParams.width = SystemMeasurementUtil.getScreenMeasurement(context)[0];
		floatCircleLayoutParams.height = SystemMeasurementUtil.getScreenMeasurement(context)[0];
		
		floatDetailView.setParams(floatCircleLayoutParams);
		windowManager.addView(floatDetailView, floatCircleLayoutParams);
		
		return floatDetailView;
	}
}
