package com.pzad.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AppDetailBlock extends RelativeLayout {
	
	private UrlImageView icon;
	
	private TextView appName;
	private TextView description;
	private TextView appSize;
	private PzRatingBar ratingBar;
	
	public AppDetailBlock(Context context){
		this(context, null);
	}
	
	public AppDetailBlock(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public AppDetailBlock(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}
}
