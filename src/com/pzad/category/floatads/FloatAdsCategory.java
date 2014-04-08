package com.pzad.category.floatads;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.pzad.category.AdsArgs;
import com.pzad.category.BaseAdsCategory;
import com.pzad.entities.Statistic;
import com.pzad.services.FloatWindowService;
import com.pzad.utils.CalculationUtil;
import com.pzad.utils.SystemMeasurementUtil;
import com.pzad.widget.FloatDetailView;

public class FloatAdsCategory extends BaseAdsCategory{
	
	private WindowManager windowManager;
	
	public static FloatAdsCategory floatAdsCategory;
	
	public static FloatAdsCategory getInstance(Context context){
		if(floatAdsCategory == null){
			floatAdsCategory = new FloatAdsCategory(context);
		}
		
		return floatAdsCategory;
	}

	public FloatAdsCategory(Context context){
		super(context);
		
		windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	}

	@Override
	public void start(AdsArgs... args) {
		Intent intent = new Intent(getContext(), FloatWindowService.class);

		if(args != null && args.length > 0){
			for(AdsArgs arg : args){
				if((arg.category & BaseAdsCategory.CATEGORY_FLOAT) != 0){
					intent.putExtra("SERVICE_DELAY_TIME", arg.serviceDelayMillis);
				}
			}
		}
		
		getContext().startService(intent);
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
		floatCircleLayoutParams.width = CalculationUtil.dip2px(context, 60);
		floatCircleLayoutParams.height = CalculationUtil.dip2px(context, 60);
		
		floatCircleView.setParams(floatCircleLayoutParams);
		windowManager.addView(floatCircleView, floatCircleLayoutParams);
		
		return floatCircleView;
		
	}
	
	public FloatCircleView createFloatCircleWindow(Context context, int x, int y){
		FloatCircleView result = createFloatCircleWindow(context);
		
		WindowManager.LayoutParams params = (LayoutParams) result.getLayoutParams();
		params.x = x;
		params.y = y;
		
		windowManager.updateViewLayout(result, params);
		
		return result;
	}
	
	public FloatDetailView createFloatDetailWindow(Context context, int... initParams){
		FloatDetailView floatDetailView;
		
        WindowManager.LayoutParams floatDetailLayoutParams = new WindowManager.LayoutParams();
		
		floatDetailLayoutParams = new WindowManager.LayoutParams();
		floatDetailLayoutParams.type = LayoutParams.TYPE_PHONE;
		floatDetailLayoutParams.format = PixelFormat.RGBA_8888;
		floatDetailLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		floatDetailLayoutParams.gravity = Gravity.CENTER;
		floatDetailLayoutParams.width = SystemMeasurementUtil.getScreenMeasurement(context)[0] - CalculationUtil.dip2px(context, 50);
		floatDetailLayoutParams.height = SystemMeasurementUtil.getScreenMeasurement(context)[0];
		
		if(initParams != null && initParams.length > 1){
			floatDetailView = new FloatDetailView(context, initParams[0], initParams[1], false);
		}else{
			floatDetailView = new FloatDetailView(context, false);
		}
		
		
		
		floatDetailView.setParams(floatDetailLayoutParams);
		windowManager.addView(floatDetailView, floatDetailLayoutParams);
		
		return floatDetailView;
	}
}
