package com.pzad.category.tuiads;

import android.content.Context;
import android.content.Intent;

import com.pzad.category.AdsArgs;
import com.pzad.category.BaseAdsCategory;
import com.pzad.entities.Statistic;
import com.pzad.services.TuiService;

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

}
