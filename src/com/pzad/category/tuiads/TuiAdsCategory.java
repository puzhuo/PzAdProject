package com.pzad.category.tuiads;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.pzad.category.AdsArgs;
import com.pzad.category.BaseAdsCategory;
import com.pzad.entities.AppInfo;
import com.pzad.entities.BannerInfo;
import com.pzad.entities.Statistic;
import com.pzad.services.FloatWindowService.OnFloatViewEventListener;
import com.pzad.services.TuiService;
import com.pzad.utils.AdsInfoProvider;
import com.pzad.utils.CalculationUtil;
import com.pzad.utils.SystemMeasurementUtil;
import com.pzad.utils.AdsInfoProvider.OnAdsGotListener;
import com.pzad.widget.FloatDetailView;

public class TuiAdsCategory extends BaseAdsCategory{

	public TuiAdsCategory(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void start(AdsArgs... args) {
		Intent intent = new Intent(getContext(), TuiService.class);
		
		if(args != null && args.length > 0){
			for(AdsArgs arg : args){
				if((arg.category & BaseAdsCategory.CATEGORY_POPUP_ON_EXIT) != 0){
					intent.putExtra("SERVICE_DELAY_TIME", arg.serviceDelayMillis);
					break;
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

	public FloatDetailView createFloatDetailWindow(Context context){
		final FloatDetailView floatDetailView;
		
		final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		
        WindowManager.LayoutParams floatDetailLayoutParams = new WindowManager.LayoutParams();
		
		floatDetailLayoutParams = new WindowManager.LayoutParams();
		floatDetailLayoutParams.type = LayoutParams.TYPE_PHONE;
		floatDetailLayoutParams.format = PixelFormat.RGBA_8888;
		floatDetailLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		floatDetailLayoutParams.gravity = Gravity.CENTER;
		floatDetailLayoutParams.width = SystemMeasurementUtil.getScreenMeasurement(context)[0] - CalculationUtil.dip2px(context, 50);
		floatDetailLayoutParams.height = SystemMeasurementUtil.getScreenMeasurement(context)[0];
		
		floatDetailView = new FloatDetailView(context, true);
		
		if(AdsInfoProvider.getInstance(context).isAdsDataAvailable()){
			floatDetailView.setAppInfos(AdsInfoProvider.getInstance(context).obtainAppInfo());
		}else{
			AdsInfoProvider.getInstance(context).requireDataRefresh();
			AdsInfoProvider.getInstance(context).registerAdsGotListener(new OnAdsGotListener(){

				@Override
				public void onAdsGot(List<AppInfo> appInfos, List<BannerInfo> bannerInfos) {
					floatDetailView.setAppInfos(appInfos);
				}
				
			});
		}
		
		floatDetailView.setParams(floatDetailLayoutParams);
		windowManager.addView(floatDetailView, floatDetailLayoutParams);
		
		return floatDetailView;
	}
}
