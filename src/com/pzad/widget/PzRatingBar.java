package com.pzad.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class PzRatingBar extends LinearLayout {
	
	private PzStarView[] stars;
	
	private int padding;
	
	public PzRatingBar(Context context, int padding){
		super(context, null);
		
		this.padding = padding;
		
		init(context, null);
	}
	
	public PzRatingBar(Context context){
		this(context, null);
	}
	
	public PzRatingBar(Context context, AttributeSet attrs){
		super(context, attrs);
		
		init(context, attrs);
	}
	
	private void init(Context context, AttributeSet attrs){
		LinearLayout.LayoutParams starLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
		starLayoutParams.weight = 1.0F;
		starLayoutParams.setMargins(padding, 0, padding, 0);
		
		stars = new PzStarView[5];
		
		stars[0] = new PzStarView(context, attrs);
		stars[1] = new PzStarView(context, attrs);
		stars[2] = new PzStarView(context, attrs);
		stars[3] = new PzStarView(context, attrs);
		stars[4] = new PzStarView(context, attrs);
		
		addView(stars[0], starLayoutParams);
		addView(stars[1], starLayoutParams);
		addView(stars[2], starLayoutParams);
		addView(stars[3], starLayoutParams);
		addView(stars[4], starLayoutParams);
	}
	
	public void setRating(float rate){
		for(int i = 0; i < 5; i++){
			
			if(rate >= 1F){
				stars[i].setRate(1F);
			}else if(rate > 0F && rate < 1F){
				stars[i].setRate(rate);
			}
			
			rate--;
		}
	}
}
