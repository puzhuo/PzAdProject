package com.pzad.category;

public class AdsArgs {
	
	public int category;
	public int bannerAdsGravity;
	public long serviceDelayMillis;
	
	public static final long SERVICE_DELAY_SLOW = 2500L;
	public static final long SERVICE_DELAY_NORMAL = 1000L;
	public static final long SERVICE_DELAY_FAST = 200L;
	
	public AdsArgs(int bannerAdsGravity){
		category = BaseAdsCategory.CATEGORY_BANNER;
		this.bannerAdsGravity = bannerAdsGravity;
	}

	public AdsArgs(long serviceDelayMillis){
		category = BaseAdsCategory.CATEGORY_POPUP_ON_EXIT | BaseAdsCategory.CATEGORY_FLOAT;
		this.serviceDelayMillis = serviceDelayMillis;
	}
}
