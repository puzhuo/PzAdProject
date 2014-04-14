package com.pzad.widget;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class AnimatedBall extends View {
	
	private int width;
	private int height;
	
	private int centerX;
	private int centerY;
	
	private Paint paint;

	public AnimatedBall(Context context){
		this(context, null);
	}
	
	public AnimatedBall(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public AnimatedBall(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
