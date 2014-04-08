package com.pzad.category.bannerads;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.pzad.broadcast.StatisticReceiver;
import com.pzad.category.AdsArgs;
import com.pzad.category.BaseAdsCategory;
import com.pzad.entities.AppInfo;
import com.pzad.entities.BannerInfo;
import com.pzad.entities.Statistic;
import com.pzad.net.AdsInfoProvider;
import com.pzad.net.AdsInfoProvider.OnAdsGotListener;
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
			iv = new BannerImageView(currentActivity){
				@Override
				public void onFinishImageLoad(){
					Statistic s = new Statistic();
					s.setName(getBannerInfo().getName(), Statistic.TYPE_BANNER);
					s.setExhibitionCount(1);
					
					Intent bannerIntent = new Intent();
					bannerIntent.setAction(StatisticReceiver.ACTION_RECEIVE_STATISTIC);
					bannerIntent.putExtra(StatisticReceiver.NAME, s);
					
					getContext().sendBroadcast(bannerIntent);
				}
			};
			if(!AdsInfoProvider.getInstance(getContext()).isAdsDataAvailable()){
				AdsInfoProvider.getInstance(getContext()).registerAdsGotListener(new OnAdsGotListener(){

					@Override
					public void onAdsGot(List<AppInfo> appInfos, List<BannerInfo> bannerInfos) {
						if(bannerInfos.size() > 0){
							iv.setBannerInfo(bannerInfos.get((int) Math.round(Math.random() * (bannerInfos.size() - 1))));
						}
					}
					
				});
			}else{
				if(AdsInfoProvider.getInstance(getContext()).obtainBannerInfo().size() > 0){
					iv.setBannerInfo(AdsInfoProvider.getInstance(getContext()).obtainBannerInfo().get((int) Math.round(Math.random() * (AdsInfoProvider.getInstance(getContext()).obtainBannerInfo().size() - 1))));
				}
			}
			
			int gravity = Gravity.TOP;
			if(args != null && args.length > 0){
				for(AdsArgs arg : args){
					if((arg.category & BaseAdsCategory.CATEGORY_BANNER) != 0){
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
