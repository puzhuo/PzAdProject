package com.pzad.category.bannerads;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.pzad.category.AdsArgs;
import com.pzad.category.BaseAdsCategory;
import com.pzad.category.entities.Statistic;
import com.pzad.entities.AppInfo;
import com.pzad.entities.BannerInfo;
import com.pzad.utils.AdsInfoProvider;
import com.pzad.utils.AdsInfoProvider.OnAdsGotListener;
import com.pzad.utils.PLog;

public class BannerAdsCategory extends BaseAdsCategory {
	
	private FrameLayout rootLayout;
	private BannerImageView iv;

	public BannerAdsCategory(Context context) {
		super(context);
	}

	@Override
	public void start(AdsArgs... args) {
		if(getContext() instanceof Activity){
			Activity currentActivity = (Activity) getContext();
			
			rootLayout = (FrameLayout) currentActivity.findViewById(android.R.id.content);
			iv = new BannerImageView(currentActivity);
			if(!AdsInfoProvider.getInstance(getContext()).isAdsDataAvailable()){
				PLog.d("banner", "obtaininfo");
				AdsInfoProvider.getInstance(getContext()).registerAdsGotListener(new OnAdsGotListener(){

					@Override
					public void onAdsGot(List<AppInfo> appInfos, List<BannerInfo> bannerInfos) {
						iv.setBannerInfo(bannerInfos.get(1));
					}
					
				});
			}else{
				iv.setBannerInfo(AdsInfoProvider.getInstance(getContext()).obtainBannerInfo().get(1));
				PLog.d("banner", "hasinfo");
			}
			
			int gravity = Gravity.TOP;
			if(args != null && args.length > 0){
				for(AdsArgs arg : args){
					if(arg.category == BaseAdsCategory.CATEGORY_BANNER){
						gravity = arg.bannerAdsGravity;
						break;
					}
				}
			}
			
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, gravity);
			
			rootLayout.addView(iv, params);
		}
	}

	@Override
	public Statistic getStatistic() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		rootLayout.removeView(iv);
	}

}
