package com.pzad.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class AnimatedBall extends View {

	public AnimatedBall(Context context){
		this(context, null);
	}
	
	public AnimatedBall(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public AnimatedBall(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}
}
