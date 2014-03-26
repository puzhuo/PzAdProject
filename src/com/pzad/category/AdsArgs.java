package com.pzad.category;

public class AdsArgs {
	
	public int category;
	public int bannerAdsGravity;
	
	public AdsArgs(int bannerAdsGravity){
		category = BaseAdsCategory.CATEGORY_BANNER;
		this.bannerAdsGravity = bannerAdsGravity;
	}

}
